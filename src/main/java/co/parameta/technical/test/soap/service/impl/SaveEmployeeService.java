package co.parameta.technical.test.soap.service.impl;

import co.parameta.technical.test.commons.dto.EmployeeDTO;
import co.parameta.technical.test.commons.pojo.*;
import co.parameta.technical.test.soap.repository.EmployeeRepository;
import co.parameta.technical.test.soap.service.ISaveEmployeeService;
import co.parameta.technical.test.soap.util.GeneralSoapUtil;
import co.parameta.technical.test.soap.util.mapper.EmployeeMapper;
import io.swagger.v3.core.util.ReferenceTypeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SaveEmployeeService implements ISaveEmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponsePojo saveEmployee(EmployeePojo employee) {
        EmployeeDTO employeeDTO = employeeMapper.pojoToDto(employee);
        employeeRepository.save(employeeMapper.toEntity(employeeDTO));
        Map<String, Integer> timeAtTheCompany = GeneralSoapUtil.diff(
                employeeDTO.getDateAffiliationCompany(),
                new Date()
        );
        Map<String, Integer> timeOfBirth = GeneralSoapUtil.diff(
                employeeDTO.getDateOfBirth(),
                new Date()
        );
        EmployeeResponsePojo employeeResponse = new EmployeeResponsePojo();
        EmployeeResponseTypePojo employeeResponseType = new EmployeeResponseTypePojo();
        AdditionalEmployeeInformationPojo additionalEmployeeInformation = new AdditionalEmployeeInformationPojo();
        additionalEmployeeInformation.setTimeLinkedToCompany(orgExtInf(timeAtTheCompany));
        additionalEmployeeInformation.setCurrentAgeEmployee(orgExtInf(timeOfBirth));
        employeeResponseType.setAdditionalEmployeeInformation(additionalEmployeeInformation);
        employeeResponseType.setStatus("200");
        employeeResponseType.setStatus("Se guardo correctamente el empleado");
        employeeResponse.setResponse(employeeResponseType);
        return employeeResponse;
    }

    private ExtraInformationPojo orgExtInf(Map<String, Integer> timeAtTheCompany){
        ExtraInformationPojo extInfTimeComp= new ExtraInformationPojo();
        extInfTimeComp.setYears(timeAtTheCompany.get("years"));
        extInfTimeComp.setMonths(timeAtTheCompany.get("months"));
        extInfTimeComp.setDays(timeAtTheCompany.get("days"));
        return extInfTimeComp;
    }
}
