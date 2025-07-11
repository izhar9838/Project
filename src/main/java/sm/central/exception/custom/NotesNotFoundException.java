package sm.central.exception.custom;

public class NotesNotFoundException extends RuntimeException{
    public NotesNotFoundException(String message){
        super(message);
    }
    @Override
    public String getMessage() {
        return super.getMessage(); // Returns "Student not found with ID STUD0002"
    }
}
