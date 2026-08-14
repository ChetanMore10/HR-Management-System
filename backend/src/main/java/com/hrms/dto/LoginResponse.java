package com.hrms.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String token;
    private String name;
    private String email;
    private String role;

}
