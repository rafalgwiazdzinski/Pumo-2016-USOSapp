package pl.edu.wat.usos.usosapp.API;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Rafal on 2016-05-14.
 */
public class Courses {
    Map<String, Termses[]> course_editions;
    Terms[] terms;

    public Map<String, Termses[]> getCourse_editions() {
        return course_editions;
    }

    public Terms[] getTerms() {
        return terms;
    }

    public class Termses  {
        String course_id;
        String term_id;
        CourseName course_name;

        public CourseName getCourse_name() {
            return course_name;
        }

        public String getTerm_id() {
            return term_id;
        }

        public String getCourse_id() {
            return course_id;
        }

        public class CourseName {
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

    public class Terms {
        int order_key;
        String start_date;
        String id;
        String end_date;
        Name name;

        public int getOrder_key() {
            return order_key;
        }

        public String getStart_date() {
            return start_date;
        }

        public String getId() {
            return id;
        }

        public String getEnd_date() {
            return end_date;
        }

        public Name getName() {
            return name;
        }

        public class Name {
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
