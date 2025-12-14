package co.parameta.technical.test.soap.service;

import co.parameta.technical.test.commons.dto.EmployeeDTO;
import co.parameta.technical.test.commons.dto.SystemParameterDTO;
import co.parameta.technical.test.commons.dto.TypeDocumentDTO;
import co.parameta.technical.test.commons.entity.EmployeeEntity;
import co.parameta.technical.test.commons.entity.SystemParameterEntity;
import co.parameta.technical.test.commons.pojo.EmployeePojo;
import co.parameta.technical.test.commons.pojo.EmployeeResponsePojo;
import co.parameta.technical.test.commons.pojo.EmployeeResponseTypePojo;
import co.parameta.technical.test.commons.repository.SystemParameterRepository;
import co.parameta.technical.test.commons.util.mapper.SystemParameterMapper;
import co.parameta.technical.test.soap.repository.EmployeeRepository;
import co.parameta.technical.test.soap.service.impl.SaveEmployeeService;
import co.parameta.technical.test.soap.util.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static co.parameta.technical.test.soap.util.constant.Constant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveEmployeeServiceTest {

    @InjectMocks
    private SaveEmployeeService service;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private SystemParameterRepository systemParameterRepository;

    @Mock
    private SystemParameterMapper systemParameterMapper;

    @Test
    void saveEmployeeWhenNotExistsSavesAndReturnsSuccessWithAdditionalInfo() {
        EmployeePojo employeePojo = new EmployeePojo();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDocumentNumber("123");
        employeeDTO.setDateAffiliationCompany(new Date());
        employeeDTO.setDateOfBirth(new Date());

        TypeDocumentDTO typeDoc = new TypeDocumentDTO();
        typeDoc.setCode("CC");
        employeeDTO.setTypeDocument(typeDoc);

        when(employeeMapper.pojoToDto(employeePojo)).thenReturn(employeeDTO);

        when(employeeRepository.searchEmployeeExistence("123", "CC"))
                .thenReturn(0);

        EmployeeEntity employeeEntity = mock(EmployeeEntity.class);
        when(employeeMapper.toEntity(employeeDTO))
                .thenReturn(employeeEntity);

        EmployeeResponsePojo resp = service.saveEmployee(employeePojo);

        assertNotNull(resp);
        assertNotNull(resp.getResponse());

        EmployeeResponseTypePojo type = resp.getResponse();
        assertEquals(STATUS_SUCCESS, type.getStatus());
        assertEquals(EMPLOYEE_SAVED_SUCCESSFULLY, type.getMessage());

        assertNotNull(
                type.getAdditionalEmployeeInformation(),
                "When status is not internal error, additionalEmployeeInformation must be present"
        );

        verify(employeeRepository, times(1)).save(any());
        verify(employeeRepository, never()).updateInformationEmployee(any(), anyInt());
        verify(systemParameterRepository, never()).findByName("UPDATE_INFORMATION");
    }

    @Test
    void saveEmployeeWhenExistsAndUpdateAllowedUpdatesAndReturnsSuccessWithAdditionalInfo() {
        EmployeePojo employeePojo = new EmployeePojo();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDocumentNumber("123");
        employeeDTO.setDateAffiliationCompany(new Date());
        employeeDTO.setDateOfBirth(new Date());

        TypeDocumentDTO typeDoc = new TypeDocumentDTO();
        typeDoc.setCode("CC");
        employeeDTO.setTypeDocument(typeDoc);

        when(employeeMapper.pojoToDto(employeePojo)).thenReturn(employeeDTO);

        when(employeeRepository.searchEmployeeExistence("123", "CC"))
                .thenReturn(1);

        SystemParameterEntity updateParamEntity = mock(SystemParameterEntity.class);
        when(systemParameterRepository.findByName("UPDATE_INFORMATION"))
                .thenReturn(updateParamEntity);

        SystemParameterDTO spDto = new SystemParameterDTO();
        spDto.setContent("1");
        when(systemParameterMapper.toDto(updateParamEntity))
                .thenReturn(spDto);

        when(employeeRepository.searchIdEmployee("123", "CC"))
                .thenReturn(77);

        EmployeeResponsePojo resp = service.saveEmployee(employeePojo);

        assertNotNull(resp);
        EmployeeResponseTypePojo type = resp.getResponse();
        assertEquals(STATUS_SUCCESS, type.getStatus());
        assertEquals(EMPLOYEE_UPDATED_SUCCESSFULLY, type.getMessage());

        assertNotNull(type.getAdditionalEmployeeInformation());

        verify(employeeRepository, times(1)).searchIdEmployee("123", "CC");
        verify(employeeRepository, times(1)).updateInformationEmployee(eq(employeeDTO), eq(77));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void saveEmployeeWhenExistsAndUpdateNotAllowedReturnsInternalErrorWithoutAdditionalInfo() {
        EmployeePojo employeePojo = new EmployeePojo();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDocumentNumber("123");
        employeeDTO.setDateAffiliationCompany(new Date());
        employeeDTO.setDateOfBirth(new Date());

        TypeDocumentDTO typeDoc = new TypeDocumentDTO();
        typeDoc.setCode("CC");
        employeeDTO.setTypeDocument(typeDoc);

        when(employeeMapper.pojoToDto(employeePojo)).thenReturn(employeeDTO);

        when(employeeRepository.searchEmployeeExistence("123", "CC"))
                .thenReturn(1);

        SystemParameterEntity updateParamEntity = mock(SystemParameterEntity.class);
        when(systemParameterRepository.findByName("UPDATE_INFORMATION"))
                .thenReturn(updateParamEntity);

        SystemParameterDTO spDto = new SystemParameterDTO();
        spDto.setContent("0");
        when(systemParameterMapper.toDto(updateParamEntity))
                .thenReturn(spDto);

        EmployeeResponsePojo resp = service.saveEmployee(employeePojo);

        assertNotNull(resp);
        EmployeeResponseTypePojo type = resp.getResponse();
        assertEquals(STATUS_INTERNAL_ERROR, type.getStatus());
        assertEquals(UPDATE_NOT_ALLOWED, type.getMessage());

        assertNull(
                type.getAdditionalEmployeeInformation(),
                "When status is internal error, additionalEmployeeInformation must be null"
        );

        verify(employeeRepository, never()).updateInformationEmployee(any(), anyInt());
        verify(employeeRepository, never()).save(any());
        verify(employeeRepository, never()).searchIdEmployee(anyString(), anyString());
    }
}
