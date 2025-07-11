package sm.central.exception.custom;

public class TeacherNotFoundException extends RuntimeException{
    public TeacherNotFoundException(String message){
        super(message);
    }
    @Override
    public String getMessage() {
        return super.getMessage(); // Returns "Student not found with ID STUD0002"
    }
}
