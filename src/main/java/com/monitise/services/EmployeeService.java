package com.monitise.services;

import com.monitise.models.BaseException;
import com.monitise.models.Employee;
import com.monitise.models.ResponseCode;
import com.monitise.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Iterable<Employee> getAll() {

        // Find all employees in repository.
        Iterable<Employee> list = employeeRepository.findAll();
        return list;
    }

    public Employee get(int id) throws BaseException {

        // Find the employee with given ID in repository.
        Employee employee = employeeRepository.findOne(id);

        // Check the success of the action and throw an exception if the action fails.
        if (employee == null) {
            throw new BaseException(ResponseCode.EMPLOYEE_ID_DOES_NOT_EXIST, "An employee with given ID does not exist.");
        }

        return employee;
    }

    public Employee add(Employee employee) throws BaseException {

        // Add given employee to repository.
        Employee employeeFromRepo = employeeRepository.save(employee);

        // Check the success of the action and throw an exception if the action fails.
        if (employeeFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given Employee.");
        }

        return employeeFromRepo;
    }

}