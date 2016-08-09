package com.monitise.services;

import com.monitise.api.model.BaseException;
import com.monitise.entity.Criteria;
import com.monitise.api.model.ResponseCode;
import com.monitise.repositories.CriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CriteriaService {

    @Autowired
    private CriteriaRepository criteriaRepository;

    public List<Criteria> getAll() {
        List<Criteria> criteriaList = (List<Criteria>) criteriaRepository.findAll();
        return criteriaList;
    }

    public List<Criteria> getAllFilterByOrganizationId(int organizationId) throws BaseException {
        List<Criteria> list = criteriaRepository.findByOrganizationId(organizationId);
        if (list == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not get the criteria list for given organization ID.");
        }
        return list;
    }

    public Criteria get(int id) throws BaseException {
        Criteria criteria = criteriaRepository.findOne(id);
        if (criteria == null) {
            throw new BaseException(ResponseCode.CRITERIA_ID_DOES_NOT_EXIST, "A criteria with given ID does not exist.");
        }
        return criteria;
    }

    public Criteria add(Criteria criteria) throws BaseException {
        validate(criteria);
        checkExistenceInOrganization(criteria);
        Criteria criteriaFromRepo = criteriaRepository.save(criteria);
        if (criteriaFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add the given criteria.");
        }
        return criteriaFromRepo;
    }

    public void remove(int criteriaId) throws BaseException {
        Criteria criteria = criteriaRepository.findOne(criteriaId);
        if (criteria == null) {
            throw new BaseException(ResponseCode.CRITERIA_ID_DOES_NOT_EXIST, "A criteria with given ID does not exist.");
        }
        criteriaRepository.delete(criteriaId);
    }

    public Criteria update(Criteria criteria) throws BaseException {
        validate(criteria);
        ensureExistence(criteria);
        Criteria criteriaFromRepo = criteriaRepository.save(criteria);
        return criteriaFromRepo;
    }

    // region Helper Methods

    // Throws exception if the criteria DOES NOT EXIST.
    private void ensureExistence(Criteria criteria) throws BaseException {
        Criteria criteriaFromRepo = criteriaRepository.findOne(criteria.getId());
        if (criteriaFromRepo == null) {
            throw new BaseException(ResponseCode.CRITERIA_ID_DOES_NOT_EXIST, "A criteria with given ID does not exist.");
        }
    }

    // Throws exception if the criteria DOES EXIST.
    private void checkExistenceInOrganization(Criteria criteria) throws BaseException {
        Criteria criteriaFromRepo = criteriaRepository.findByOrganizationAndCriteria(criteria.getOrganization(), criteria.getCriteria());
        if (criteriaFromRepo != null) {
            throw new BaseException(ResponseCode.CRITERIA_EXISTS_IN_ORGANIZATION, "Given criteria exists in the given organization.");
        }
    }

    private void validate(Criteria criteria) throws BaseException {
        if (criteria.getCriteria() == null || criteria.getCriteria().trim() == "") {
            throw new BaseException(ResponseCode.CRITERIA_EMPTY, "Empty criteria is not allowed.");
        } else if (criteria.getOrganization() == null) {
            throw new BaseException(ResponseCode.CRITERIA_EMPTY_ORGANIZATION, "Criteria must belong to an organization.");
        }
    }

    // endregion

}