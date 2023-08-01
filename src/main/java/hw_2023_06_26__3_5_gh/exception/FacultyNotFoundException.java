package hw_2023_06_26__3_5_gh.exception;

public class FacultyNotFoundException extends RuntimeException {
    private final long id;

    public FacultyNotFoundException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Факультет с id = " + id + " не найден!";
    }
}
