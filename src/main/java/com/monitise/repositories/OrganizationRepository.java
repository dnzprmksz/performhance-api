package com.monitise.repositories;

import com.monitise.models.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface OrganizationRepository extends CrudRepository<Organization, Integer> {

}