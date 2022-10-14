package com.userservice;

import com.userservice.data.entity.Users;
import com.userservice.data.repository.UserRepository;
import com.userservice.web.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class UserControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void GetUserTest() {
        Users user = new Users();
        user.setUsername("usernamee");
        userRepository.save(user);

        String path = "http://localhost:" + port + "/user/" + user.getId();
        String response = this.restTemplate.getForObject(path, String.class);
        String pathNoneExistingUser = "http://localhost:" + port + "/user/" + 32442;
        String responseNoneExistingUser = this.restTemplate.getForObject(pathNoneExistingUser, String.class);

        assertThat(response).contains(getUserJsonResponse(user.getId(), user.getUsername()));
        assertThat(responseNoneExistingUser).contains("No user found with given id");
    }

    @Test
    public void registerTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserDto user = new UserDto();
        user.setUsername("registerTest");
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(user, headers);

        String path = "http://localhost:" + port + "/user/register";
        String registerNoneExistingUserResponse = this.restTemplate.postForObject(path, httpEntity, String.class);
        String registerExistingUserResponse = this.restTemplate.postForObject(path, httpEntity, String.class);

        assertThat(registerNoneExistingUserResponse).contains("true");
        assertThat(registerExistingUserResponse).contains("false");
    }

    private String getUserJsonResponse(Integer id, String username){
        return "{\"id\":" + id + ",\"username\":\"" + username +"\"}";
    }

    private String JsonUsername(String username){
        return "{\"username\":\"" + username +"\"}";
    }
}
