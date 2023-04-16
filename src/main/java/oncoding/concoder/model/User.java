package oncoding.concoder.model;


import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends JpaBaseEntity {

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id")
  private Room room;

  public User(final String name) {
    this.name = name;
  }

  public void setRoom(Room room) {
    this.room = room;
  }
}
