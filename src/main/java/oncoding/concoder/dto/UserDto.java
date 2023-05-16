package oncoding.concoder.dto;

import lombok.*;
import oncoding.concoder.model.User;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

public class UserDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Cacheable
    public static class UserInfo {

        private UUID userId;
        private String name;
        private UUID roomId;

        public static UserInfo of(User user) {
            return UserInfo.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .roomId(user.getRoom().getId())
                    .build();
        }
    }

}
