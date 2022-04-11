package randyahx.authorizationclient.event.listener;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import randyahx.authorizationclient.entity.User;
import randyahx.authorizationclient.event.RegisterEvent;
import randyahx.authorizationclient.service.UserService;

import java.util.UUID;

@Component @RequiredArgsConstructor @Slf4j
public class RegisterEventListener implements ApplicationListener<RegisterEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(RegisterEvent event) {
        // Create token + link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationToken(token, user);
        // Send email to user
        String url = event.getApplicationUrl() + "/api/user/verifyRegistration?token=" + token;
        log.info("Verify registration: {}", url);
    }
}
