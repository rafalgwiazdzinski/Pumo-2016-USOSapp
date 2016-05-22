package pl.edu.wat.usos.usosapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.usos.usosapp.API.Course;
import pl.edu.wat.usos.usosapp.API.GroupsUser;
import pl.edu.wat.usos.usosapp.R;

/**
 * Created by Rafal on 2016-05-16.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    String semestr = "";
    ArrayList<Course> course;
    Map<String, GroupsUser.Termses[]> courses;
    Map<String, String> unit_course_type;
    ArrayList<String> alPrzedmioty = new ArrayList<String>();
    ArrayList<String> alPrzedmiotyId = new ArrayList<String>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCourse;
        public TextView tvWyklad;

        public MyViewHolder(View view) {
            super(view);
            tvCourse = (TextView) view.findViewById(R.id.tvCourse);
            tvWyklad = (TextView) view.findViewById(R.id.tvWyklad);
        }
    }

    public CourseAdapter(Map<String, GroupsUser.Termses[]> courses, String semestr, ArrayList<Course> course) {
        this.semestr = semestr;
        this.course = course;
        this.courses = courses;
        unit_course_type = new HashMap<String, String>();
        GroupsUser.Termses[] termses = courses.get(semestr);
        int j = 0;
        for(int i = 0; i< termses.length; i++) {
            if(!alPrzedmioty.contains(termses[i].getCourse_name().getPl())) {
                alPrzedmioty.add(termses[i].getCourse_name().getPl());
                alPrzedmiotyId.add(termses[i].getCourse_id());
            }
            unit_course_type.put(termses[i].getCourse_unit_id(), termses[i].getClass_type().getPl());
        }
    }

    public String getCourseByPosition(int position){
        return alPrzedmioty.get(position);
    }

    public String getCourseByIdPosition(int position){
        return alPrzedmiotyId.get(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.courses_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvCourse.setText(getCourseByPosition(position));
        String ocn = "";
        try {
            for (Map.Entry<String, Map<String, Course.Grades.ExamSessionNumber>> entry : course.get(position).getGrades().getCourse_units_grades().entrySet()) {
                if(unit_course_type.containsKey(entry.getKey())) {
                    ocn = ocn + unit_course_type.get(entry.getKey()) + ":";
                    String termin =" ";
                    for(int i=0; i<entry.getValue().size(); i++) {
                        if(i<(entry.getValue().size()-1))
                            termin = termin + "(" + entry.getValue().get(Integer.toString(i+1)).getValue_symbol() + ") ";
                        else
                            termin = termin + entry.getValue().get(Integer.toString(i+1)).getValue_symbol();
                    }
                    ocn = ocn + termin + "\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvWyklad.setText(ocn);
    }

    @Override
    public int getItemCount() {

        return alPrzedmioty.size();
    }
}
