package co.parameta.technical.test.soap.service;

import co.parameta.technical.test.commons.dto.EmployeeDTO;
import co.parameta.technical.test.commons.entity.*;
import co.parameta.technical.test.commons.pojo.AdministratorUserPojo;
import co.parameta.technical.test.commons.pojo.EmployeePojo;
import co.parameta.technical.test.commons.pojo.PositionPojo;
import co.parameta.technical.test.commons.pojo.TypeDocumentPojo;
import co.parameta.technical.test.commons.repository.SystemParameterRepository;
import co.parameta.technical.test.soap.repository.EmployeeRepository;
import co.parameta.technical.test.soap.service.impl.ViewAllUserInformationService;
import co.parameta.technical.test.soap.util.mapper.AdministratorUserPojoMapper;
import co.parameta.technical.test.soap.util.mapper.EmployeeMapper;
import co.parameta.technical.test.soap.util.mapper.PositionMapper;
import co.parameta.technical.test.soap.util.mapper.TypeDocumentMapper;
import com.parameta.technical.test.soap.gen.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewAllUserInformationServiceTest {

    @InjectMocks
    private ViewAllUserInformationService service;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private TypeDocumentMapper typeDocumentMapper;

    @Mock
    private PositionMapper positionMapper;

    @Mock
    private AdministratorUserPojoMapper administratorUserPojoMapper;

    @Mock
    private SystemParameterRepository systemParameterRepository;

    @Mock
    private IGetPdfS3Service pdfS3Service;

    private SystemParameterEntity buildPdfParam(String content) {
        SystemParameterEntity p = new SystemParameterEntity();
        p.setId(1);
        p.setName("GET_PDF_EMPLOYEE");
        p.setContent(content);
        p.setCreator("SYSTEM");
        p.setDateCreate(new Date());
        return p;
    }

    @BeforeEach
    void setUpDefaultParameters() {
        when(systemParameterRepository.findByName("GET_PDF_EMPLOYEE"))
                .thenReturn(buildPdfParam("1"));
    }

    @Test
    void getAllInformationEmptyFieldsBecomeNullAndWithoutCriteriaDoesNotQueryRepository() {
        GetAllInformationEmployeeRequest req = new GetAllInformationEmployeeRequest();
        req.setIdEmployee(0);
        req.setTypeDocument("");
        req.setNumberDocument(" ");

        GetAllInformationEmployeeResponse resp = service.allInformationEmployee(req);

        assertNotNull(resp);
        assertNull(resp.getData(), "Without id or document it should return null data");

        verify(employeeRepository, never())
                .searchAllInformationEmployee(any(), any(), any());
        verify(employeeMapper, never()).toDto(any());
        verify(pdfS3Service, never()).getPdf(anyString());
    }

    @Test
    void getAllInformationByIdWithPdfEnabledFetchesMapsAndLoadsPdf() {
        GetAllInformationEmployeeRequest req = new GetAllInformationEmployeeRequest();
        req.setIdEmployee(10);
        req.setTypeDocument(null);
        req.setNumberDocument(null);

        EmployeeEntity entity = new EmployeeEntity();
        when(employeeRepository.searchAllInformationEmployee(eq(10), isNull(), isNull()))
                .thenReturn(entity);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDateOfBirth(new Date());
        employeeDTO.setDateAffiliationCompany(new Date());
        employeeDTO.setStorageLocationReport("pdf/key.pdf");

        when(employeeMapper.toDto(entity)).thenReturn(employeeDTO);

        EmployeePojo employeePojo = new EmployeePojo();
        employeePojo.setTypeDocument(new TypeDocumentPojo());
        employeePojo.setPosition(new PositionPojo());
        employeePojo.setAdministratorUser(new AdministratorUserPojo());

        when(employeeMapper.dtoToPojo(employeeDTO)).thenReturn(employeePojo);

        TypeDocument typeDocEntity = new TypeDocument();
        Position posEntity = new Position();
        AdministratorUser adminEntity = new AdministratorUser();

        when(typeDocumentMapper.toEntity(any(TypeDocumentPojo.class))).thenReturn(typeDocEntity);
        when(positionMapper.toEntity(any(PositionPojo.class))).thenReturn(posEntity);
        when(administratorUserPojoMapper.toEntity(any(AdministratorUserPojo.class))).thenReturn(adminEntity);

        byte[] pdf = "PDF".getBytes();
        when(pdfS3Service.getPdf("pdf/key.pdf")).thenReturn(pdf);

        AllInformationEmployee expectedAllInfo = new AllInformationEmployee();
        when(employeeMapper.employeeDTOToAllInformationEmployeePojo(
                eq(employeePojo),
                any(),
                any(),
                eq(pdf),
                eq(typeDocEntity),
                eq(posEntity),
                eq(adminEntity)
        )).thenReturn(expectedAllInfo);

        GetAllInformationEmployeeResponse resp = service.allInformationEmployee(req);

        assertNotNull(resp);
        assertSame(expectedAllInfo, resp.getData());

        verify(employeeRepository, times(1))
                .searchAllInformationEmployee(10, null, null);
        verify(pdfS3Service, times(1))
                .getPdf("pdf/key.pdf");
        verify(typeDocumentMapper, times(1))
                .toEntity(any(TypeDocumentPojo.class));
        verify(positionMapper, times(1))
                .toEntity(any(PositionPojo.class));
        verify(administratorUserPojoMapper, times(1))
                .toEntity(any(AdministratorUserPojo.class));
    }

    @Test
    void getAllInformationByDocumentWithPdfEnabledFetchesMapsAndLoadsPdf() {
        GetAllInformationEmployeeRequest req = new GetAllInformationEmployeeRequest();
        req.setIdEmployee(null);
        req.setTypeDocument("CC");
        req.setNumberDocument("123");

        EmployeeEntity entity = new EmployeeEntity();
        when(employeeRepository.searchAllInformationEmployee(isNull(), eq("CC"), eq("123")))
                .thenReturn(entity);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDateOfBirth(new Date());
        employeeDTO.setDateAffiliationCompany(new Date());
        employeeDTO.setStorageLocationReport("pdf/doc.pdf");

        when(employeeMapper.toDto(entity)).thenReturn(employeeDTO);

        EmployeePojo employeePojo = new EmployeePojo();
        employeePojo.setTypeDocument(new TypeDocumentPojo());
        employeePojo.setPosition(new PositionPojo());
        employeePojo.setAdministratorUser(new AdministratorUserPojo());
        when(employeeMapper.dtoToPojo(employeeDTO)).thenReturn(employeePojo);

        TypeDocument typeDocEntity = new TypeDocument();
        Position posEntity = new Position();
        AdministratorUser adminEntity = new AdministratorUser();
        when(typeDocumentMapper.toEntity(any(TypeDocumentPojo.class))).thenReturn(typeDocEntity);
        when(positionMapper.toEntity(any(PositionPojo.class))).thenReturn(posEntity);
        when(administratorUserPojoMapper.toEntity(any(AdministratorUserPojo.class))).thenReturn(adminEntity);

        byte[] pdf = "PDF2".getBytes();
        when(pdfS3Service.getPdf("pdf/doc.pdf")).thenReturn(pdf);

        AllInformationEmployee expectedAllInfo = new AllInformationEmployee();
        when(employeeMapper.employeeDTOToAllInformationEmployeePojo(
                eq(employeePojo),
                any(),
                any(),
                eq(pdf),
                eq(typeDocEntity),
                eq(posEntity),
                eq(adminEntity)
        )).thenReturn(expectedAllInfo);

        GetAllInformationEmployeeResponse resp = service.allInformationEmployee(req);

        assertNotNull(resp);
        assertSame(expectedAllInfo, resp.getData());

        verify(employeeRepository, times(1))
                .searchAllInformationEmployee(null, "CC", "123");
        verify(pdfS3Service, times(1))
                .getPdf("pdf/doc.pdf");
    }

    @Test
    void getAllInformationWithPdfDisabledDoesNotCallS3AndPdfIsNull() {
        when(systemParameterRepository.findByName("GET_PDF_EMPLOYEE"))
                .thenReturn(buildPdfParam("0"));

        GetAllInformationEmployeeRequest req = new GetAllInformationEmployeeRequest();
        req.setIdEmployee(1);

        EmployeeEntity entity = new EmployeeEntity();
        when(employeeRepository.searchAllInformationEmployee(eq(1), isNull(), isNull()))
                .thenReturn(entity);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDateOfBirth(new Date());
        employeeDTO.setDateAffiliationCompany(new Date());
        employeeDTO.setStorageLocationReport("pdf/should-not-call.pdf");

        when(employeeMapper.toDto(entity)).thenReturn(employeeDTO);

        EmployeePojo employeePojo = new EmployeePojo();
        employeePojo.setTypeDocument(new TypeDocumentPojo());
        employeePojo.setPosition(new PositionPojo());
        employeePojo.setAdministratorUser(new AdministratorUserPojo());
        when(employeeMapper.dtoToPojo(employeeDTO)).thenReturn(employeePojo);

        TypeDocument typeDocEntity = new TypeDocument();
        Position posEntity = new Position();
        AdministratorUser adminEntity = new AdministratorUser();
        when(typeDocumentMapper.toEntity(any(TypeDocumentPojo.class))).thenReturn(typeDocEntity);
        when(positionMapper.toEntity(any(PositionPojo.class))).thenReturn(posEntity);
        when(administratorUserPojoMapper.toEntity(any(AdministratorUserPojo.class))).thenReturn(adminEntity);

        AllInformationEmployee expectedAllInfo = new AllInformationEmployee();
        when(employeeMapper.employeeDTOToAllInformationEmployeePojo(
                eq(employeePojo),
                any(),
                any(),
                isNull(),
                eq(typeDocEntity),
                eq(posEntity),
                eq(adminEntity)
        )).thenReturn(expectedAllInfo);

        GetAllInformationEmployeeResponse resp = service.allInformationEmployee(req);

        assertNotNull(resp);
        assertSame(expectedAllInfo, resp.getData());

        verify(pdfS3Service, never()).getPdf(anyString());
    }
}
