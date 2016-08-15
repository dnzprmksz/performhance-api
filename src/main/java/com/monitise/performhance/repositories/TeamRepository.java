package com.monitise.performhance.repositories;

import com.monitise.performhance.entity.Team;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends CrudRepository<Team, Integer>, JpaSpecificationExecutor {

    List<Team> findByOrganizationId(int organizationId);

}
