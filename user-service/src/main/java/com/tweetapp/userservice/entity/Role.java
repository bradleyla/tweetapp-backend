package com.tweetapp.userservice.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(
        collection = "roles"
)
public class Role {
    @Id
    private ObjectId id;

    @NotNull(message = "User's username must not be null")
    @Indexed(unique = true)
    private String name;
}
