package pl.edu.wat.usos.usosapp.API;

/**
 * Created by Rafal on 2016-05-02.
 */
public class User {

    String id;
    String first_name;
    String last_name;
    String sex;
    String email;
    StudentProgrammes[] student_programmes;

    public String getId() {
        return id;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getSex() {
        return sex;
    }

    public String getEmail() {
        return email;
    }

    public StudentProgrammes[] getStudent_programmes() {
        return student_programmes;
    }

    public class StudentProgrammes {
        String id;
        Programme programme;

        public String getId() {
            return id;
        }

        public Programme getProgramme() {
            return programme;
        }
    }

    public class Programme {
        String id;
        Description description;

        public String getId() {
            return id;
        }

        public Description getDescription() {
            return description;
        }

        public class Description {
            String en;
            String pl;

            public String getEn() {
                return en;
            }

            public String getPl() {
                return pl;
            }
        }
    }
}
