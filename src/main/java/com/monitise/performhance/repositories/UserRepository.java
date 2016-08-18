package com.monitise.performhance.repositories;

import com.monitise.performhance.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>, JpaSpecificationExecutor {

    @Override
    List<User> findAll();

    User findByUsername(String username);

    List<User> findByOrganizationId(int organizationId);

    @Query(value = "SELECT id FROM user WHERE team_id = ?1", nativeQuery = true)
    List<Integer> findAllByTeamIdSelectUserId(int teamId);

    @Query(value = "SELECT id FROM user WHERE job_title_id = ?1", nativeQuery = true)
    List<Integer> findAllByJobTitleIdSelectUserId(int jobTitleId);

}