package com.monitise.performhance.repositories;

import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriteriaRepository extends CrudRepository<Criteria, Integer> {

    List<Criteria> findByOrganizationId(int organizationId);

    Criteria findByOrganizationAndCriteria(Organization organization, String criteria);

}