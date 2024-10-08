package org.likelion.likelion_12th_team05.global.auth.googleAuth;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GoogleToken {
    @SerializedName("access_token")
    private String accessToken;
}
