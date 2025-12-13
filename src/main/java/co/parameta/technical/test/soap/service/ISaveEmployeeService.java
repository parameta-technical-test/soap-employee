package co.parameta.technical.test.soap.service;

import co.parameta.technical.test.commons.pojo.EmployeePojo;
import co.parameta.technical.test.commons.pojo.EmployeeResponsePojo;
import com.parameta.technical.test.soap.gen.Employee;
import com.parameta.technical.test.soap.gen.EmployeeResponse;

public interface ISaveEmployeeService {

    EmployeeResponsePojo saveEmployee(EmployeePojo employee);


}
