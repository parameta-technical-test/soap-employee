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


@Mapper(componentModel = "spring")
public interface EmployeeMapper extends BaseMapper<EmployeeEntity, EmployeeDTO> {

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
