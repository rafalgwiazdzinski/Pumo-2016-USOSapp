package pl.edu.wat.usos.usosapp;

/**
 * Created by Rafal on 2016-05-16.
 */
public class ApiUrls {

    public String[] studentInfoUrl = {"https://apps.uwm.edu.pl/services/users/user?fields=id|first_name|last_name|sex|email|student_programmes", "https://usosapps.wat.edu.pl/services/users/user?fields=id|first_name|last_name|sex|email|student_programmes" };
    public String[] studentCourseUrl = {"https://apps.uwm.edu.pl/services/courses/course_edition", "https://usosapps.wat.edu.pl/services/courses/course_edition"};
    public String[] studentGroupsUrl = {"https://apps.uwm.edu.pl/services/groups/participant?fields=course_unit_id|class_type|class_type_id|course_id&active_terms=false", "https://usosapps.wat.edu.pl/services/groups/participant?fields=course_unit_id|class_type|class_type_id|course_id&active_terms=false"};
    public String[] logOut = {"https://usosweb.uwm.edu.pl/kontroler.php?_action=logowaniecas/wyloguj", "https://usos.wat.edu.pl/kontroler.php?_action=logowaniecas/wyloguj"};
    public String[] revokeToken = {"https://apps.uwm.edu.pl/services/oauth/revoke_token", "https://usosapps.wat.edu.pl/services/oauth/revoke_token"};

    int universityId;

    public ApiUrls(int universityId) {
        this.universityId = universityId;
    }

    public String getStudentGroupsUrl() {
        return studentGroupsUrl[universityId];
    }

    public String getStudentInfoUrl() {
        return studentInfoUrl[universityId];
    }

    public String getStudentCourseUrl(String course_id, String term_id) {
        return (studentCourseUrl[universityId] + "?course_id=" + course_id + "&term_id=" +
                term_id + "&fields=course_id|term_id|grades");
    }

    public String getLogOut() {
        return logOut[universityId];
    }

    public String getRevokeToken() {
        return revokeToken[universityId];
    }
}
