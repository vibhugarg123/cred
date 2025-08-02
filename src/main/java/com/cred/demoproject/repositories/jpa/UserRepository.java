package com.cred.demoproject.repositories.jpa;

import com.cred.demoproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  // Add custom query methods if needed
}
