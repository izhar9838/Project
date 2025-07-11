package sm.central.dto.teacher;

import lombok.Data;

import java.util.List;

@Data
public class MarksEntryDTO {
    private String subjectName;
    private String examTerm;
    private List<StudentMarkDTO> marks;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getExamTerm() {
        return examTerm;
    }

    public void setExamTerm(String examTerm) {
        this.examTerm = examTerm;
    }

    public List<StudentMarkDTO> getMarks() {
        return marks;
    }

    public void setMarks(List<StudentMarkDTO> marks) {
        this.marks = marks;
    }
    @Data
    public static class StudentMarkDTO {
        private String studentId;
        private Integer marks;

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public Integer getMarks() {
            return marks;
        }

        public void setMarks(Integer marks) {
            this.marks = marks;
        }
    }
}