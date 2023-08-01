package hw_2023_06_26__3_5_gh.exception;

public class StudentNotFoundException extends RuntimeException {
    private final long id;

    public StudentNotFoundException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Студент с id = " + id + " не найден!";
    }
}
