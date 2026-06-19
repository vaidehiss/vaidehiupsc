package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UidDetails {

    private Integer is_uid_consent;

    private String consent;
    private String nm;
    private String dob;
    private String gn_nm;
    private String lmrk;
    private String loc;
    private String house;
    private String street;
    private String st;
    private String pin;
    private String dis;
    private String l4;
    private String contry;
    private String adv_tkn;
    private String adv_txn;
    private String txn;
    private String tkn;
    private String da_status;
}
