package oncoding.concoder.controller;


import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import oncoding.concoder.dto.SnapshotDto;
import oncoding.concoder.dto.SnapshotDto.GetAll;
import oncoding.concoder.mapper.SnapshotDtoMapper;
import oncoding.concoder.service.SnapshotService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/snapshots", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class SnapshotController {
    
    private final SnapshotService service;
    private final SnapshotDtoMapper mapper;
    
    /**
     * 전체 조회
     * @return
     */
    @SneakyThrows
    @GetMapping
    public List<GetAll> getSnapshots(){
        return mapper.toSnapshotGetAllList(service.getSnapshots());
    }
    
    /**
     * 단건 조회
     * @param
     * @return
     */
    @SneakyThrows
    @GetMapping("/{snapshotId}")
    public GetAll getSnapshot(@PathVariable UUID snapshotId){
        return mapper.toSnapshotDtoGetAll(service.getSnapshot(snapshotId));
    }
    
    /**
     * 새로운 스냅샷 저장
     * @param in
     * @return
     */
    @SneakyThrows
    @PostMapping
    public GetAll createSnapshot(SnapshotDto.Add in){
        return mapper.toSnapshotDtoGetAll(service.createSnapshot(mapper.toSnapshot(in)));
    }
    
    
    /**
     * 수정
     * @param
     * @return
     */
    @SneakyThrows
    @PutMapping
    public GetAll modifySnapshot(SnapshotDto.Modify in){
        UUID id = in.getId();
        String memo = in.getMemo();
        return mapper.toSnapshotDtoGetAll(service.modifySnapshot(id, memo));
    }
    
    
    /**
     * 삭제
     * @param
     * @return
     */
    @SneakyThrows
    @DeleteMapping("/{snapshotId}")
    public void deleteSnapshot(@PathVariable UUID snapshotId){
        service.deleteSnapshot(snapshotId);
    }
    
    
}
