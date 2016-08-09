package com.monitise.repositories;

import com.monitise.entity.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends CrudRepository<Team, Integer> {

    List<Team> findByOrganizationId(int organizationId);

}
