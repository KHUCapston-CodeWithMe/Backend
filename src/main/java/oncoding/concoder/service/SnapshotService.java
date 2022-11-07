package oncoding.concoder.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oncoding.concoder.model.Snapshot;
import oncoding.concoder.repository.SnapshotRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class SnapshotService {
    
    private final SnapshotRepository repository;
    
    /**
     * 목록 조회
     * @return 현재 snpshot 리스트
     */
    public List<Snapshot> getSnapshots(){
        return repository.findAll();
    }
    
    /**
     * 단건 조회
     *
     * @return 현재 snpshot 리스트
     */
    public Snapshot getSnapshot(UUID id){
        return repository.findById(id).orElse(null);
    }
    
    /**
     * snapshot 생성
     * @param snapshot
     * @return
     */
    public Snapshot createSnapshot(Snapshot snapshot){
        return repository.save(snapshot);
    }
    
    /**
     * 스냅샷 수정 - 메모만 수정 가능
     * @param id
     * @param memo
     * @return
     */
    public Snapshot modifySnapshot(UUID id, String memo){
        
        Snapshot existing = repository.findById(id).orElse(null);
        existing.setMemo(memo);
        
        return repository.save(existing);
        
    }
    
    /**
     * 스냅샷 단건 삭제
     */
    public void deleteSnapshot(UUID id){
        repository.deleteById(id);
    }
    
    /**
     * 스냅샷 전체 삭제
     */
    public void deleteSnapshots(){
        repository.deleteAll();
    }

}
