package pl.edu.wat.usos.usosapp.API;

import java.util.Map;

/**
 * Created by Rafal on 2016-05-21.
 */
public class Course {
    String course_id;
    String term_id;
    Grades grades;

    public String getCourse_id() {
        return course_id;
    }

    public Grades getGrades() {
        return grades;
    }

    public String getTerm_id() {
        return term_id;
    }

    public class Grades {
        Map<String, Map<String, ExamSessionNumber>> course_units_grades;

        public Map<String, Map<String, ExamSessionNumber>> getCourse_units_grades() {
            return course_units_grades;
        }

        public class ExamSessionNumber {
            String value_symbol;
            int exam_session_number;
            int exam_id;

            public String getValue_symbol() {
                return value_symbol;
            }

            public int getExam_session_number() {
                return exam_session_number;
            }

            public int getExam_id() {
                return exam_id;
            }
        }
    }
}
