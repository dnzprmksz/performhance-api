package com.monitise.services;

import com.monitise.models.BaseException;
import com.monitise.models.Employee;
import com.monitise.models.ResponseCode;
import com.monitise.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAll() {

        List<Employee> list = (List<Employee>) employeeRepository.findAll();
        return list;
    }

    public Employee get(int id) throws BaseException {

        Employee employee = employeeRepository.findOne(id);

        if (employee == null) {
            throw new BaseException(ResponseCode.EMPLOYEE_ID_DOES_NOT_EXIST, "An employee with given ID does not exist.");
        }

        return employee;
    }

    public Employee add(Employee employee) throws BaseException {

        Employee employeeFromRepo = employeeRepository.save(employee);

        if (employeeFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given Employee.");
        }

        return employeeFromRepo;
    }

}