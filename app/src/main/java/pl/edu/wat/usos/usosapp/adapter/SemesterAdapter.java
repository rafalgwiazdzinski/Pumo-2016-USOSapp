package pl.edu.wat.usos.usosapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.edu.wat.usos.usosapp.API.Courses;
import pl.edu.wat.usos.usosapp.R;
import pl.edu.wat.usos.usosapp.university.University;

/**
 * Created by Rafal on 2016-05-16.
 */
public class SemesterAdapter extends BaseAdapter{

    public ArrayList<String> semesters;
    private LayoutInflater layoutInflater;
    public String[] semestersIdSorted;
    Courses.Terms[] terms;
    Integer[] orders;

    public SemesterAdapter(Context context, Integer[] orders, Courses.Terms[] terms) {//ArrayList<String> semesters) {
        //this.semesters = semesters;
        this.orders = orders;
        this.terms = terms;
        semesters = new ArrayList<String>();
        semestersIdSorted = new String[this.terms.length];
        orderNames();
        layoutInflater = LayoutInflater.from(context);
    }

    public void orderNames() {
        int i = 0;
        while(true) {
            for (int j = 0; j < orders.length; j++) {
                if (orders[i].equals(terms[j].getOrder_key())) {
                    semesters.add(terms[j].getName().getPl());
                    semestersIdSorted[i] = terms[j].getId();
                }
            }
            if (i < orders.length - 1)
                ++i;
            else
                return;
        }
    }

    @Override

    public int getCount() {
            return semesters.size();
    }

    @Override
    public String getItem(int position) {
        //return terms[position].getId();
        return semesters.get(position);
    }

    public String getSemesterId(int position) {
        return semestersIdSorted[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.semester_item, parent, false);
        }

        ((TextView) convertView).setText(getItem(position));

        return convertView;
    }
}
