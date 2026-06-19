package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndiaBirthplace {

    private Integer stateId;

    private Integer districtId;

    private String town;

    private String postOffice;

    private Integer pinCode;

    private String country;

    private String dist_nm;
}
