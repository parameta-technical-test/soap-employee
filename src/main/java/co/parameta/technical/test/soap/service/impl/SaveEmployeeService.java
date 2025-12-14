package co.parameta.technical.test.soap.service.impl;

import co.parameta.technical.test.commons.dto.EmployeeDTO;
import co.parameta.technical.test.commons.dto.SystemParameterDTO;
import co.parameta.technical.test.commons.pojo.*;
import co.parameta.technical.test.commons.repository.SystemParameterRepository;
import co.parameta.technical.test.commons.util.helper.GeneralUtil;
import co.parameta.technical.test.commons.util.mapper.SystemParameterMapper;
import co.parameta.technical.test.soap.repository.EmployeeRepository;
import co.parameta.technical.test.soap.service.ISaveEmployeeService;
import co.parameta.technical.test.soap.util.helper.GeneralSoapUtil;
import co.parameta.technical.test.soap.util.mapper.EmployeeMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import static co.parameta.technical.test.soap.util.constant.Constant.*;
import static co.parameta.technical.test.soap.util.helper.GeneralSoapUtil.buildAdditionalInformation;

@Service
@RequiredArgsConstructor
public class SaveEmployeeService implements ISaveEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final SystemParameterRepository systemParameterRepository;
    private final SystemParameterMapper systemParameterMapper;

    @Override
    @Transactional
    public EmployeeResponsePojo saveEmployee(EmployeePojo employee) {

        EmployeeDTO employeeDTO = employeeMapper.pojoToDto(employee);

        EmployeeResponsePojo response = new EmployeeResponsePojo();
        EmployeeResponseTypePojo responseType = new EmployeeResponseTypePojo();

        boolean employeeExists =
                employeeRepository.searchEmployeeExistence(
                        employeeDTO.getDocumentNumber(),
                        employeeDTO.getTypeDocument().getCode()
                ) == 1;

        if (employeeExists) {

            SystemParameterDTO systemParameter =
                    systemParameterMapper.toDto(
                            systemParameterRepository.findByName("UPDATE_INFORMATION")
                    );

            if ("1".equals(systemParameter.getContent())) {

                Integer employeeId =
                        employeeRepository.searchIdEmployee(
                                employeeDTO.getDocumentNumber(),
                                employeeDTO.getTypeDocument().getCode()
                        );

                employeeRepository.updateInformationEmployee(employeeDTO, employeeId);

                responseType.setStatus(STATUS_SUCCESS);
                responseType.setMessage(EMPLOYEE_UPDATED_SUCCESSFULLY);

            } else {
                responseType.setStatus(STATUS_INTERNAL_ERROR);
                responseType.setMessage(UPDATE_NOT_ALLOWED);
            }

        } else {

            employeeRepository.save(employeeMapper.toEntity(employeeDTO));

            responseType.setStatus(STATUS_SUCCESS);
            responseType.setMessage(EMPLOYEE_SAVED_SUCCESSFULLY);
        }

        if (!STATUS_INTERNAL_ERROR.equals(responseType.getStatus())) {

            Map<String, Integer> timeAtCompany =
                    GeneralUtil.diff(
                            employeeDTO.getDateAffiliationCompany(),
                            new Date()
                    );

            Map<String, Integer> ageEmployee =
                    GeneralUtil.diff(
                            employeeDTO.getDateOfBirth(),
                            new Date()
                    );

            AdditionalEmployeeInformationPojo additionalInfo =
                    buildAdditionalInformation(timeAtCompany, ageEmployee);
            responseType.setAdditionalEmployeeInformation(additionalInfo);
        }
        response.setResponse(responseType);
        return response;
    }


}
