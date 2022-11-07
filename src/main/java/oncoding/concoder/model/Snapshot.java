package oncoding.concoder.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Snapshot implements Serializable {
    
    private static final long serialVersionUID = 2148687123052233925L;
    
    @Id
    private UUID id;
    
    @Column(updatable=false, nullable = false)
    private LocalDateTime createdDate;
    
    @Column(updatable=false, nullable = false)
    private LocalDateTime modifiedDate;
    
    @Column
    @NotNull
    private String memo;
    
    @Column
    @NotNull
    private String content;
    
    
    public void setId(UUID id){
        this.id = id;
    }
    
    public void setMemo(String memo){
        this.memo = memo;
    }
    
    public void setCreatedDate(LocalDateTime time){
        this.createdDate = time;
    }
    
    public void setModifiedDate(LocalDateTime time){
        this.modifiedDate = time;
    }
    
}
