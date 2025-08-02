package com.cred.demoproject.services.impl;

import com.cred.demoproject.domains.CreateUserRequest;
import com.cred.demoproject.domains.CreateUserResponse;
import com.cred.demoproject.entities.User;
import com.cred.demoproject.mappers.UserMapper;
import com.cred.demoproject.repositories.jpa.UserRepository;
import com.cred.demoproject.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;
  private UserMapper userMapper;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public CreateUserResponse addUser(CreateUserRequest request) {
    User user = userMapper.toUser(request);
    Long userId = userRepository.save(user).getId();
    return new CreateUserResponse(userId);
  }
}
