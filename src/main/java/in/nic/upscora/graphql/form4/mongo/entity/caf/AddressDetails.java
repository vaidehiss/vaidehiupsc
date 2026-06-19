package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDetails {

    private String address;
    private Integer stateId;
    private Integer districtId;
    private String city;
    private Integer pin;
    private String nearestPostOffice;
}
