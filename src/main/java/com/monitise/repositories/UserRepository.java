package com.monitise.repositories;

import com.monitise.models.Organization;
import com.monitise.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

    @Query(value = "select * from user where organization_id = ?1 ", nativeQuery = true )
    List<User> findByOrganizationId(int organizationId);
}