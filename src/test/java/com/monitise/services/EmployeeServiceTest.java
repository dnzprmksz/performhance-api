package com.monitise.services;

import com.monitise.AppConfig;
import com.monitise.models.BaseException;
import com.monitise.models.Employee;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void add_uniqueEmployee_shouldAdd() throws BaseException {

        Employee employee = new Employee("Deniz", "Parmaksız");
        Employee employeeFromService = null;
        employeeFromService = employeeService.add(employee);

        Assert.assertNotNull(employeeFromService);
        Assert.assertEquals(employee.getName(), employeeFromService.getName());
        Assert.assertEquals(employee.getSurname(), employeeFromService.getSurname());
    }

}
