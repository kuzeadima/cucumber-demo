package com.thekuzea.experimental.domain.dao;

import com.thekuzea.experimental.domain.model.User;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = User.class, idClass = UUID.class)
public interface UserRepository {

    Optional<User> findUserByUsername(String username);
}
