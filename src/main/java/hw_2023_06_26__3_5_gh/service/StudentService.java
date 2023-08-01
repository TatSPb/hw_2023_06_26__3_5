package hw_2023_06_26__3_5_gh.service;

import hw_2023_06_26__3_5_gh.dto.*;
import hw_2023_06_26__3_5_gh.entity.Avatar;
import hw_2023_06_26__3_5_gh.entity.Student;
import hw_2023_06_26__3_5_gh.exception.*;
import hw_2023_06_26__3_5_gh.mapper.*;
import hw_2023_06_26__3_5_gh.repository.*;
import io.micrometer.common.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;
    private final AvatarService avatarService;

    public StudentService(StudentRepository studentRepository,
                          FacultyRepository facultyRepository,
                          StudentMapper studentMapper, FacultyMapper facultyMapper, AvatarService avatarService) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
        this.avatarService = avatarService;
    }

    public StudentDtoOut create(StudentDtoIn studentDtoIn) {
        return studentMapper.toDto(
                studentRepository.save(
                        studentMapper.toEntity(studentDtoIn)));
    }

    public StudentDtoOut update(long id, StudentDtoIn studentDtoIn) {
        return studentRepository.findById(id)
                .map(oldStudent -> {
                    oldStudent.setName(studentDtoIn.getName());
                    oldStudent.setAge(studentDtoIn.getAge());
                    Optional.ofNullable(studentDtoIn.getFacultyId())
                            .ifPresent(facultyId ->
                                    oldStudent.setFaculty(
                                            facultyRepository.findById(facultyId)
                                                    .orElseThrow(() -> new FacultyNotFoundException(facultyId))
                                    )
                            );
                    return studentMapper.toDto(studentRepository.save(oldStudent));
                })
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public StudentDtoOut delete(long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return studentMapper.toDto(student);
    }

    public StudentDtoOut get(long id) {
        return studentRepository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public List<StudentDtoOut> findAll(@Nullable Integer age) {
        return Optional.ofNullable(age)
                .map(studentRepository::findAllByAge)
                .orElseGet(studentRepository::findAll).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentDtoOut> findByAgeBetween(int ageFrom, int ageTo) {
        return studentRepository.findByAgeBetween(ageFrom, ageTo).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public FacultyDtoOut findFaculty(long id) {
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .map(facultyMapper::toDto)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public StudentDtoOut uploadAvatar(long id, MultipartFile multipartFile) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        Avatar avatar = avatarService.create(student, multipartFile);
        StudentDtoOut studentDtoOut = studentMapper.toDto(student);
        studentDtoOut.setAvatarUrl("http://localhost:8089/avatars/" + avatar.getId() + "/from-db");
        return studentDtoOut;
    }
}
