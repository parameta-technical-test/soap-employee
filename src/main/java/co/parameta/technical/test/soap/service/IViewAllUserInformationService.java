package co.parameta.technical.test.soap.service;

import com.parameta.technical.test.soap.gen.GetAllInformationEmployeeRequest;
import com.parameta.technical.test.soap.gen.GetAllInformationEmployeeResponse;

/**
 * SOAP service interface responsible for retrieving complete employee information.
 * <p>
 * This service allows querying employee data using different identification
 * criteria and returns a detailed response including calculated information
 * and optional resources.
 */
public interface IViewAllUserInformationService {

    /**
     * Retrieves all available information for an employee.
     * <p>
     * The search can be performed using the employee ID or a combination
     * of document type and document number.
     *
     * @param request SOAP request containing the search parameters
     * @return SOAP response with the employee information, if found
     */
    GetAllInformationEmployeeResponse allInformationEmployee(GetAllInformationEmployeeRequest request);

}
