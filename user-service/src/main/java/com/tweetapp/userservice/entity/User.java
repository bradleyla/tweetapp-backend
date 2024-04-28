package com.tweetapp.userservice.entity;
;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(
        collection = "users"
)
public class User {
    @Id
    private ObjectId id;

    @NotNull(message = "User's first name must not be null")
    private String firstName;

    @NotNull(message = "User's last name must not be null")
    private String lastName;

    @NotNull(message = "User's username must not be null")
    @Indexed(unique = true)
    private String username;

    @NotNull(message = "User's email must not be null")
    @Indexed(unique = true)
    private String email;

    @NotNull(message = "User's password must not be null")
    private String password;

    @NotNull(message = "User's contact number must not be null")
    private String contactNumber;

    @DBRef
    private Set<Role> roles_ids = new HashSet<>();
}
