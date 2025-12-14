package co.parameta.technical.test.soap.util.mapper;

import co.parameta.technical.test.commons.pojo.AdministratorUserPojo;
import co.parameta.technical.test.commons.util.mapper.BaseMapper;
import com.parameta.technical.test.soap.gen.AdministratorUser;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting administrator user data
 * between SOAP-generated objects and internal POJOs.
 * <p>
 * This mapper is used in the SOAP layer to translate
 * {@link AdministratorUser} SOAP elements into
 * {@link AdministratorUserPojo} domain objects and vice versa,
 * ensuring a clean separation between SOAP contracts and
 * internal application models.
 *
 * <p>Implemented automatically by MapStruct at compile time.</p>
 */
@Mapper(componentModel = "spring")
public interface AdministratorUserPojoMapper
        extends BaseMapper<AdministratorUser, AdministratorUserPojo> {
}
