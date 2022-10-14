package com.userservice.web.mapper;

import com.userservice.data.entity.Users;
import com.userservice.web.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(Users entity);

    // mapper fails without
    @Mapping(target = "username", source = "username")
    Users toModel(UserDto dto);
}
