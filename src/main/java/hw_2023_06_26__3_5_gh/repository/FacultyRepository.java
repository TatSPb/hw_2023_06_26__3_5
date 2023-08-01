package hw_2023_06_26__3_5_gh.repository;

import hw_2023_06_26__3_5_gh.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findAllByColor(String color);
    List<Faculty> findByColorContainingIgnoreCaseOrNameContainingIgnoreCase(String color, String name);
}
