package oncoding.concoder.mapper;

import java.util.List;
import oncoding.concoder.dto.SnapshotDto;
import oncoding.concoder.dto.SnapshotDto.GetAll;
import oncoding.concoder.model.Snapshot;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(
    implementationName = "SnapshotDtoMapperImpl",
    builder=@Builder(disableBuilder = true),
    componentModel = "spring"
)
public abstract class SnapshotDtoMapper {

    public abstract Snapshot toSnapshot(SnapshotDto.Add in);
    public abstract Snapshot toSnapshot(SnapshotDto.Modify in);
    public abstract SnapshotDto.GetAll toSnapshotDtoGetAll(Snapshot snapshot);
    
    public abstract List<GetAll> toSnapshotGetAllList(List<Snapshot> in);
}
