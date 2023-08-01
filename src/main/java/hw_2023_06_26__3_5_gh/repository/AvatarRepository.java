package hw_2023_06_26__3_5_gh.repository;

import hw_2023_06_26__3_5_gh.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByStudent_Id(long StudentId);
}
