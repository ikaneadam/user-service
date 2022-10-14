package com.userservice;

import com.userservice.data.entity.Users;
import com.userservice.data.repository.UserRepository;
import com.userservice.domain.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTests {
    private String testUsername = "usernametest";
    private Integer noExistingId = 5466;


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @BeforeAll
    public void initializeTestData() {

    }

    @Test
    public void DoesUserExistTest(){
        Users user = new Users();
        user.setUsername(testUsername);
        userRepository.save(user);

        boolean userExistStatusTrueByUserName = userService.doesUserExistByUserName(testUsername);
        boolean userExistStatusFalseByUserName = userService.doesUserExistByUserName("NoExistingUser");

        boolean userExistStatusTrueById = userService.doesUserExistById(user.getId());
        boolean userExistStatusFalseById = userService.doesUserExistById(noExistingId);

        assertThat(userExistStatusTrueByUserName).isEqualTo(true);
        assertThat(userExistStatusTrueById).isEqualTo(true);
        assertThat(userExistStatusFalseByUserName).isEqualTo(false);
        assertThat(userExistStatusFalseById).isEqualTo(false);
    }

    @Test
    public void putUserTest(){
        // created user
        Users user = new Users();
        user.setUsername("putUser");
        userRepository.save(user);
        // to update user
        Users ToUpdateUser = new Users();
        ToUpdateUser.setUsername("putUserTest");

        Users updatedUser = userService.putUser(ToUpdateUser,user.getId());


        Users updatedUserFound = userRepository.getById(user.getId());

        assertThat(updatedUser.getUsername()).isEqualTo(updatedUserFound.getUsername());
    }


    @Test
    public void deleteUserTest(){
        Users user = new Users();
        user.setUsername("todeleteUser");
        userRepository.save(user);

        Optional<Users> notYetDeletedUser = userRepository.findById(user.getId());
        assertThat(notYetDeletedUser.isPresent()).isEqualTo(true);


        userService.deleteUser(user.getId());

        Optional<Users> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser.isPresent()).isEqualTo(false);
    }

}
