package erik.study.spring.boot.test.one.controller;

import erik.study.spring.boot.test.one.entity.Employee;
import erik.study.spring.boot.test.one.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {
 
    @Autowired
    private EmployeeService employeeService;
 
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
}