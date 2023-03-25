package oncoding.concoder.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import oncoding.concoder.model.Room;
import oncoding.concoder.model.User;

public class ChatDTO {

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserAndRoomResponse {

    private List<UserResponse> users;
    private List<RoomResponse> rooms;

    public static UserAndRoomResponse of(final List<User> users, final List<Room> rooms) {
      List<UserResponse> userResponses = users.stream()
          .map(UserResponse::from)
          .collect(Collectors.toList());
      List<RoomResponse> roomResponses = rooms.stream()
          .map(RoomResponse::from)
          .collect(Collectors.toList());

      return new UserAndRoomResponse(userResponses, roomResponses);
    }
  }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExitResponse {

      private UUID roomId;
      private UUID userId;
      private String name;

      public static ExitResponse from(UUID roomUUID, UUID userUUID, String userName) {
        return new ExitResponse(roomUUID, userUUID, userName);
      }
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageRequest {

      private UUID userId;
      private String content;
    }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageResponse {

      private UUID userId;
      private String username;
      private String content;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoomResponse {

      private UUID id;
      private Integer maxHeadCount;

      public static RoomResponse from(final Room room) {
        return new RoomResponse(room.getId(), room.getMaxHeadCount());
      }
    }


  @Setter
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class SessionForTest {


    private UUID id;
    private String sessionId;

  }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponse {

      private UUID id;
      private String name;

      public static UserResponse from(final User user) {

        return new UserResponse(user.getId(), user.getName());
      }
    }

  }
