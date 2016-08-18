package com.monitise.performhance.repositories;

import com.monitise.performhance.entity.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, Integer> {

    @Override
    List<Organization> findAll();

    Organization findByName(String name);

}