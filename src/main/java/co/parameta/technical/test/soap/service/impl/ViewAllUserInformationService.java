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

/**
 * SOAP service responsible for retrieving complete employee information.
 * <p>
 * This service allows querying employee data either by employee ID or
 * by document type and number. It enriches the response with calculated
 * information such as employee age, time linked to the company, and
 * optionally includes a PDF report retrieved from Amazon S3.
 */
@Service
@RequiredArgsConstructor
public class ViewAllUserInformationService implements IViewAllUserInformationService {

    /**
     * Repository used to retrieve employee information from the database.
     */
    private final EmployeeRepository employeeRepository;

    /**
     * Mapper used to convert between Employee entities, DTOs, and SOAP POJOs.
     */
    private final EmployeeMapper employeeMapper;

    /**
     * Mapper used to convert type document information to SOAP entities.
     */
    private final TypeDocumentMapper typeDocumentMapper;

    /**
     * Mapper used to convert position information to SOAP entities.
     */
    private final PositionMapper positionMapper;

    /**
     * Mapper used to convert administrator user information to SOAP entities.
     */
    private final AdministratorUserPojoMapper administratorUserPojoMapper;

    /**
     * Repository used to retrieve system configuration parameters.
     */
    private final SystemParameterRepository systemParameterRepository;

    /**
     * Service used to retrieve employee PDF reports from Amazon S3.
     */
    private final IGetPdfS3Service iGetPdfS3Service;

    /**
     * Retrieves all available information for a given employee.
     * <p>
     * The search can be performed using the employee ID or a combination
     * of document type and document number. Additional information such as
     * current age and time linked to the company is calculated dynamically.
     * The employee PDF report is included only if enabled by system parameters.
     *
     * @param request SOAP request containing employee identification criteria
     * @return SOAP response with full employee information, or empty if not found
     */
    @Override
    @Transactional(readOnly = true)
    public GetAllInformationEmployeeResponse allInformationEmployee(GetAllInformationEmployeeRequest request) {

        if (GeneralSoapUtil.isNullOrBlank(request.getIdEmployee())) {
            request.setIdEmployee(null);
        }
        if (GeneralSoapUtil.isNullOrBlank(request.getTypeDocument())) {
            request.setTypeDocument(null);
        }
        if (GeneralSoapUtil.isNullOrBlank(request.getNumberDocument())) {
            request.setNumberDocument(null);
        }

        boolean hasId = !GeneralSoapUtil.isNullOrBlank(request.getIdEmployee());
        boolean hasDocumentDate =
                !GeneralSoapUtil.isNullOrBlank(request.getTypeDocument())
                        && !GeneralSoapUtil.isNullOrBlank(request.getNumberDocument());

        boolean viewPdf =
                systemParameterRepository.findByName("GET_PDF_EMPLOYEE")
                        .getContent()
                        .equals("1");

        AllInformationEmployee response = null;
        GetAllInformationEmployeeResponse getAllInformationEmployeeResponse =
                new GetAllInformationEmployeeResponse();

        if (hasId || hasDocumentDate) {

            EmployeeDTO employeeDTO =
                    employeeMapper.toDto(
                            employeeRepository.searchAllInformationEmployee(
                                    request.getIdEmployee(),
                                    request.getTypeDocument(),
                                    request.getNumberDocument()
                            )
                    );

            EmployeePojo employeeInformation =
                    employeeMapper.dtoToPojo(employeeDTO);

            response =
                    employeeMapper.employeeDTOToAllInformationEmployeePojo(
                            employeeInformation,
                            GeneralSoapUtil.toExtraInformation(
                                    GeneralUtil.diff(
                                            employeeDTO.getDateAffiliationCompany(),
                                            new Date()
                                    )
                            ),
                            GeneralSoapUtil.toExtraInformation(
                                    GeneralUtil.diff(
                                            employeeDTO.getDateOfBirth(),
                                            new Date()
                                    )
                            ),
                            viewPdf && !GeneralSoapUtil.isNullOrBlank(employeeDTO.getStorageLocationReport())
                                    ? iGetPdfS3Service.getPdf(
                                    employeeDTO.getStorageLocationReport()
                            )
                                    : null,
                            typeDocumentMapper.toEntity(
                                    employeeInformation.getTypeDocument()
                            ),
                            positionMapper.toEntity(
                                    employeeInformation.getPosition()
                            ),
                            administratorUserPojoMapper.toEntity(
                                    employeeInformation.getAdministratorUser()
                            )
                    );
        }

        getAllInformationEmployeeResponse.setData(response);
        return getAllInformationEmployeeResponse;
    }
}
