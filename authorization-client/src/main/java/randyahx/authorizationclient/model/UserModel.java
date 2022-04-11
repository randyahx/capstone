package randyahx.authorizationclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data @AllArgsConstructor @NoArgsConstructor
public class UserModel {
    private String firstname;
    private String lastName;
    private String email;
    private String password;
    private String matchingpassword;
}
