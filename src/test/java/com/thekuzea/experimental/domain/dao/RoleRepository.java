package com.thekuzea.experimental.domain.dao;

import com.thekuzea.experimental.domain.model.Role;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = Role.class, idClass = UUID.class)
public interface RoleRepository {

    Optional<Role> findRoleByName(String name);
}
