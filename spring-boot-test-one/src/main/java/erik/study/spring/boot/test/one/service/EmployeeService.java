package erik.study.spring.boot.test.one.service;

import erik.study.spring.boot.test.one.entity.Employee;

import java.util.List;

/**
 * @author erik.wang
 * @date 2020-02-28 17:06
 * @description
 */
public interface EmployeeService {
    Employee getEmployeeByName(String name);

    List<Employee> getAllEmployees();
}
