package co.parameta.technical.test.soap.util.mapper;

import co.parameta.technical.test.commons.dto.AdministratorUserDTO;
import co.parameta.technical.test.commons.dto.EmployeeDTO;
import co.parameta.technical.test.commons.dto.PositionDTO;
import co.parameta.technical.test.commons.dto.TypeDocumentDTO;
import co.parameta.technical.test.commons.entity.EmployeeEntity;
import co.parameta.technical.test.commons.pojo.EmployeePojo;
import co.parameta.technical.test.commons.util.helper.GeneralUtil;
import co.parameta.technical.test.commons.util.mapper.BaseMapper;
import co.parameta.technical.test.soap.util.helper.GeneralSoapUtil;
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

}
