package com.userservice.web.controller;

import com.userservice.data.entity.Users;
import com.userservice.domain.exceptions.NoUserFoundWithGivenId;
import com.userservice.domain.exceptions.UsernameAlreadyExistException;
import com.userservice.domain.service.UserService;
import com.userservice.web.dto.UserDto;
import com.userservice.web.mapper.UserMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Valid;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @GetMapping("{id}")
    public ResponseEntity getUser(@PathVariable Integer id){
        if(!userService.doesUserExistById(id)){
            return new ResponseEntity<>(new NoUserFoundWithGivenId().getMessage(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @GetMapping
    public Page<UserDto> getUsers(@RequestParam(value = "size", required = false, defaultValue = "10") final int size, @RequestParam(value = "page", required = false, defaultValue = "0") final int page){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Users> users = userService.getUsers(pageRequest);
        return users.map(userMapper::toDto);
    }

    @PutMapping("{id}")
    public ResponseEntity putUser(@Valid @RequestBody UserDto dto,@PathVariable Integer id) {
        if(!userService.doesUserExistById(id)){
            return new ResponseEntity<>(new NoUserFoundWithGivenId().getMessage(), HttpStatus.NOT_FOUND);
        }
        Users user = userMapper.toModel(dto);
        if(userService.doesUserExistByUserName(user.getUsername())){
            return new ResponseEntity<>(new UsernameAlreadyExistException().getMessage(), HttpStatus.CONFLICT);
        }
        userService.putUser(user, id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody UserDto dto) {
        Users user = userMapper.toModel(dto);
        try {
            Users userFound = userService.login(user.getUsername());
            return new ResponseEntity<>(userFound, HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody UserDto dto){
        Users user = userMapper.toModel(dto);
        boolean status = userService.register(user);
        return new ResponseEntity<>(status, HttpStatus.OK);
        //true status means succes
        //false status means there is no user found with that username
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id){
        if(!userService.doesUserExistById(id)){
            return new ResponseEntity<>(new NoUserFoundWithGivenId().getMessage(), HttpStatus.NOT_FOUND);
        }
        userService.deleteUser(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
