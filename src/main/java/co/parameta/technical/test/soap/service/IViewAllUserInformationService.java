package co.parameta.technical.test.soap.service;

import com.parameta.technical.test.soap.gen.GetAllInformationEmployeeRequest;
import com.parameta.technical.test.soap.gen.GetAllInformationEmployeeResponse;

public interface IViewAllUserInformationService {

    GetAllInformationEmployeeResponse allInformationEmployee(GetAllInformationEmployeeRequest request);

}
