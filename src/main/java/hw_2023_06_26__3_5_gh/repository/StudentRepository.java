package hw_2023_06_26__3_5_gh.repository;

import hw_2023_06_26__3_5_gh.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);
    List<Student> findByAgeBetween(int ageFrom, int ageTo);
    List<Student> findAllByFaculty_Id(long facultyId);
}
