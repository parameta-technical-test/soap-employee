package co.parameta.technical.test.soap.util.mapper;

import co.parameta.technical.test.commons.pojo.TypeDocumentPojo;
import co.parameta.technical.test.commons.util.mapper.BaseMapper;
import com.parameta.technical.test.soap.gen.TypeDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TypeDocumentMapper extends BaseMapper<TypeDocument, TypeDocumentPojo> {

}
