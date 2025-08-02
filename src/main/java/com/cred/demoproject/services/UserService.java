package com.cred.demoproject.services;

import com.cred.demoproject.domains.CreateUserRequest;
import com.cred.demoproject.domains.CreateUserResponse;

public interface UserService {
  CreateUserResponse addUser(CreateUserRequest request);
}
