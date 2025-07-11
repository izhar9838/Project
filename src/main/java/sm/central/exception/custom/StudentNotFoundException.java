package sm.central.exception.custom;

public class StudentNotFoundException extends RuntimeException{
    public StudentNotFoundException(String message){
        super(message);
    }
    @Override
    public String getMessage() {
        return super.getMessage(); // Returns "Student not found with ID STUD0002"
    }
}
