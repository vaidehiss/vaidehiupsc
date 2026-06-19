package in.nic.upscora.graphql.form4.utils;

import in.nic.upscora.graphql.form4.mongo.entity.application.OraApplication;

public class OraApplicationUtil {

    private OraApplicationUtil(){
    }

     public static String abbreviate(String value, int max) {
        if (value == null) {
            return "";
        }
        if (value.length() <= max) {
            return value;
        }
        return value.substring(0, Math.max(0, max - 3)) + "...";
    }

    public static String normalizePaymentStatus(OraApplication app) {
        if (app.getPayment_details() != null && app.getPayment_details().getStatus() != null) {
            return app.getPayment_details().getStatus();
        }
        return "Pending";
    }

    public static String normalizeCenter(OraApplication app, Integer preference) {
        if (app.getCenterPreferences() != null && !app.getCenterPreferences().isEmpty()) {
            int index = (preference != null && preference > 0) ? preference - 1 : 0;
            if (index < app.getCenterPreferences().size()) {
                return app.getCenterPreferences().get(index);
            }
        }
        return "";
    }

    public static String getSubmittedAt(OraApplication app) {
        if (app.getSubmittedAtIst() != null && !app.getSubmittedAtIst().isBlank()) {
            return app.getSubmittedAtIst();
        }
        if (app.getSubmittedAt() != null && !app.getSubmittedAt().isBlank()) {
            return app.getSubmittedAt();
        }
        if (app.getApplication_info() != null && app.getApplication_info().getSubmittedAt() != null) {
            return app.getApplication_info().getSubmittedAt().toString();
        }
        return "";
    }

    public static String getApplicationInfoStatus(OraApplication application) {
        if (application == null || application.getApplication_info() == null) {
            return "null";
        }
        return application.getApplication_info().getStatus();
    }

    public static boolean getCafLocked(OraApplication application) {
        return application != null
                && application.getApplication_info() != null
                && application.getApplication_info().isCafLocked();
    }

    public static boolean getProfileLocked(OraApplication application) {
        return application != null
                && application.getApplication_info() != null
                && application.getApplication_info().isProfileLocked();
    }
}
