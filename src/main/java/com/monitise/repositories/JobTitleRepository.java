package com.monitise.repositories;

import com.monitise.models.JobTitle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobTitleRepository extends CrudRepository<JobTitle, Integer> {

}
