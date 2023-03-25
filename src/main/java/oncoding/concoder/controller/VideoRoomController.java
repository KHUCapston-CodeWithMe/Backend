package oncoding.concoder.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.ChatDTO.UserAndRoomResponse;
import oncoding.concoder.dto.ChatDTO.ExitResponse;
import oncoding.concoder.dto.ChatDTO.UserResponse;
import oncoding.concoder.service.ChattingService;
import org.json.simple.JSONObject;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VideoRoomController {

  private final ChattingService chattingService;
  private final SimpMessagingTemplate template;

  @MessageMapping("/video/chat/{roomId}")
  public void chat(@DestinationVariable final String roomId, JSONObject ob) {
    log.debug("/rooms/chat/{}, userId: {}", roomId, ob.get("userId").toString());
    log.debug("/rooms/chat/{}, content: {}", roomId, ob.get("content").toString());

    chattingService.sendMessage(roomId, ob);

    log.debug("after chatting convert and send");
  }

  /**
   * 코드를 해당 룸 사용자들에게 공유
   * @param roomId
   * @param ob
   */
  @MessageMapping("/code/{roomId}")
  private void codeShare(@DestinationVariable final String roomId,JSONObject ob) {

    template.convertAndSend("/sub/code/"+roomId,ob);

    log.debug("convertAndSend to /sub/code/{}: {}", roomId, ob.toJSONString());

  }




  // 실시간으로 들어온 User 감지하여 전체 User 리스트 반환
  @MessageMapping("/video/joined-room-info/{roomId}")
  private List<UserResponse> joinRoom(@DestinationVariable final String roomId, JSONObject ob) {

    String userId = ob.get("userId").toString();

    log.debug("@MessageMapping(\"/video/joined-room-info\") roomId : {}", roomId);
    log.debug("@MessageMapping(\"/video/joined-room-info\") userId : {}", userId);

    List<UserResponse> userResponseList = chattingService.enter(roomId, userId);

    template.convertAndSend("/sub/video/joined-room-info/"+ roomId, userResponseList);

    log.debug("convertAndSend to /sub/video/joined-room-info/{}: {}", roomId, userResponseList);

    return userResponseList;

  }

  // 실시간으로 나간 User 감지하여 리턴
  @MessageMapping("/video/unjoined-room-info/{roomId}")
  private ExitResponse unJoinRoom(@DestinationVariable final String roomId, JSONObject ob) {

    String userId = (String) ob.get("userId");

    log.debug("@MessageMapping(\"/video/unjoined-room-info\") roomId: {}, userId: {}", roomId, userId);

    ExitResponse exitResponse = chattingService.exit(roomId, userId);

    template.convertAndSend("/sub/video/unjoined-room-info/"+roomId, exitResponse);

    log.debug("convertAndSend to /sub/video/unjoined-room-info/{}", userId);

    return exitResponse;
  }




  // caller의 정보를 다른 callee들에게 쏴준다.
  @MessageMapping("/video/caller-info/{roomId}")
  private void caller(@DestinationVariable final String roomId,JSONObject ob) {


    template.convertAndSend("/sub/video/caller-info/"+roomId,ob);

    log.debug("convertAndSend to /sub/video/caller-info/{} : {}", roomId, ob.toJSONString());

  }

  // caller와 callee의 signaling을 위해 callee 정보를 쏴준다.
  @MessageMapping("/video/callee-info/{roomId}")
  private void answerCall(@DestinationVariable final String roomId,JSONObject ob) {

    template.convertAndSend("/sub/video/callee-info/"+roomId,ob);
    log.debug("convertAndSend to /sub/video/callee-info/{} : {}", roomId, ob);

  }


  @EventListener
  private void handleSessionConnected(SessionConnectEvent event) {
    String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
  //connect하고 나서 클라이언트한테 본내주면 될 듯
    log.debug("session connected sessionId: {}", sessionId);

  }


  @EventListener
  public void handleSessionDisconnect(SessionDisconnectEvent event) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = accessor.getSessionId();

    log.debug("disconnect event Listener sessionId: {}", sessionId);
  }


  @PostMapping("/video")
  public ResponseEntity<UserAndRoomResponse> createRoomAndUser(@RequestBody JSONObject ob) {
    String username = (String) ob.get("username");
    UserAndRoomResponse response = chattingService.createRoomAndUser(username);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/video/user")
  public ResponseEntity<UserResponse> createUser(@RequestBody JSONObject ob) {
    String username = (String) ob.get("username");
    UserResponse response = chattingService.createOnlyUser(username);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/video")
  public void clearRoomAndUser() {
    chattingService.clear();
  }


}
