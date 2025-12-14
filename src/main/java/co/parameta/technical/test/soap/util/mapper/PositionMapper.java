package co.parameta.technical.test.soap.util.mapper;

import co.parameta.technical.test.commons.pojo.PositionPojo;
import co.parameta.technical.test.commons.util.mapper.BaseMapper;
import com.parameta.technical.test.soap.gen.Position;
import org.mapstruct.Mapper;

/**
 * Mapper responsible for converting position information between
 * SOAP-generated objects and internal POJOs.
 * <p>
 * This mapper is used to translate {@link Position} elements defined
 * in the SOAP contract into {@link PositionPojo} objects used internally,
 * and vice versa.
 * </p>
 *
 * <p>
 * The mapping logic is delegated to MapStruct and follows the
 * conventions defined in {@link BaseMapper}.
 * </p>
 */
@Mapper(componentModel = "spring")
public interface PositionMapper extends BaseMapper<Position, PositionPojo> {
}
