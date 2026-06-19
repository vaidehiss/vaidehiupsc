package in.nic.upscora.graphql.form4.mongo.entity.application;



import org.eclipse.microprofile.graphql.DefaultValue;

import io.smallrye.graphql.api.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssistiveDeviceType {

    @Nullable
    @DefaultValue("false")
    private boolean dev_handheld;

    @Nullable
    @DefaultValue("false")
    private boolean dev_mag_glass;

    @Nullable
    @DefaultValue("false")
    private boolean dev_medicine;

    @Nullable
    @DefaultValue("false")
    private boolean dev_lamp;

    @Nullable
    @DefaultValue("false")
    private boolean dev_bioptic;

    @Nullable
    @DefaultValue("false")
    private boolean dev_black_spec;

    @Nullable
    @DefaultValue("false")
    private boolean dev_braille;

    @Nullable
    @DefaultValue("false")
    private boolean dev_digi_led;

    @Nullable
    @DefaultValue("false")
    private boolean dev_dome;

    @Nullable
    @DefaultValue("false")
    private boolean dev_electric;

    @Nullable
    @DefaultValue("false")
    private boolean dev_hands_free;

    @Nullable
    @DefaultValue("false")
    private boolean dev_magnifier;

    @Nullable
    @DefaultValue("false")
    private boolean dev_medical;

    @Nullable
    @DefaultValue("false")
    private boolean dev_plain_paper;

    @Nullable
    @DefaultValue("false")
    private boolean dev_pocket;

    @Nullable
    @DefaultValue("false")
    private boolean dev_taylor;

    @Nullable
    @DefaultValue("false")
    private boolean dev_tissue;

    @Nullable
    @DefaultValue("false")
    private boolean dev_hearing;

    @Nullable
    @DefaultValue("false")
    private boolean dev_wheelchair;

    @Nullable
    @DefaultValue("false")
    private boolean dev_prosthetics;

    @Nullable
    @DefaultValue("false")
    private boolean dev_others;


    
    
}
