package pl.edu.wat.usos.usosapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import pl.edu.wat.usos.usosapp.API.Courses;
import pl.edu.wat.usos.usosapp.OAuth.OAuthServiceBuilder;
import pl.edu.wat.usos.usosapp.adapter.CourseAdapter;
import pl.edu.wat.usos.usosapp.adapter.SemesterAdapter;
import pl.edu.wat.usos.usosapp.adapter.UniversityAdapter;
import pl.edu.wat.usos.usosapp.university.University;

public class GradesActivity extends AppCompatActivity {

    SemesterAdapter semestrAdapter;
    String semestr;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter courseAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView textView;
    Spinner spinner;

    Courses courses;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        textView = (TextView) findViewById(R.id.json);
        spinner = (Spinner) findViewById(R.id.spinner);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        new TestowePobieranie().execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semestr = semestrAdapter.getSemesterId(position);
                wyswietl_przedmioty_semestru();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

    }

    public void wyswietl_przedmioty_semestru(){
        courseAdapter = new CourseAdapter(courses.getCourse_editions(), semestr);
        mRecyclerView.setAdapter(courseAdapter);
    }

    public Integer[] sortowanie_semestrow(){
        Courses.Terms[] semestry = new Courses.Terms[courses.getTerms().length];
        semestry = courses.getTerms();
        Integer[] orders = new Integer[semestry.length];
        for(int i=0; i<orders.length; i++){
            orders[i] = semestry[i].getOrder_key();
        }
        //Arrays.sort(orders);
        Arrays.sort(orders, Collections.reverseOrder());
        return orders;
    }


    public class TestowePobieranie extends AsyncTask<Void, Void, Object> {
        String responseBody;
        String mapa = "";
        ArrayList<String> semesters = new ArrayList<String>();

        public int read_university_id() {
            SharedPreferences sharedPref = context.getSharedPreferences("appPref", Context.MODE_PRIVATE);
            int id = sharedPref.getInt("UNIVERSITY_ID", 0);
            return id;
        }

        public OAuth1AccessToken read_accessToken() {
            SharedPreferences sharedPref = context.getSharedPreferences("appPref", Context.MODE_PRIVATE);
            String access_token = sharedPref.getString("ACCESS_TOKEN", "");
            String access_token_secret = sharedPref.getString("ACCESS_TOKEN_SECRET", "");
            return new OAuth1AccessToken(access_token,access_token_secret);
        }

        @Override
        protected Object doInBackground(Void... params) {

            //try {
                OAuth10aService service = OAuthServiceBuilder.getService((University) new UniversityAdapter().getItem(read_university_id()));
                OAuthRequest request = new OAuthRequest(Verb.GET, new ApiUrls().getStudentCoursesUrl(), service);
                service.signRequest(read_accessToken(), request);
                Response response = request.send();
                responseBody = response.getBody();
                Gson gson = new Gson();
                courses = gson.fromJson(responseBody, Courses.class);
                int i = 0;

                /*JsonParser parser = new JsonParser();
                Gson gson2 = new GsonBuilder().setPrettyPrinting().create();

                JsonElement el = parser.parse(responseBody);
                responseBody = gson2.toJson(el); // done*/

            //} catch (Exception e) {
             //   e.printStackTrace();
            //}
            //return responseBody;
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Integer[] posortowane_semestry = sortowanie_semestrow();
            semestrAdapter = new SemesterAdapter(context, posortowane_semestry, courses.getTerms());
            spinner.setAdapter(semestrAdapter);
            //textView.setText(responseBody);
        }
    }
}
