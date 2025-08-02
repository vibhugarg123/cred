package com.cred.demoproject.mappers;

import com.cred.demoproject.domains.CreateUserRequest;
import com.cred.demoproject.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User toUser(CreateUserRequest request);
}
