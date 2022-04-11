package randyahx.authorizationclient.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import randyahx.authorizationclient.entity.PasswordResetToken;
import randyahx.authorizationclient.entity.User;
import randyahx.authorizationclient.entity.VerificationToken;
import randyahx.authorizationclient.model.UserModel;
import randyahx.authorizationclient.repository.PasswordResetTokenRepository;
import randyahx.authorizationclient.repository.UserRepository;
import randyahx.authorizationclient.repository.VerificationTokenRepository;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service @RequiredArgsConstructor @Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstname());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationToken(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return "invalid";
        }

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();

        if (verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        if (user.getIsEnabled()) {
            return "enabled";
        }

        user.setIsEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);

        if (verificationToken != null) {
            verificationToken.setToken(UUID.randomUUID().toString());
            verificationTokenRepository.save(verificationToken);

            return verificationToken;
        }

        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void generatePasswordResetToken(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null) {
            return "invalid";
        }

        User user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();

        if (passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }

        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean verifyOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }


}
