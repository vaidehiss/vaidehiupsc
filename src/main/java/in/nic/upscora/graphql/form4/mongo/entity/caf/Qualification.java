package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Qualification {

    private Integer qualificationtypeId;
    private String otherQualificatoinName;
    private Integer qualificationLevelStatus;

    private Integer educationFromId;
    private String educationFromName;

    private Integer countryid;
    private String countryname;
    private String othercountrynm;

    private String destanceID;
    private String courseTypeName;

    private Integer passingStateId;
    private String passingName;

    private String certificateDate;
    private String rollNo;
    private Integer passoutYear;

    private Integer qualificationStream; 
    private String otherStreamName;

    private Integer intragratedcourseid;
    private String intragratedcourseName;

    private Integer graduateStream;
    private String graduateStreamName;

    private String subject;
    private Integer board;
    private String boardName;
    private String otherboardName;

    private Integer unversityId;
    private String unversityName;

    private String college;
    private Integer medium;
    private String mediumName;

    private Integer gradeSystemId;
    private String gradeSystemName;

    private Integer gradePointId;
    private Double marks;

    private String joinDate;
    private String completionDate;

    private Boolean abroadDegreeStatus;
}
