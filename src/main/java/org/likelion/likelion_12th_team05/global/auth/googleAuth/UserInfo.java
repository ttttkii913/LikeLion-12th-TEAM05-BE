package org.likelion.likelion_12th_team05.global.auth.googleAuth;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class UserInfo {
    private String id;
    private String name;
    private String email;

    @SerializedName("verified_email")
    private Boolean verifiedEmail;

}
