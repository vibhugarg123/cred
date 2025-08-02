package com.cred.demoproject.controllers;

import com.cred.demoproject.domains.CreateUserRequest;
import com.cred.demoproject.domains.CreateUserResponse;
import com.cred.demoproject.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public CreateUserResponse createUser(@RequestBody @Valid CreateUserRequest request) {
    return userService.addUser(request);
  }
}
