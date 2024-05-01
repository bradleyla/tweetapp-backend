package com.cogent.replyservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String contactNumber;
    private Set<Role> roles_ids = new HashSet<>();
}
