package uz.maniac4j.forwarder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.telegram.telegrambots.meta.api.objects.User;
import uz.maniac4j.forwarder.template.EntityLong;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sender {
    @Id
    private Long id;
    @OrderBy
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;
    private String telegramId;
    private String username;
    private String firstname;
    private String lastname;

    private boolean ban=false;
    public Sender(User user) {
        this.id = user.getId();
        this.username=user.getUserName();
        this.firstname=user.getFirstName();
        this.lastname=user.getLastName();
        this.ban=false;
    }

//    private void ban(){
//        ban=true;
//    }
//    private void unBan(){
//        ban=false;
//    }

    public Sender ban(){
        ban=true;
        return this;
    }
    public Sender unBan(){
        ban=false;
        return this;
    }

}
