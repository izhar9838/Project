package sm.central.exception.custom;

public class AssignmentNotFoundException extends RuntimeException{
    public AssignmentNotFoundException(String message){
        super(message);
    }
    @Override
    public String getMessage() {
        return super.getMessage(); // Returns "Student not found with ID STUD0002"
    }
}
