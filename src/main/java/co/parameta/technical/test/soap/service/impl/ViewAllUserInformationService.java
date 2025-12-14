package co.parameta.technical.test.soap.service.impl;

import co.parameta.technical.test.commons.dto.EmployeeDTO;
import co.parameta.technical.test.commons.pojo.EmployeePojo;
import co.parameta.technical.test.commons.repository.SystemParameterRepository;
import co.parameta.technical.test.commons.util.helper.GeneralUtil;
import co.parameta.technical.test.soap.repository.EmployeeRepository;
import co.parameta.technical.test.soap.service.IGetPdfS3Service;
import co.parameta.technical.test.soap.service.IViewAllUserInformationService;
import co.parameta.technical.test.soap.util.helper.GeneralSoapUtil;
import co.parameta.technical.test.soap.util.mapper.AdministratorUserPojoMapper;
import co.parameta.technical.test.soap.util.mapper.EmployeeMapper;
import co.parameta.technical.test.soap.util.mapper.PositionMapper;
import co.parameta.technical.test.soap.util.mapper.TypeDocumentMapper;
import com.parameta.technical.test.soap.gen.AllInformationEmployee;
import com.parameta.technical.test.soap.gen.GetAllInformationEmployeeRequest;
import com.parameta.technical.test.soap.gen.GetAllInformationEmployeeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ViewAllUserInformationService implements IViewAllUserInformationService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final TypeDocumentMapper typeDocumentMapper;
    private final PositionMapper positionMapper;
    private final AdministratorUserPojoMapper administratorUserPojoMapper;
    private final SystemParameterRepository systemParameterRepository;
    private final IGetPdfS3Service iGetPdfS3Service;

    @Override
    @Transactional(readOnly = true)
    public GetAllInformationEmployeeResponse allInformationEmployee(GetAllInformationEmployeeRequest request) {
        if(GeneralSoapUtil.isNullOrBlank(request.getIdEmployee())){
            request.setIdEmployee(null);
        }
        if(GeneralSoapUtil.isNullOrBlank(request.getTypeDocument())){
            request.setTypeDocument(null);
        }
        if(GeneralSoapUtil.isNullOrBlank(request.getNumberDocument())){
            request.setNumberDocument(null);
        }
        boolean hasId = !GeneralSoapUtil.isNullOrBlank(request.getIdEmployee());
        boolean hasDocumentDate = !GeneralSoapUtil.isNullOrBlank(request.getTypeDocument()) && !GeneralSoapUtil.isNullOrBlank(request.getNumberDocument());
        boolean viewPdf = systemParameterRepository.findByName("GET_PDF_EMPLOYEE").getContent().equals("1");
        AllInformationEmployee response = null;
        GetAllInformationEmployeeResponse getAllInformationEmployeeResponse = new GetAllInformationEmployeeResponse();
        if(hasId || hasDocumentDate){
            EmployeeDTO employeeDTO = employeeMapper.toDto(employeeRepository.searchAllInformationEmployee(request.getIdEmployee(), request.getTypeDocument(), request.getNumberDocument()));
            EmployeePojo employeeInformation = employeeMapper.dtoToPojo(employeeDTO);
            response = employeeMapper.employeeDTOToAllInformationEmployeePojo(employeeInformation,
                    GeneralSoapUtil.toExtraInformation(GeneralUtil.diff(employeeDTO.getDateAffiliationCompany(), new Date())),
                    GeneralSoapUtil.toExtraInformation(GeneralUtil.diff(employeeDTO.getDateOfBirth(), new Date())),
                    viewPdf ? iGetPdfS3Service.getPdf(employeeDTO.getStorageLocationReport()) : null,
                    typeDocumentMapper.toEntity(employeeInformation.getTypeDocument()),
                    positionMapper.toEntity(employeeInformation.getPosition()),
                    administratorUserPojoMapper.toEntity(employeeInformation.getAdministratorUser())
            );
        }
        getAllInformationEmployeeResponse.setData(response);
        return getAllInformationEmployeeResponse;
    }
}
