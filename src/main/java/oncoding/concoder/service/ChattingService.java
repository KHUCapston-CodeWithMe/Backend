package oncoding.concoder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.ChatDTO.UserAndRoomResponse;
import oncoding.concoder.dto.ChatDTO.ExitResponse;
import oncoding.concoder.dto.ChatDTO.MessageRequest;
import oncoding.concoder.dto.ChatDTO.MessageResponse;
import oncoding.concoder.dto.ChatDTO.UserResponse;
import oncoding.concoder.dto.UserDto;
import oncoding.concoder.model.Room;
import oncoding.concoder.model.User;
import oncoding.concoder.repository.RoomRepository;
import oncoding.concoder.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChattingService {

  private final UserRepository userRepository;
  private final RoomRepository roomRepository;

  private final SimpMessagingTemplate messagingTemplate;

  private final UserService userService;

  /**
   * 
   * @param request
   * @return request 내용을 바탕으로 생성된 메시지의 responseDTO 리턴
   */
  private MessageResponse getMessageResponse(final MessageRequest request) {
    UserDto.UserInfo userInfo = userService.getUserInfoById(request.getUserId());

    return new MessageResponse(userInfo.getUserId(), userInfo.getName(), request.getContent()); //보낼 메세지 객체를 리턴
  }

  public void sendMessage(String roomId, JSONObject ob) {
    UUID userId = UUID.fromString(ob.get("userId").toString());
    String content = ob.get("content").toString();

    MessageRequest request = new MessageRequest(userId, content);
    MessageResponse response = getMessageResponse(request);

    messagingTemplate.convertAndSend("/sub/video/chat/"+ roomId , response);
  }

  /**
   * 
   * @param roomId 입장하려는 room의 Id
   * @param userId 입장하려는 user의 Id
   * @return 입장 후 방에 있는 모든 User들의 정보 반환
   */
  public List<UserResponse> enter(final String roomId, final String userId) {
    UUID roomUUID = UUID.fromString(roomId);
    UUID userUUID = UUID.fromString(userId);

    User user = userRepository.findById(userUUID).orElseThrow(IllegalArgumentException::new);
    Room room = roomRepository.findById(roomUUID).orElseThrow(IllegalArgumentException::new);

    log.debug("<<<<<enter service>>>>>");
    log.debug("entered user: {}:{}", user.getId(), user.getName());
    log.debug("entered room: {}", room.getId());

    user.setRoom(room);
    userRepository.save(user);

    room.addUser(user);
    roomRepository.save(room);

    return room.getUsers().stream()
            .map(UserResponse::from)
            .collect(Collectors.toList());
  }

  /**
   *
   * @param roomId 퇴장하려는 room의 Id
   * @param userId 퇴장하려는 user의 Id
   * @return 나가는 방에서 나가려는 User 삭제 후의 User들을 반환
   */
  @CacheEvict(value = "UserInfo", key = "#userId", cacheManager = "redisCacheManager")
  public ExitResponse exit(final String roomId, final String userId) {

    UUID roomUUID = UUID.fromString(roomId);
    UUID userUUID = UUID.fromString(userId);

    Room room = roomRepository.findById(roomUUID).orElseThrow(IllegalArgumentException::new);
    String removeUserName = room.removeUser(userUUID);

    userRepository.deleteById(userUUID);

    log.debug("User {} exited Room {}", userId, roomId);

    if (room.getUsers().isEmpty()) {
      roomRepository.deleteById(roomUUID);
      log.debug("Room {} deleted.", roomId);
    }

    return ExitResponse.from(roomUUID, userUUID, removeUserName);
  }


  public UserResponse createOnlyUser(String username){
    User user = userRepository.save(new User(username));
    return UserResponse.from(user);
  }



  public UserAndRoomResponse createRoomAndUser(String username){

    List<User> users = new ArrayList<>();
    List<Room> rooms = new ArrayList<>();

    users.add(userRepository.save(new User(username)));
    rooms.add(roomRepository.save(new Room(2)));

    return UserAndRoomResponse.of(users, rooms);
  }


  public void clear() {
    userRepository.deleteAll();
    roomRepository.deleteAll();
  }

}
