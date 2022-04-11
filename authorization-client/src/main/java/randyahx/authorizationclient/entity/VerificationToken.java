package randyahx.authorizationclient.entity;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity @Data @NoArgsConstructor
public class VerificationToken {

    private static final int EXPIRATION_TIME = 10;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable = false, foreignKey = @ForeignKey(name="FK_USER_VERIFY_TOKEN"))
    private User user;

    public VerificationToken(String token, User user) {
        super();
        this.token = token;
        this.user= user;
        this.expirationTime = getExpirationDate(EXPIRATION_TIME);
    }

    public VerificationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = getExpirationDate(EXPIRATION_TIME);
    }

    private Date getExpirationDate(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
