package pl.spoda.ks.database.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;
import pl.spoda.ks.database.dto.MatchDetailsDto;

@Mapper(mappingControl = DeepClone.class)
public interface CloningMapper {

    CloningMapper INSTANCE = Mappers.getMapper( CloningMapper.class);

    MatchDetailsDto clone(MatchDetailsDto in);

}
