package coworking.repository;

import coworking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


@org.springframework.stereotype.Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);

}
