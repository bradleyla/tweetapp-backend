package com.tweetapp.userservice.repository;

import com.tweetapp.userservice.entity.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            userRepository.delete(user);
        }
    }


    // save()
    @DisplayName("Save User JUnit Test")
    @Test
    public void givenUserObject_whenSave_thenReturnSavedUserObject() {
        User user = User.builder()
                .firstName("Mark")
                .lastName("Hamill")
                .username("mark123")
                .email("m@gmail.com")
                .password("12345678")
                .contactNumber("1234567890")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
    }

    // findAll()

    @DisplayName("Find All Users JUnit Test")
    @Test
    public void givenUserList_whenFindAll_returnUserList() {
        User user1 = User.builder()
                .firstName("Mark")
                .lastName("Hamill")
                .username("mark123")
                .email("m@gmail.com")
                .password("12345678")
                .contactNumber("1234567890")
                .build();

        User user2 = User.builder()
                .firstName("Luke")
                .lastName("Bradley")
                .username("luke123")
                .email("l@gmail.com")
                .password("87654321")
                .contactNumber("0987654321")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> userList = userRepository.findAll();

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
        assertThat(userList.get(0).getFirstName()).isEqualTo("Mark");
        assertThat(userList.get(1).getFirstName()).isEqualTo("Luke");
    }

    @DisplayName("Find All Users JUnit Test Failed")
    @Test
    public void givenNoUsers_whenFindAll_returnNoUsers() {
        User user1 = User.builder()
                .firstName("Mark")
                .lastName("Hamill")
                .username("mark123")
                .email("m@gmail.com")
                .password("12345678")
                .contactNumber("1234567890")
                .build();

        User user2 = User.builder()
                .firstName("Luke")
                .lastName("Bradley")
                .username("luke123")
                .email("l@gmail.com")
                .password("87654321")
                .contactNumber("0987654321")
                .build();

        List<User> userList = userRepository.findAll();

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(0);
    }

    // findById()
    @DisplayName("Find By Id Users JUnit Test")
    @Test
    public void givenUserObject_whenFindById_returnUserObject() {
        User user1 = User.builder()
                .firstName("Mark")
                .lastName("Hamill")
                .username("mark123")
                .email("m@gmail.com")
                .password("12345678")
                .contactNumber("1234567890")
                .build();

        User user2 = User.builder()
                .firstName("Luke")
                .lastName("Bradley")
                .username("luke123")
                .email("l@gmail.com")
                .password("87654321")
                .contactNumber("0987654321")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        User foundUser1 = userRepository.findById(user1.getId()).get();
        User foundUser2 = userRepository.findById(user2.getId()).get();


        assertThat(foundUser1).isNotNull();
        assertThat(foundUser2).isNotNull();
        assertThat(foundUser1.getId()).isNotNull();
        assertThat(foundUser2.getId()).isNotNull();
        assertThat(foundUser1.getFirstName()).isEqualTo("Mark");
        assertThat(foundUser2.getFirstName()).isEqualTo("Luke");
    }

    @DisplayName("Find By Id Users JUnit Test Failed")
    @Test
    public void givenNoUser_whenFindById_returnNoSuchElementException() {
        User user1 = User.builder()
                .firstName("Mark")
                .lastName("Hamill")
                .username("mark123")
                .email("m@gmail.com")
                .password("12345678")
                .contactNumber("1234567890")
                .build();

        User user2 = User.builder()
                .id(new ObjectId())
                .firstName("Luke")
                .lastName("Bradley")
                .username("luke123")
                .email("l@gmail.com")
                .password("87654321")
                .contactNumber("0987654321")
                .build();

        userRepository.save(user1);

        User foundUser1 = userRepository.findById(user1.getId()).get();

        assertThrows(NoSuchElementException.class, () -> {
            User foundUser2 = userRepository.findById(user2.getId()).get();
        });
    }

    @DisplayName("Find By Email User JUnit Test")
    @Test
    public void givenUserObject_whenFindByEmail_returnUserObject() {
        User user1 = User.builder()
                .firstName("Mark")
                .lastName("Hamill")
                .username("mark123")
                .email("m@gmail.com")
                .password("12345678")
                .contactNumber("1234567890")
                .build();

        User user2 = User.builder()
                .firstName("Luke")
                .lastName("Bradley")
                .username("luke123")
                .email("l@gmail.com")
                .password("87654321")
                .contactNumber("0987654321")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        User foundUser1 = userRepository.findByEmail(user1.getEmail()).get();
        User foundUser2 = userRepository.findByEmail(user2.getEmail()).get();


        assertThat(foundUser1).isNotNull();
        assertThat(foundUser2).isNotNull();
        assertThat(foundUser1.getId()).isNotNull();
        assertThat(foundUser2.getId()).isNotNull();
        assertThat(foundUser1.getFirstName()).isEqualTo("Mark");
        assertThat(foundUser2.getFirstName()).isEqualTo("Luke");
    }

    @DisplayName("Find By Email User JUnit Test Failed")
    @Test
    public void givenNoUser_whenFindByEmail_returnNoSuchElementException() {
        User user1 = User.builder()
                .firstName("Mark")
                .lastName("Hamill")
                .username("mark123")
                .email("m@gmail.com")
                .password("12345678")
                .contactNumber("1234567890")
                .build();

        User user2 = User.builder()
                .firstName("Luke")
                .lastName("Bradley")
                .username("luke123")
                .email("l@gmail.com")
                .password("87654321")
                .contactNumber("0987654321")
                .build();

        userRepository.save(user1);

        User foundUser1 = userRepository.findByEmail(user1.getEmail()).get();

        assertThrows(NoSuchElementException.class, () -> {
            User foundUser2 = userRepository.findByEmail(user2.getEmail()).get();
        });
    }

    @DisplayName("Find By Username User JUnit Test")
    @Test
    public void givenUserObject_whenFindByUsername_returnUserObject() {
        User user1 = User.builder()
                .firstName("Mark")
                .lastName("Hamill")
                .username("mark123")
                .email("m@gmail.com")
                .password("12345678")
                .contactNumber("1234567890")
                .build();

        User user2 = User.builder()
                .firstName("Luke")
                .lastName("Bradley")
                .username("luke123")
                .email("l@gmail.com")
                .password("87654321")
                .contactNumber("0987654321")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        User foundUser1 = userRepository.findByUsername(user1.getUsername()).get();
        User foundUser2 = userRepository.findByUsername(user2.getUsername()).get();


        assertThat(foundUser1).isNotNull();
        assertThat(foundUser2).isNotNull();
        assertThat(foundUser1.getId()).isNotNull();
        assertThat(foundUser2.getId()).isNotNull();
        assertThat(foundUser1.getFirstName()).isEqualTo("Mark");
        assertThat(foundUser2.getFirstName()).isEqualTo("Luke");
    }

    @DisplayName("Find By Username User JUnit Test Failed")
    @Test
    public void givenNoUser_whenFindByUsername_returnNoSuchElementException() {
        User user1 = User.builder()
                .firstName("Mark")
                .lastName("Hamill")
                .username("mark123")
                .email("m@gmail.com")
                .password("12345678")
                .contactNumber("1234567890")
                .build();

        User user2 = User.builder()
                .firstName("Luke")
                .lastName("Bradley")
                .username("luke123")
                .email("l@gmail.com")
                .password("87654321")
                .contactNumber("0987654321")
                .build();

        userRepository.save(user1);

        User foundUser1 = userRepository.findByUsername(user1.getUsername()).get();

        assertThrows(NoSuchElementException.class, () -> {
            User foundUser2 = userRepository.findByUsername(user2.getUsername()).get();
        });
    }

    @DisplayName("Find By Username Or Email User JUnit Test")
    @Test
    public void givenUserObject_whenFindByUsernameOrEmail_returnUserObject() {
        User user1 = User.builder()
                .firstName("Mark")
                .lastName("Hamill")
                .username("mark123")
                .email("m@gmail.com")
                .password("12345678")
                .contactNumber("1234567890")
                .build();

        User user2 = User.builder()
                .firstName("Luke")
                .lastName("Bradley")
                .username("luke123")
                .email("l@gmail.com")
                .password("87654321")
                .contactNumber("0987654321")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        User foundUser1 = userRepository.findByUsernameOrEmail(user1.getUsername(), user1.getEmail()).get();
        User foundUser2 = userRepository.findByUsernameOrEmail(user2.getUsername(), user2.getEmail()).get();


        assertThat(foundUser1).isNotNull();
        assertThat(foundUser2).isNotNull();
        assertThat(foundUser1.getId()).isNotNull();
        assertThat(foundUser2.getId()).isNotNull();
        assertThat(foundUser1.getFirstName()).isEqualTo("Mark");
        assertThat(foundUser2.getFirstName()).isEqualTo("Luke");
    }

    @DisplayName("Find By Username Or Email User JUnit Test Failed")
    @Test
    public void givenNoUser_whenFindByUsernameOrEmail_returnNoSuchElementException() {
        User user1 = User.builder()
                .firstName("Mark")
                .lastName("Hamill")
                .username("mark123")
                .email("m@gmail.com")
                .password("12345678")
                .contactNumber("1234567890")
                .build();

        User user2 = User.builder()
                .firstName("Luke")
                .lastName("Bradley")
                .username("luke123")
                .email("l@gmail.com")
                .password("87654321")
                .contactNumber("0987654321")
                .build();

        userRepository.save(user1);

        User foundUser1 = userRepository.findByUsernameOrEmail(user1.getUsername(), user1.getEmail()).get();

        assertThrows(NoSuchElementException.class, () -> {
            User foundUser2 = userRepository.findByUsernameOrEmail(user2.getUsername(), user2.getEmail()).get();
        });
    }


}
