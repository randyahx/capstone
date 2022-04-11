package randyahx.authorizationclient.controller;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import randyahx.authorizationclient.entity.User;
import randyahx.authorizationclient.entity.VerificationToken;
import randyahx.authorizationclient.event.RegisterEvent;
import randyahx.authorizationclient.model.UserModel;
import randyahx.authorizationclient.model.UserPasswordModel;
import randyahx.authorizationclient.repository.UserRepository;
import randyahx.authorizationclient.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@RestController @RequestMapping("/api/user") @RequiredArgsConstructor @Slf4j
public class RegisterController {

    private final UserService userService;
    private final ApplicationEventPublisher event;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, HttpServletRequest request) {
        User user = userService.registerUser(userModel);
        event.publishEvent(new RegisterEvent(user, getApplicationUrl(request)));
        return "User registered.";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam String token) {
        String result = userService.validateVerificationToken(token);

        if (result.equalsIgnoreCase("valid")) {
            return "User verified.";
        } else if (result.equalsIgnoreCase("enabled")) {
            return "User is already verified.";
        }

        return "User verification error.";
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
        VerificationToken verificationToken = userService.generateVerificationToken(oldToken);

        if (verificationToken != null) {
            User user = verificationToken.getUser();
            verificationTokenUrl(user, getApplicationUrl(request), verificationToken);

            return "Verification token sent.";
        }

        return "Token was already requested.";
    }

    private void verificationTokenUrl(User user, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl + "/api/user/verifyRegistration?token=" + verificationToken.getToken();
        log.info("Verify token: {}", url);
    }


    private String getApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @PostMapping("/resetPassword")
    private String resetPassword(@RequestBody UserPasswordModel passwordModel, HttpServletRequest request) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";

        if (user != null) {
            String token = UUID.randomUUID().toString();
            userService.generatePasswordResetToken(user, token);
            url = passwordResetTokenUrl(user, getApplicationUrl(request), token);
        }

        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody UserPasswordModel userPasswordModel) {
        String result = userService.validatePasswordResetToken(token);

        if (!result.equalsIgnoreCase("valid")) {
            return "Token invalid.";
        }

        Optional<User> user = userService.getUserByPasswordResetToken(token);

        if (user.isPresent()) {
            userService.changePassword(user.get(), userPasswordModel.getNewPassword());
            return "Password has been reset.";
        } else {
            return "Token invalid";
        }
    }

    private String passwordResetTokenUrl(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/api/user/savePassword?token=" + token;
        log.info("Reset password: {}", url);

        return url;
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody UserPasswordModel userPasswordModel) {
        User user = userService.findUserByEmail(userPasswordModel.getEmail());
        if (!userService.verifyOldPassword(user, userPasswordModel.getOldPassword())) {
            return "Current password is invalid.";
        }

        userService.changePassword(user, userPasswordModel.getNewPassword());
        return "Password changed.";
    }
}
