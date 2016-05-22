package pl.edu.wat.usos.usosapp.API;

import java.util.Map;

/**
 * Created by Rafal on 2016-05-22.
 */
public class GroupsUser {
    public Terms[] terms;
    public Map<String, Termses[]> groups;

    public Terms[] getTerms() {
        return terms;
    }

    public Map<String, Termses[]> getGroups() {
        return groups;
    }

    public class Terms {
        public int order_key;
        public String start_date;
        public String finish_date;
        public String id;
        public String end_date;
        public Name name;

        public String getFinish_date() {
            return finish_date;
        }

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

    public class Termses {
        Name course_name;
        String class_type_id;
        Name class_type;
        String course_unit_id;
        String term_id;
        String course_id;

        public Name getCourse_name() {
            return course_name;
        }

        public String getClass_type_id() {
            return class_type_id;
        }

        public Name getClass_type() {
            return class_type;
        }

        public String getCourse_unit_id() {
            return course_unit_id;
        }

        public String getTerm_id() {
            return term_id;
        }

        public String getCourse_id() {
            return course_id;
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
