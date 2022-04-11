package randyahx.authorizationclient.service;

import randyahx.authorizationclient.entity.User;
import randyahx.authorizationclient.entity.VerificationToken;
import randyahx.authorizationclient.model.UserModel;

import java.util.Optional;

public interface UserService {
    User registerUser(UserModel userModel);
    void saveVerificationToken(String token, User user);

    String validateVerificationToken(String token);
    VerificationToken generateVerificationToken(String oldToken);

    User findUserByEmail(String email);

    void generatePasswordResetToken(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    boolean verifyOldPassword(User user, String oldPassword);
}
