package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentDetails {

    private String name;

    private Integer status;

    private Integer deathYear;

    private Integer occupationId;

    private String occupationName;

    private String occupationOther;

    private Integer income;

    private Integer nationalityId;

    private String nationalityOther;

    private Integer stateId;

    private Integer districtId;

    private String districtName;
}
