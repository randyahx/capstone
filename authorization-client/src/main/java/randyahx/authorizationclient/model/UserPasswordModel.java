package randyahx.authorizationclient.model;

import lombok.Data;

@Data
public class UserPasswordModel {
    private String email;
    private String oldPassword;
    private String newPassword;
}
