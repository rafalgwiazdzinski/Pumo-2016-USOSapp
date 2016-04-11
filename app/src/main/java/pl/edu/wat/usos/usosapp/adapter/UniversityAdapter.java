package pl.edu.wat.usos.usosapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pl.edu.wat.usos.usosapp.R;
import pl.edu.wat.usos.usosapp.university.University;

/**
 * Created by Rafal on 2016-04-10.
 */
public class UniversityAdapter extends BaseAdapter {

    public static final University[] universities = new University[]{
            new University(0, "Uniwersytet Warmińsko-Mazurski",
                    "https://apps.uwm.edu.pl",
                    "Sw5E3Nay6wDs7bnVRrCe",
                    "NJcHTMxtKsSnLLdfe6bNVaw8vBDCxHCCKfUpVwRb"),

            new University(1, "Wojskowa Akademia Techniczna",
                    "https://usosapps.wat.edu.pl",
                    "9G6AR9WpNXz3uR2QmAFY",
                    "Wju2N3pm9bSf6MERYydEY5RcNJn6wWeXfpNj5JaE")
    };

    private LayoutInflater layoutInflater;

    public UniversityAdapter() {
    }

    public UniversityAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return universities.length;
    }

    @Override
    public Object getItem(int position) {
        return universities[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.university_item, parent, false);
        }

        University item = universities[position];
        ((TextView) convertView).setText(item.name);

        return convertView;

    }
}
