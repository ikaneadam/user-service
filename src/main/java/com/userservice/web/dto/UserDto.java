package com.userservice.web.dto;

import javax.validation.constraints.*;

public class UserDto {
    private Integer id;
    @NotNull
    @Size(min = 3, max = 255)
    private String username;

    public UserDto() {
    }

    public UserDto(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
