package com.tweetapp.userservice.entity;
;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
//import org.bson.types.ObjectId;
//import org.springframework.data.mongodb.core.index.Indexed;
//import org.springframework.data.mongodb.core.mapping.DBRef;
//import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
//@Document(
//        collection = "users"
//)
@Table(
        name = "users"
)
public class User {
//    @Id
//    private ObjectId id;

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long userId;

    @NotNull(message = "User's first name must not be null")
    private String firstName;

    @NotNull(message = "User's last name must not be null")
    private String lastName;

    @NotNull(message = "User's username must not be null")
//    @Indexed(unique = true)
    @Column(unique = true)
    private String username;

    @NotNull(message = "User's email must not be null")
//    @Indexed(unique = true)
    @Column(unique = true)
    private String email;

    @NotNull(message = "User's password must not be null")
    private String password;

    @NotNull(message = "User's contact number must not be null")
    private String contactNumber;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "userId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "roleId"
            )
    )
    private Set<Role> roles_ids = new HashSet<>();
}
