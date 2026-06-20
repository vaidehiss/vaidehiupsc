package in.nic.upscora.graphql.form4.mongo.entity.application;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswer {

    private String questionId;
    private String question;
    private String answerType;
    private String answer;
    private String document;

   
}
