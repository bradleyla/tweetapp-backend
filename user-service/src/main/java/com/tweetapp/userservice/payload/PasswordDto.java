package com.tweetapp.userservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {
    private String email;
    private String password;
    private String confirmPassword;
    private String newPassword;
}
