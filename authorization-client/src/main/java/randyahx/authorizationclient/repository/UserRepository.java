package randyahx.authorizationclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import randyahx.authorizationclient.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
