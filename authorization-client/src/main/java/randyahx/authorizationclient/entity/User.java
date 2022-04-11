package randyahx.authorizationclient.entity;

import lombok.*;

import javax.persistence.*;

@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @Column(length=60)
    private String password;
    private String role;
    private Boolean isEnabled = false;
}
