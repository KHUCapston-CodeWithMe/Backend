package oncoding.concoder.service;


import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TestCaseService {

  private final RedisTemplate<String,Object> redisTemplate;


  /**
   * 해당 룸이 가지고 있는 테스트 케이스 전체 조회
   * @param roomId
   * @return
   */
  public List<Object> getTestCases(String roomId){

    //roomId는 first key값, testcaseId는 secondkey값 (내부의 key값)

    HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();

    hashOperations.entries(roomId);

    List<Object> list = (List<Object>) hashOperations.entries(roomId);

    return list;
  }


  /**
   * 특정 테스트 케이스 조회
   * @param roomId
   * @param testCaseId
   * @return
   */
  public Object getTestCase(String roomId,String testCaseId){

    HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();

    Object testcase = hashOperations.get(roomId,testCaseId);

    return testcase;

  }


  /**
   * 새로운 테스트 케이스 저장
   * @param roomId
   * @param ob
   * @return
   */
  public Object createTestCase(String roomId,JSONObject ob){
    HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();

    UUID testCaseId = UUID.randomUUID();

    hashOperations.put(roomId,testCaseId.toString(),ob);

    Object created = this.getTestCase(roomId,testCaseId.toString());

    return created;
  }

  /**
   * 특정 테스트 케이스 편집 - 수정
   * @param roomId
   * @param testCaseId
   * @param ob
   * @return
   */
  public Object modifyTestCase(String roomId,String testCaseId,JSONObject ob){
    HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
    hashOperations.put(roomId,testCaseId,ob);

    return this.getTestCase(roomId,testCaseId.toString());
    
  }

  /**
   * 테스트 케이스 삭제
   * @param roomId
   * @param testCaseId
   */
  public void deleteTestCase(String roomId, String testCaseId){
    HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
    hashOperations.delete(roomId,testCaseId);
  }
  
}
