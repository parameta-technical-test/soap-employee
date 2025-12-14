package co.parameta.technical.test.soap.util.mapper;

import co.parameta.technical.test.commons.pojo.PositionPojo;
import co.parameta.technical.test.commons.util.mapper.BaseMapper;
import com.parameta.technical.test.soap.gen.Position;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionMapper extends BaseMapper<Position, PositionPojo> {
}
