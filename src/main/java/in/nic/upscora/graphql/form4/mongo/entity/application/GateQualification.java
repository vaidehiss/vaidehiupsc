package in.nic.upscora.graphql.form4.mongo.entity.application;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GateQualification {

    private String requirementType;
    private String isGateQualified;
    private String examPaper;
    private String examPaperOther;
    private String percentileScore;
    private String allIndiaRank;
    private String examYear;
    private String dateOfResult;
    private String scoreValidUpto;
    private String gateScoreCard;
    
   
}
