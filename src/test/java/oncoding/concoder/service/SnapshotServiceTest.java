package oncoding.concoder.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.UUID;
import oncoding.concoder.model.Snapshot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class SnapshotServiceTest {
    
    @Autowired
    SnapshotService service;
    
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    
    
    @Test
    void redis_전체_조회(){
    
        Snapshot snapshot = Snapshot.builder().memo("memo").content("hi").build();
        snapshot.setId(UUID.randomUUID());
        
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("Snapshot",snapshot.getId().toString(),snapshot);
        
        Map<String,Snapshot> map = service.getSnapshots();
        assertTrue((map.get("332a90fb-c02f-4b35-b273-b66eab7e6140").getMemo()).equals("modified_memo"));
        
    }
    
    
    @Test
    void redis_단건_조회(){
        Snapshot snapshot = service.getSnapshot(UUID.fromString("332a90fb-c02f-4b35-b273-b66eab7e6140"));
        assertTrue(snapshot != null);
        assertTrue(snapshot.getMemo().equals("memo"));
        assertTrue(snapshot.getContent().equals("hi"));
    }
    
    @Test
    void redis_생성_및_저장(){
    
        Snapshot snapshot = Snapshot.builder().memo("new_memo").content("new_content").build();
        Snapshot newSnapshot = service.createSnapshot(snapshot);
        
        assertTrue(newSnapshot.getMemo().equals("new_memo"));
        assertTrue(snapshot.getContent().equals("new_content"));
        
    }
    
    @Test
    void redis_메모_수정(){
        Snapshot shot = service.getSnapshot(UUID.fromString("332a90fb-c02f-4b35-b273-b66eab7e6140"));
        assertTrue(shot.getMemo().equals("memo"));
        Snapshot newshot = service.modifySnapshot(UUID.fromString("332a90fb-c02f-4b35-b273-b66eab7e6140"),"modified_memo");
       assertTrue(newshot.getMemo().equals("modified_memo"));
    
    }
    
    
    @Test
    void redis_단건_삭제(){
        
        //[068d718b-4a80-4bb5-96bf-fec395e1d5b3, a8296418-127a-4839-80e7-6697f730cc5a, c95224ea-b7cc-4ded-b23d-09c5670f5145, 99f6168a-1bf0-46b2-bb70-dad04fdc0421, 030434af-2eab-4b95-af6e-1abbc872d282, ebe37cef-4795-41e7-8b16-7fa169728898, 3412bd33-950c-4c68-9db6-a955987e63a3, bf937828-49fa-4a3d-ae83-8b6256939ae3, ed9365b1-9899-4c28-b7cc-f6c83354fb30, f4351de8-8719-49f4-99c5-b2a2989057df, 10361e27-794e-4dcc-b17d-4ebccdedb146, 332a90fb-c02f-4b35-b273-b66eab7e6140, 513fb53d-b6e1-4bbd-bb59-3067150e15dc, b3477404-557e-44c0-859f-201912c78d33, 4c3fef4a-2e04-4e1b-86ca-fff34b8e67bc, 348cb38a-6640-4a72-a7cd-9c769ec1f6e1, 740def97-106b-4446-9632-df74b03bbc06]
    
        assertTrue(service.getSnapshot(UUID.fromString("068d718b-4a80-4bb5-96bf-fec395e1d5b3"))!= null);
        service.deleteSnapshot(UUID.fromString("068d718b-4a80-4bb5-96bf-fec395e1d5b3"));
        assertTrue(service.getSnapshot(UUID.fromString("068d718b-4a80-4bb5-96bf-fec395e1d5b3"))== null);
    
    }
    
    
}
