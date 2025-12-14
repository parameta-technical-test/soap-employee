package co.parameta.technical.test.soap.service;

import co.parameta.technical.test.commons.pojo.EmployeePojo;
import co.parameta.technical.test.commons.pojo.EmployeeResponsePojo;

/**
 * SOAP service interface responsible for employee creation and update operations.
 * <p>
 * This service receives employee information from SOAP requests, applies
 * business rules, persists the data when applicable, and returns a structured
 * response indicating the result of the operation.
 */
public interface ISaveEmployeeService {

    /**
     * Saves or updates an employee based on the provided information.
     * <p>
     * If the employee already exists, the update behavior depends on
     * system configuration parameters.
     *
     * @param employee employee information received from the SOAP request
     * @return SOAP response containing the operation status and additional details
     */
    EmployeeResponsePojo saveEmployee(EmployeePojo employee);

}
