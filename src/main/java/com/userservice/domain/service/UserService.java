package com.userservice.domain.service;

import com.userservice.data.entity.Users;
import com.userservice.data.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<Users> getUser(Integer id){
        return userRepository.findById(id);
    }

    public Page<Users> getUsers(Pageable page){
        return userRepository.findAll(page);
    }

    public Users putUser(Users users, Integer id)  {
        Users usersToUpdate = userRepository.getById(id);
        usersToUpdate.setUsername(users.getUsername());
        return userRepository.save(usersToUpdate);
    }

    public boolean doesUserExistByUserName(String username){
        Users user = userRepository.findByUsername(username);
        return user != null;
    }

    public Users login(String username) throws Exception {
        if(!doesUserExistByUserName(username)){
            throw new Exception("No user found with given Username");
        }
        return userRepository.findByUsername(username);
    }

    public boolean register(Users users){
        if(doesUserExistByUserName(users.getUsername())){
            return false;
        }

        userRepository.save(users);
        return true;
    }

    public void deleteUser(Integer id){
        userRepository.deleteById(id);
    }

    public boolean doesUserExistById(Integer id){
        Optional<Users> user = userRepository.findById(id);
        return user.isPresent();
    }
}
