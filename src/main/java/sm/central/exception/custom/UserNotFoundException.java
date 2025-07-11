package sm.central.exception.custom;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);
    }
    @Override
    public String getMessage() {
        return super.getMessage(); // Returns "Student not found with ID STUD0002"
    }
}
