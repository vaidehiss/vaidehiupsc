package in.nic.upscora.graphql.form4.mongo.entity.application;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OraQualification {
    
    private String qualificationLevel;
    private String degree;
    private String subject;
    private String specialization;
    private String medium;
    private String university;
    private String college;
    private String durationFromMonth;
    private String durationFromYear;
    private String durationToMonth;
    private String durationToYear;
    private String notificationDate;
    private String division;
    private String resultType;
    private String resultScore;
    private String degreeDate;
    private String notAwarded;
    private String degreeCertificate;
    private String marksheet;
}
