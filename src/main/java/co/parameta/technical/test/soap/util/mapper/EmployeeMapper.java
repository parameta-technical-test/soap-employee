package co.parameta.technical.test.soap.util.mapper;

import co.parameta.technical.test.commons.dto.AdministratorUserDTO;
import co.parameta.technical.test.commons.dto.EmployeeDTO;
import co.parameta.technical.test.commons.dto.PositionDTO;
import co.parameta.technical.test.commons.dto.TypeDocumentDTO;
import co.parameta.technical.test.commons.entity.EmployeeEntity;
import co.parameta.technical.test.commons.pojo.AdministratorUserPojo;
import co.parameta.technical.test.commons.pojo.EmployeePojo;
import co.parameta.technical.test.commons.pojo.PositionPojo;
import co.parameta.technical.test.commons.pojo.TypeDocumentPojo;
import co.parameta.technical.test.commons.util.helper.GeneralUtil;
import co.parameta.technical.test.commons.util.mapper.BaseMapper;
import co.parameta.technical.test.soap.util.helper.GeneralSoapUtil;
import com.parameta.technical.test.soap.gen.*;
import org.mapstruct.Mapper;

/**
 * Mapper responsible for converting employee data between
 * SOAP-generated objects, internal POJOs, DTOs, and entities.
 * <p>
 * This mapper acts as a bridge between the SOAP contract layer
 * and the internal domain model, handling:
 * <ul>
 *   <li>SOAP {@link EmployeePojo} to internal {@link EmployeeDTO}</li>
 *   <li>Internal {@link EmployeeDTO} to SOAP {@link EmployeePojo}</li>
 *   <li>Construction of {@link AllInformationEmployee} SOAP responses</li>
 * </ul>
 *
 * <p>
 * It also performs safe conversions for dates, nested objects,
 * and optional values using shared utility classes.
 * </p>
 *
 * <p>Base entity-to-DTO mappings are provided by {@link BaseMapper},
 * while custom logic is implemented through default methods.</p>
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends BaseMapper<EmployeeEntity, EmployeeDTO> {

    /**
     * Converts a SOAP {@link EmployeePojo} into an internal {@link EmployeeDTO}.
     * <p>
     * This method extracts nested SOAP elements and safely maps them
     * into DTOs used by the business layer.
     *
     * @param employeePojo SOAP employee object
     * @return mapped {@link EmployeeDTO}
     */
    default EmployeeDTO pojoToDto(EmployeePojo employeePojo){
        EmployeeDTO employee = new EmployeeDTO();
        employee.setNames(GeneralUtil.get(employeePojo::getNames, ""));
        employee.setLastNames(GeneralUtil.get(employeePojo::getLastNames, ""));

        TypeDocumentDTO typeDocument = new TypeDocumentDTO();
        typeDocument.setCode(GeneralUtil.get(() -> employeePojo.getTypeDocument().getCode(), ""));
        employee.setTypeDocument(typeDocument);

        employee.setDocumentNumber(GeneralUtil.get(employeePojo::getDocumentNumber, ""));

        employee.setDateOfBirth(
                GeneralUtil.get(() -> GeneralSoapUtil.toDate(employeePojo.getDateOfBirth()), null)
        );

        employee.setDateAffiliationCompany(
                GeneralUtil.get(() -> GeneralSoapUtil.toDate(employeePojo.getDateAffiliationCompany()), null)
        );

        PositionDTO position = new PositionDTO();
        position.setCode(GeneralUtil.get(() -> employeePojo.getPosition().getCode(), ""));
        employee.setPosition(position);

        employee.setSalary(GeneralUtil.get(employeePojo::getSalary, null));

        AdministratorUserDTO administratorUser = new AdministratorUserDTO();
        administratorUser.setCode(GeneralUtil.get(() -> employeePojo.getAdministratorUser().getCode(), ""));
        employee.setAdministratorUser(administratorUser);

        return employee;
    }

    /**
     * Converts an internal {@link EmployeeDTO} into a SOAP {@link EmployeePojo}.
     * <p>
     * Date values are converted into {@link javax.xml.datatype.XMLGregorianCalendar}
     * to comply with SOAP schema requirements.
     *
     * @param dto internal employee DTO
     * @return SOAP employee POJO
     */
    default EmployeePojo dtoToPojo(EmployeeDTO dto) {

        EmployeePojo employeePojo = new EmployeePojo();

        employeePojo.setId(dto.getId());
        employeePojo.setNames(dto.getNames());
        employeePojo.setLastNames(dto.getLastNames());
        employeePojo.setDocumentNumber(dto.getDocumentNumber());
        employeePojo.setSalary(dto.getSalary());

        employeePojo.setDateOfBirth(
                GeneralUtil.get(() -> GeneralSoapUtil.toXMLGregorianCalendar(dto.getDateOfBirth()), null)
        );
        employeePojo.setDateAffiliationCompany(
                GeneralUtil.get(() -> GeneralSoapUtil.toXMLGregorianCalendar(dto.getDateAffiliationCompany()), null)
        );
        employeePojo.setDateCreate(
                GeneralUtil.get(() -> GeneralSoapUtil.toXMLGregorianCalendar(dto.getDateCreate()), null)
        );
        employeePojo.setDateUpdate(
                GeneralUtil.get(() -> GeneralSoapUtil.toXMLGregorianCalendar(dto.getDateUpdate()), null)
        );

        TypeDocumentPojo typeDocumentPojo = new TypeDocumentPojo();
        typeDocumentPojo.setCode(dto.getTypeDocument().getCode());
        typeDocumentPojo.setDescription(dto.getTypeDocument().getDescription());
        employeePojo.setTypeDocument(typeDocumentPojo);

        PositionPojo positionPojo = new PositionPojo();
        positionPojo.setCode(dto.getPosition().getCode());
        positionPojo.setDescription(dto.getPosition().getDescription());
        employeePojo.setPosition(positionPojo);

        AdministratorUserPojo administratorUserPojo = new AdministratorUserPojo();
        administratorUserPojo.setCode(dto.getAdministratorUser().getCode());
        employeePojo.setAdministratorUser(administratorUserPojo);

        return employeePojo;
    }

    /**
     * Builds a complete {@link AllInformationEmployee} SOAP response
     * combining employee data, calculated time information, and
     * optional PDF content.
     *
     * @param employeePojo employee base information
     * @param timeAtCompany calculated time linked to company
     * @param ageEmployee calculated employee age
     * @param fielPdf optional employee PDF document
     * @param typeDocument SOAP type document element
     * @param position SOAP position element
     * @param administratorUser SOAP administrator user element
     * @return fully populated {@link AllInformationEmployee}
     */
    default AllInformationEmployee employeeDTOToAllInformationEmployeePojo(
            EmployeePojo employeePojo,
            ExtraInformation timeAtCompany,
            ExtraInformation ageEmployee,
            byte[] fielPdf,
            TypeDocument typeDocument,
            Position position,
            AdministratorUser administratorUser
    ) {

        AllInformationEmployee allInformationEmployee = new AllInformationEmployee();

        Employee employee = new Employee();
        employee.setId(employeePojo.getId());
        employee.setNames(employeePojo.getNames());
        employee.setLastNames(employeePojo.getLastNames());
        employee.setTypeDocument(typeDocument);
        employee.setDocumentNumber(employeePojo.getDocumentNumber());
        employee.setDateOfBirth(employeePojo.getDateOfBirth());
        employee.setDateAffiliationCompany(employeePojo.getDateAffiliationCompany());
        employee.setPosition(position);
        employee.setSalary(employeePojo.getSalary());

        if (employee.getAdministratorUser() != null) {
            employee.getAdministratorUser().setCode(null);
            employee.setAdministratorUser(administratorUser);
        }

        employee.setDateCreate(employeePojo.getDateCreate());
        employee.setDateUpdate(employeePojo.getDateUpdate());

        allInformationEmployee.setEmployee(employee);
        allInformationEmployee.setTimeLinkedToCompany(timeAtCompany);
        allInformationEmployee.setCurrentAgeEmployee(ageEmployee);
        allInformationEmployee.setInformativePdf(fielPdf);

        return allInformationEmployee;
    }

}
