package coworking.repository;

import coworking.model.UserEntity;
import coworking.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


@org.springframework.stereotype.Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByName(String name);

}
