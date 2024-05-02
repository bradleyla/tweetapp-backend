package com.tweetapp.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.bson.types.ObjectId;
//import org.springframework.data.mongodb.core.index.Indexed;
//import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Document(
//        collection = "roles"
//)
@Table(
        name = "roles"
)
public class Role {
//    @Id
//    private ObjectId id;

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long roleId;

    @NotNull(message = "User's username must not be null")
//    @Indexed(unique = true)
    @Column(unique = true)
    private String name;
}
