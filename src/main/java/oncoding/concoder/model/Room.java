package oncoding.concoder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends JpaBaseEntity{


  @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
  private List<User> users = new ArrayList<>();

  private int maxHeadCount;

  @Builder
  public Room(final int maxHeadCount) {
    this.maxHeadCount = maxHeadCount;
  }

  public void addUser(User user) {
    users.add(user);
  }

  public String removeUser(UUID userUUID) {
    for (User user : users) {
      if (user.getId().equals(userUUID)) {
        users.remove(user);

        return user.getName();
      }
    }

    return "";
  }
}
