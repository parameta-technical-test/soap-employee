package co.parameta.technical.test.soap.util.mapper;

import co.parameta.technical.test.commons.dto.AdministratorUserDTO;
import co.parameta.technical.test.commons.dto.EmployeeDTO;
import co.parameta.technical.test.commons.dto.PositionDTO;
import co.parameta.technical.test.commons.dto.TypeDocumentDTO;
import co.parameta.technical.test.commons.entity.EmployeeEntity;
import co.parameta.technical.test.commons.pojo.EmployeePojo;
import co.parameta.technical.test.commons.pojo.TypeDocumentPojo;
import co.parameta.technical.test.commons.util.mapper.BaseMapper;
import co.parameta.technical.test.soap.util.GeneralSoapUtil;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EmployeeMapper extends BaseMapper<EmployeeEntity, EmployeeDTO> {

    default EmployeeDTO pojoToDto(EmployeePojo employeePojo){
        EmployeeDTO employee = new EmployeeDTO();
        employee.setNames(employeePojo.getNames());
        employee.setLastNames(employeePojo.getLastNames());
        TypeDocumentDTO typeDocument = new TypeDocumentDTO();
        typeDocument.setCode(employeePojo.getTypeDocument().getCode());
        employee.setTypeDocument(typeDocument);
        employee.setDocumentNumber(employeePojo.getDocumentNumber());
        employee.setDateOfBirth(GeneralSoapUtil.toDate(employeePojo.getDateOfBirth()));
        employee.setDateAffiliationCompany(GeneralSoapUtil.toDate(employeePojo.getDateAffiliationCompany()));
        PositionDTO position = new PositionDTO();
        position.setCode(employeePojo.getPosition().getCode());
        employee.setPosition(position);
        employee.setSalary(employeePojo.getSalary());
        AdministratorUserDTO administratorUser = new AdministratorUserDTO();
        administratorUser.setCode(employeePojo.getAdministratorUser().getCode());
        employee.setAdministratorUser(administratorUser);
        return employee;
    }

}
