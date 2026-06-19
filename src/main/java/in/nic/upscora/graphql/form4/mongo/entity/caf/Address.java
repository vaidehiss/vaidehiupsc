package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private Boolean isSameAsCorrespondanceAddress;

    @BsonProperty("correspondanceAddress")
    private AddressDetails correspondanceAddress;

    private AddressDetails permanentAddress;

    private Boolean isFatherAddSameAsCorrespondance;
    private Boolean isFatherAddSameAsPermanent;

    @BsonProperty("fatherAddress")
    private AddressDetails fatherAddress;

    private Boolean isMotherAddSameAsCorrespondance;
    private Boolean isMotherAddSameAsPermanent;
    private Boolean isMotherAddSameAsFather;

    @BsonProperty("motherAddress")
    private AddressDetails motherAddress;

    private LocalDateTime updatedOn;

    @BsonProperty("i_on")
    private LocalDateTime createdOn;
}
