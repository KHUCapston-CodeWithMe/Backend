package oncoding.concoder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.UserDto;
import oncoding.concoder.model.User;
import oncoding.concoder.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Cacheable(value = "UserInfo", key = "#userId", cacheManager = "redisCacheManager")
    public UserDto.UserInfo getUserInfoById(UUID userId) {
        User findUser = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        return UserDto.UserInfo.of(findUser);
    }
}
