package sm.central.exception.custom;

public class InvalidUsernamePasswordException extends RuntimeException{
    public InvalidUsernamePasswordException(String msg){
        super(msg);
    }
    @Override
    public String getMessage() {
        return super.getMessage(); // Returns "Student not found with ID STUD0002"
    }
}
