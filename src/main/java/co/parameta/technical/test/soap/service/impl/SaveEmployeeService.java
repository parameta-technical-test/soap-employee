package co.parameta.technical.test.soap.service.impl;

import co.parameta.technical.test.commons.dto.EmployeeDTO;
import co.parameta.technical.test.commons.pojo.*;
import co.parameta.technical.test.soap.repository.EmployeeRepository;
import co.parameta.technical.test.soap.service.ISaveEmployeeService;
import co.parameta.technical.test.soap.util.helper.GeneralSoapUtil;
import co.parameta.technical.test.soap.util.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import static co.parameta.technical.test.soap.util.constant.Constant.*;


@Service
@RequiredArgsConstructor
public class SaveEmployeeService implements ISaveEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponsePojo saveEmployee(EmployeePojo employee) {

        EmployeeDTO employeeDTO = employeeMapper.pojoToDto(employee);
        employeeRepository.save(employeeMapper.toEntity(employeeDTO));

        Map<String, Integer> timeAtCompany = GeneralSoapUtil.diff(
                employeeDTO.getDateAffiliationCompany(),
                new Date()
        );

        Map<String, Integer> ageEmployee = GeneralSoapUtil.diff(
                employeeDTO.getDateOfBirth(),
                new Date()
        );

        AdditionalEmployeeInformationPojo additionalInfo =
                buildAdditionalInformation(timeAtCompany, ageEmployee);

        EmployeeResponseTypePojo responseType = new EmployeeResponseTypePojo();
        responseType.setStatus(STATUS_SUCCESS);
        responseType.setMessage(EMPLOYEE_SAVED_SUCCESSFULLY);
        responseType.setAdditionalEmployeeInformation(additionalInfo);

        EmployeeResponsePojo response = new EmployeeResponsePojo();
        response.setResponse(responseType);

        return response;
    }

    private AdditionalEmployeeInformationPojo buildAdditionalInformation(
            Map<String, Integer> timeAtCompany,
            Map<String, Integer> ageEmployee
    ) {
        AdditionalEmployeeInformationPojo info = new AdditionalEmployeeInformationPojo();
        info.setTimeLinkedToCompany(toExtraInformation(timeAtCompany));
        info.setCurrentAgeEmployee(toExtraInformation(ageEmployee));
        return info;
    }

    private ExtraInformationPojo toExtraInformation(Map<String, Integer> data) {
        ExtraInformationPojo extra = new ExtraInformationPojo();
        extra.setYears(data.get(YEARS));
        extra.setMonths(data.get(MONTHS));
        extra.setDays(data.get(DAYS));
        return extra;
    }
}
