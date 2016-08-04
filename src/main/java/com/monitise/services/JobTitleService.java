package com.monitise.services;

import com.monitise.models.BaseException;
import com.monitise.models.JobTitle;
import com.monitise.models.ResponseCode;
import com.monitise.repositories.JobTitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobTitleService {

    @Autowired
    private JobTitleRepository jobTitleRepository;

    public JobTitle add(JobTitle jobTitle) throws BaseException {

        JobTitle jobTitleFromRepo = jobTitleRepository.save(jobTitle);

        if (jobTitleFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given job title.");
        }

        return jobTitleFromRepo;
    }

}