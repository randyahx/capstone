package randyahx.authorizationclient.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import randyahx.authorizationclient.entity.User;

@Getter @Setter
public class RegisterEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;

    public RegisterEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
