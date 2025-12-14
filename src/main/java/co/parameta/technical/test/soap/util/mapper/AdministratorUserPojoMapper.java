package co.parameta.technical.test.soap.util.mapper;

import co.parameta.technical.test.commons.pojo.AdministratorUserPojo;
import co.parameta.technical.test.commons.util.mapper.BaseMapper;
import com.parameta.technical.test.soap.gen.AdministratorUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdministratorUserPojoMapper extends BaseMapper<AdministratorUser, AdministratorUserPojo> {
}
