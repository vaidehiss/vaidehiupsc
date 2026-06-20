package in.nic.upscora.graphql.form4.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.subscription.MultiEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import in.nic.upscora.graphql.form4.cache.MasterDataCache;
import in.nic.upscora.graphql.form4.cache.MasterDataConstants;
import in.nic.upscora.graphql.form4.mongo.entity.application.OraApplication;
import in.nic.upscora.graphql.form4.mongo.entity.application.OraExam1Report;
import in.nic.upscora.graphql.form4.mongo.entity.caf.CandidateProfile;
import in.nic.upscora.graphql.form4.mongo.entity.caf.Community;
import in.nic.upscora.graphql.form4.mongo.repository.OraApplicationRepository;
import in.nic.upscora.graphql.form4.mongo.repository.OraExam1ReportRepository;

@Slf4j
@ApplicationScoped
public class OraExam1ReportService {

    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private static final String PAYMENT_UPI = "UPI";
    private static final String PAYMENT_CREDIT_CARD = "Credit Card";
    private static final String PAYMENT_DEBIT_CARD = "Debit Card";
    private static final String PAYMENT_NETBANKING_SBI = "Netbanking (SBI)";
    private static final String PAYMENT_NETBANKING_OTHERS = "Netbanking (Others)";
    private static final String PAYMENT_EXEMPTED = "Exempted";
    private static final String PAYMENT_TOTAL = "Total";

    private static final String UNKNOWN = "unknown";

    private final Instance<OraApplicationRepository> oraRepo;
    private final Instance<OraExam1ReportRepository> reportRepo;
    private final MasterDataCache masterDataCache;

    public OraExam1ReportService(Instance<OraApplicationRepository> oraRepo,
            Instance<OraExam1ReportRepository> reportRepo, MasterDataCache masterDataCache) {
        this.oraRepo = oraRepo;
        this.reportRepo = reportRepo;
        this.masterDataCache = masterDataCache;
    }

    private final Set<SubscriptionHandle> subscribers = ConcurrentHashMap.newKeySet();

    // @CacheResult(cacheName = "ora-reports-by-vacancy")
    public OraExam1Report getOrBuildReport(String vacancyId) {
        OraExam1Report report = reportRepo.get().findByVacancyId(vacancyId);
        if (report != null) {
            return report;
        }
        return rebuildReport(vacancyId, false);
    }

    // @CacheInvalidate(cacheName = "ora-reports-by-vacancy")
    public OraExam1Report refreshForVacancy(String vacancyId) {
        return rebuildReport(vacancyId, true);
    }

    // @CacheInvalidate(cacheName = "ora-reports-by-vacancy")
    public void invalidateReportCache(String vacancyId) {
        log.info("invalidateReportCache | request={{\"vacancyId\":\"{}\"}}", vacancyId);
    }

    public Multi<OraExam1Report> subscribe(String vacancyId) {
        return Multi.createFrom().emitter(emitter -> {
            SubscriptionHandle handle = new SubscriptionHandle(vacancyId, emitter);
            cleanupSubscribers();
            subscribers.add(handle);
            emitter.onTermination(() -> closeSubscription(handle));
            Uni.createFrom().item(() -> getOrBuildReport(vacancyId))
                    .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                    .subscribe().with(
                            emitter::emit,
                            failure -> {
                                closeSubscription(handle);
                                if (!emitter.isCancelled()) {
                                    emitter.fail(failure);
                                }
                            });
        });
    }

    public void refreshFromApplication(OraApplication oraApplication) {
        if (oraApplication == null || oraApplication.getVacancyId() == null
                || oraApplication.getVacancyId().isBlank()) {
            return;
        }
        refreshForVacancy(oraApplication.getVacancyId());
    }

    private OraExam1Report rebuildReport(String vacancyId, boolean publish) {
        log.info("rebuildReport | step=start | vacancyId={} | publish={}", vacancyId, publish);
        log.info("rebuildReport | step=fetch_applications | vacancyId={}", vacancyId);
        List<OraApplication> applications = oraRepo.get().findByVacancyId(vacancyId);
        log.info("rebuildReport | step=applications_fetched | vacancyId={} | count={}", vacancyId,
                applications == null ? 0 : applications.size());

        log.info("rebuildReport | step=fetch_existing_report | vacancyId={}", vacancyId);
        OraExam1Report existing = reportRepo.get().findByVacancyId(vacancyId);
        log.info("rebuildReport | step=existing_report_fetched | vacancyId={} | exists={}", vacancyId,
                existing != null);

        log.info("rebuildReport | step=build_report | vacancyId={}", vacancyId);
        OraExam1Report report = buildReport(vacancyId, applications, existing);
        log.info("rebuildReport | step=report_built | vacancyId={} | vacancyNumber={} | vacancyTitle={}",
                vacancyId, report.getVacancyNumber(), report.getVacancyTitle());

        log.info("rebuildReport | step=persist_report | vacancyId={}", vacancyId);
        reportRepo.get().persistOrUpdate(report);
        log.info("rebuildReport | step=report_persisted | vacancyId={}", vacancyId);
        if (publish) {
            log.info("rebuildReport | step=publish_report | vacancyId={}", vacancyId);
            publish(report);
            log.info("rebuildReport | step=report_published | vacancyId={}", vacancyId);
        }
        log.info("rebuildReport | step=complete | vacancyId={}", vacancyId);
        return report;
    }

    // 1. The new refactored main method (Drastically reduced LOC and Variables)
    private OraExam1Report buildReport(String vacancyId, List<OraApplication> applications, OraExam1Report existing) {
        ReportAggregator aggregator = new ReportAggregator(existing);

        applications.stream()
                .filter(app -> !Boolean.TRUE.equals(app.getTesting()))
                .forEach(aggregator::processApplication);

        return aggregator.build(vacancyId);
    }

    // 2. The Aggregator Helper Class
    private class ReportAggregator {

        private final Map<String, Long> statusBreakup = new LinkedHashMap<>();
        private final Map<String, OraExam1Report.BucketCount> centerWise = new LinkedHashMap<>();
        private final Map<String, OraExam1Report.BucketCount> communityWise = new LinkedHashMap<>();
        private final Map<String, OraExam1Report.BucketCount> genderWise = new LinkedHashMap<>();
        private final Map<String, OraExam1Report.BucketCount> commGenderMatrix = new LinkedHashMap<>();
        private final Map<String, OraExam1Report.CenterPreferenceBreakup> centerPrefMap = new LinkedHashMap<>();

        private long upi;
        private long creditCard;
        private long debitCard;
        private long netSbi;
        private long netOthers;
        private long exempted;
        private long totalPaid;
        private long totalSubmissions;
        private String vacancyTitle;
        private String vacancyNumber;

        ReportAggregator(OraExam1Report existing) {
            if (existing != null) {
                this.vacancyTitle = existing.getVacancyTitle();
                this.vacancyNumber = existing.getVacancyNumber();
            }
        }

        void processApplication(OraApplication app) {
            String status = normalizeStatus(getApplicationStatus(app));
            statusBreakup.merge(status, 1L, Long::sum);

            boolean submitted = "SUBMITTED".equalsIgnoreCase(status);
            if (submitted) {
                totalSubmissions++;
            }

            updateDemographics(app, submitted);
            updateCenters(app, submitted);
            updatePayment(app);
            updateVacancyDetails(app);
        }

        private void updateDemographics(OraApplication app, boolean submitted) {
            String community = normalizeCommunity(app);
            String gender = normalizeGender(app);

            incrementBucket(communityWise, community, submitted);
            incrementBucket(genderWise, gender, submitted);

            incrementBucket(commGenderMatrix, community + "_" + gender, submitted);

        }

        private void updateCenters(OraApplication app, boolean submitted) {
            String firstPref = getCenterPreference(app, 0);
            if (firstPref != null && !firstPref.isBlank()) {
                incrementBucket(centerWise, normalizeCenter(firstPref), submitted);
            }

            if (submitted) {
                incrementCenterPreferenceBreakup(centerPrefMap, firstPref, true);
                incrementCenterPreferenceBreakup(centerPrefMap, getCenterPreference(app, 1), false);
            }
        }

        private void updatePayment(OraApplication app) {
            boolean isExempted = false;

            if (app.getPayment_details() != null && app.getPayment_details().getStatus() != null) {
                String payStatus = app.getPayment_details().getStatus().trim().toUpperCase(Locale.ROOT);
                if ("SUCCESS".equals(payStatus) || "PAID".equals(payStatus)) {
                    totalPaid++;
                    categorizePaymentMode(normalizePaymentMode(app.getPayment_details().getPayment_mode()));
                }
            } else if (app.getApplication_info() != null
                    && "SUBMITTED".equalsIgnoreCase(app.getApplication_info().getStatus())) {
                isExempted = true;
            }

            if (isExempted) {
                exempted++;
            }
        }

        private void categorizePaymentMode(String mode) {
            if (PAYMENT_UPI.equals(mode)) {
                upi++;
            } else if (PAYMENT_CREDIT_CARD.equals(mode)) {
                creditCard++;
            } else if (PAYMENT_DEBIT_CARD.equals(mode)) {
                debitCard++;
            } else if (PAYMENT_NETBANKING_SBI.equals(mode)) {
                netSbi++;
            } else {
                netOthers++;
            }
        }

        private void updateVacancyDetails(OraApplication app) {
            if (vacancyTitle == null || vacancyTitle.isBlank()) {
                vacancyTitle = getVacancyTitle(app);
            }
            if (vacancyNumber == null || vacancyNumber.isBlank()) {
                vacancyNumber = getVacancyNumber(app);
            }
        }

        OraExam1Report build(String vacancyId) {
            Map<String, Long> paymentBreakup = new LinkedHashMap<>();
            paymentBreakup.put(PAYMENT_UPI, upi);
            paymentBreakup.put(PAYMENT_CREDIT_CARD, creditCard);
            paymentBreakup.put(PAYMENT_DEBIT_CARD, debitCard);
            paymentBreakup.put(PAYMENT_NETBANKING_SBI, netSbi);
            paymentBreakup.put(PAYMENT_NETBANKING_OTHERS, netOthers);
            paymentBreakup.put(PAYMENT_EXEMPTED, exempted);
            paymentBreakup.put(PAYMENT_TOTAL, totalSubmissions);

            List<OraExam1Report.CenterPreferenceBreakup> centerPrefList = new ArrayList<>(centerPrefMap.values());
            centerPrefList.sort(Comparator.comparing(OraExam1Report.CenterPreferenceBreakup::getCenterName,
                    String.CASE_INSENSITIVE_ORDER));

            OraExam1Report.PaymentDetailsBreakup detailsBreakup = OraExam1Report.PaymentDetailsBreakup.builder()
                    .upi(upi).creditCard(creditCard).debitCard(debitCard).netbankingSbi(netSbi)
                    .netbankingOthers(netOthers).exempted(exempted).totalPaid(totalPaid).total(totalSubmissions)
                    .build();

            LocalDateTime lastUpdated = LocalDateTime.now(UTC_ZONE);
            String lastUpdatedIst = lastUpdated.atZone(UTC_ZONE).withZoneSameInstant(ZoneId.of("Asia/Kolkata"))
                    .toLocalDateTime().toString();

            return OraExam1Report.builder()
                    .id(vacancyId).vacancyId(vacancyId).vacancyNumber(vacancyNumber).vacancyTitle(vacancyTitle)
                    .applicationStatusBreakup(statusBreakup).centerWise(centerWise)
                    .communityGenderMatrix(commGenderMatrix)
                    .communityWise(communityWise).genderWise(genderWise).paymentBreakup(paymentBreakup)
                    .paymentDetailsBreakup(detailsBreakup).centerPreferenceBreakup(centerPrefList)
                    .lastUpdated(lastUpdated).lastUpdatedIst(lastUpdatedIst)
                    .build();
        }

        private String normalizePaymentMode(String mode) {
            if (mode == null || mode.isBlank()) {
                return PAYMENT_NETBANKING_OTHERS;
            }
            String modeLower = mode.trim().toLowerCase(Locale.ROOT);
            if (modeLower.contains("upi") || modeLower.equals("u")) {
                return PAYMENT_UPI;
            }
            if (modeLower.contains("credit") || modeLower.equals("c")) {
                return PAYMENT_CREDIT_CARD;
            }
            if (modeLower.contains("debit") || modeLower.equals("d")) {
                return PAYMENT_DEBIT_CARD;
            }
            if (modeLower.contains("sbi") || modeLower.equals("i")) {
                return PAYMENT_NETBANKING_SBI;
            }
            return PAYMENT_NETBANKING_OTHERS;
        }

        private void incrementBucket(Map<String, OraExam1Report.BucketCount> buckets, String key, boolean submitted) {
            String bucketKey = (key == null || key.isBlank()) ? UNKNOWN : key;
            OraExam1Report.BucketCount current = buckets.get(bucketKey);
            if (current == null) {
                current = OraExam1Report.BucketCount.builder().build();
            }
            if (submitted) {
                current.setSubmittedCount(current.getSubmittedCount() + 1);
            } else {
                current.setPendingCount(current.getPendingCount() + 1);
            }
            buckets.put(bucketKey, current);
        }

        private String getApplicationStatus(OraApplication application) {
            if (application == null || application.getApplication_info() == null
                    || application.getApplication_info().getStatus() == null) {
                return "PENDING";
            }
            return application.getApplication_info().getStatus();
        }

        private String getVacancyTitle(OraApplication application) {
            if (application == null || application.getApplication_info() == null) {
                return null;
            }
            return application.getApplication_info().getPost_name();
        }

        private String getVacancyNumber(OraApplication application) {
            if (application == null || application.getApplication_info() == null) {
                return null;
            }
            return application.getApplication_info().getRecruitmentCode();
        }

        private String normalizeStatus(String status) {
            if (status == null || status.isBlank()) {
                return "PENDING";
            }
            return status.trim().toUpperCase(Locale.ROOT);
        }

        private String normalizeCenter(String centerPreference) {
            if (centerPreference == null || centerPreference.isBlank()) {
                return UNKNOWN;
            }
            return normalizeLabel(centerPreference);
        }

        private String normalizeCommunity(OraApplication application) {
            CandidateProfile caf = application == null ? null : application.getCaf_details();
            Community community = caf == null ? null : caf.getCommunity();
            if (community == null) {
                return UNKNOWN;
            }
            if (community.getCategoryId() != null) {
                String mapped = masterDataCache.lookup(MasterDataConstants.COMMUNITIES,
                        String.valueOf(community.getCategoryId()));
                if (mapped != null) {
                    return normalizeLabel(mapped);
                }
            }
            return UNKNOWN;
        }

        private String normalizeGender(OraApplication application) {
            CandidateProfile caf = application == null ? null : application.getCaf_details();
            if (caf != null && caf.getPreIdentity() != null && caf.getPreIdentity().getGenderId() != null) {
                String mapped = masterDataCache.lookup(MasterDataConstants.GENDERS,
                        String.valueOf(caf.getPreIdentity().getGenderId()));
                if (mapped != null) {
                    return normalizeGenderLabel(mapped);
                }
            }
            return UNKNOWN;
        }

        private void incrementCenterPreferenceBreakup(
                Map<String, OraExam1Report.CenterPreferenceBreakup> centerPreferenceBreakupMap,
                String centerPreference,
                boolean firstPreference) {
            if (centerPreference == null || centerPreference.isBlank()) {
                return;
            }
            String centerValue = centerPreference.trim();
            OraExam1Report.CenterPreferenceBreakup breakup = centerPreferenceBreakupMap.get(centerValue);
            if (breakup == null) {
                breakup = OraExam1Report.CenterPreferenceBreakup.builder()
                        .centerCode(centerValue)
                        .centerName(centerValue)
                        .build();
            }
            if (firstPreference) {
                breakup.setFirstPreferenceCount(breakup.getFirstPreferenceCount() + 1);
            } else {
                breakup.setSecondPreferenceCount(breakup.getSecondPreferenceCount() + 1);
            }
            centerPreferenceBreakupMap.put(centerValue, breakup);
        }

        private String getCenterPreference(OraApplication application, int preferenceIndex) {
            if (application == null
                    || application.getCenterPreferences() == null
                    || application.getCenterPreferences().size() <= preferenceIndex) {
                return null;
            }
            String centerPreference = application.getCenterPreferences().get(preferenceIndex);
            if (centerPreference == null || centerPreference.isBlank()) {
                return null;
            }
            return centerPreference.trim();
        }

        private String normalizeGenderLabel(String value) {
            String normalized = normalizeLabel(value);
            if (normalized.equals("m") || normalized.equals("male")) {
                return "male";
            }
            if (normalized.equals("f") || normalized.equals("female")) {
                return "female";
            }
            if (normalized.equals("t") || normalized.equals("trans") || normalized.equals("transgender")) {
                return "transgender";
            }
            return normalized;
        }

        private String normalizeLabel(String value) {
            if (value == null) {
                return UNKNOWN;
            }
            String normalized = value.trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_");
            if (normalized.isBlank()) {
                return UNKNOWN;
            }
            return normalized;
        }

        // ReportAggregator End

    }

    private void publish(OraExam1Report report) {
        cleanupSubscribers();
        for (SubscriptionHandle subscriber : new ArrayList<>(subscribers)) {
            if (!subscriber.matches(report.getVacancyId()) || subscriber.emitter.isCancelled()) {
                subscribers.remove(subscriber);
                continue;
            }
            try {
                subscriber.emitter.emit(report);
            } catch (Exception e) {
                subscribers.remove(subscriber);
                log.warn("publish | droppedCancelledSubscriber | vacancyId={}", report.getVacancyId(), e);
            }
        }
    }

    private void cleanupSubscribers() {
        subscribers.removeIf(handle -> handle == null || handle.emitter() == null || handle.emitter().isCancelled());
    }

    private void closeSubscription(SubscriptionHandle handle) {
        if (handle != null) {
            subscribers.remove(handle);
        }
    }

    private record SubscriptionHandle(String vacancyId, MultiEmitter<? super OraExam1Report> emitter) {
        boolean matches(String reportVacancyId) {
            return Objects.equals(vacancyId, reportVacancyId);
        }
    }
}
