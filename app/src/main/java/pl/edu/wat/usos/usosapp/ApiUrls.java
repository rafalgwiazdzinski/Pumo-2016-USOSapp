package pl.edu.wat.usos.usosapp;

/**
 * Created by Rafal on 2016-05-16.
 */
public class ApiUrls {
    String studentInfoUrl = "https://apps.uwm.edu.pl/services/users/user?fields=id|first_name|last_name|sex|email|student_programmes";
    String studentCoursesUrl = "https://apps.uwm.edu.pl/services/courses/user?active_terms_only=false&fields=course_editions|terms";

    public String getStudentInfoUrl() {
        return studentInfoUrl;
    }

    public String getStudentCoursesUrl() {
        return studentCoursesUrl;
    }
}
