package pl.edu.wat.usos.usosapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.edu.wat.usos.usosapp.API.Courses;
import pl.edu.wat.usos.usosapp.R;

/**
 * Created by Rafal on 2016-05-16.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    Map<String, Courses.Termses[]> courses;
    String semestr = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCourse;

        public MyViewHolder(View view) {
            super(view);
            tvCourse = (TextView) view.findViewById(R.id.tvCourse);
        }
    }

    public CourseAdapter(Map<String, Courses.Termses[]> courses, String semestr) {
        this.courses = courses;
        this.semestr = semestr;
    }

    public String getCourseByPosition(int position){
        Map<String, Courses.Termses[]> coursesGet = new LinkedHashMap<>();
        coursesGet = courses;
        //return coursesGet.values().toArray()[position].toString();'
        return coursesGet.get(semestr)[position].getCourse_name().getPl();
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
    }

    @Override
    public int getItemCount() {

        return courses.get(semestr).length;
    }
}
