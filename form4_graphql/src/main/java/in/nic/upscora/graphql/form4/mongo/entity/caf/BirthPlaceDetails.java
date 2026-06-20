package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BirthPlaceDetails {

    private IndiaBirthplace indiaBirthplace;

    private ForeignBirthPlace foreingCountryBirthPlace;
    
}
