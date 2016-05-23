package pl.edu.wat.usos.usosapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;

import pl.edu.wat.usos.usosapp.API.Course;
import pl.edu.wat.usos.usosapp.API.GroupsUser;
import pl.edu.wat.usos.usosapp.OAuth.OAuthServiceBuilder;
import pl.edu.wat.usos.usosapp.adapter.CourseAdapter;
import pl.edu.wat.usos.usosapp.adapter.SemesterAdapter;
import pl.edu.wat.usos.usosapp.adapter.UniversityAdapter;
import pl.edu.wat.usos.usosapp.university.University;

public class GradesActivity extends AppCompatActivity {

    private SemesterAdapter semestrAdapter;
    private ArrayList<Course> courses = new ArrayList<Course>();
    private String semestr;
    private Course[] course1;
    private int j = 0;
    private String responseBody2;
    private ProgressDialog pd;

    private OAuth1AccessToken accessToken;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter courseAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView textView;
    private Spinner spinner;
    private String responseBody = "";
    private ArrayList<String> oceny = new ArrayList<String>();

    private GroupsUser groupsUser;
    private Context context = this;
    private int universityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        accessToken = read_accessToken();

        textView = (TextView) findViewById(R.id.json);
        spinner = (Spinner) findViewById(R.id.spinner);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        pd = new ProgressDialog(context);
        universityId = read_university_id();

        try {
            groupsUser = new GroupsQuery().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(groupsUser ==null) {
            Toast.makeText(context, "Brak dostępu do internetu", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, MainActivity.class);
            startActivity(i);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //pd.show(context, "Pobieranie ocen", "Proszę czekać...");
                semestr = semestrAdapter.getSemesterId(position);
                j = 0;
                GroupsUser.Termses[] course_term = groupsUser.getGroups().get(semestr);


                ArrayList<String> courseTerm = new ArrayList<String>();
                courseTerm.add(semestr);
                for(int i = 0; i< course_term.length; i++) {
                    if(!courseTerm.contains(course_term[i].getCourse_id())) {
                        courseTerm.add(course_term[i].getCourse_id());
                    }
                }
                int blad = 0;

                try {
                    blad = new GradesQuery().execute(courseTerm).get();
                    wyswietl_przedmioty_semestru();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                if(blad < 0) {
                    Toast.makeText(context, "Brak dostępu do internetu", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, MainActivity.class);
                    startActivity(i);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    public void wyswietl_przedmioty_semestru() {
        courseAdapter = new CourseAdapter(groupsUser.getGroups(), semestr, courses);
        mRecyclerView.setAdapter(courseAdapter);
    }

    public Integer[] sortowanie_semestrow() {
        GroupsUser.Terms[] semestry;// = new Courses.Terms[courses.getTerms().length];
        semestry = groupsUser.getTerms();
        Integer[] orders = new Integer[semestry.length];
        for (int i = 0; i < orders.length; i++) {
            orders[i] = semestry[i].getOrder_key();
        }
        Arrays.sort(orders, Collections.reverseOrder());
        return orders;
    }

    public int read_university_id() {
        SharedPreferences sharedPref = context.getSharedPreferences("appPref", Context.MODE_PRIVATE);
        int id = sharedPref.getInt("UNIVERSITY_ID", 0);
        return id;
    }

    public OAuth1AccessToken read_accessToken() {
        SharedPreferences sharedPref = context.getSharedPreferences("appPref", Context.MODE_PRIVATE);
        String access_token = sharedPref.getString("ACCESS_TOKEN", "");
        String access_token_secret = sharedPref.getString("ACCESS_TOKEN_SECRET", "");
        return new OAuth1AccessToken(access_token, access_token_secret);
    }

    private OAuth10aService service;
    private ApiUrls apiUrls = new ApiUrls(universityId);

    public class GroupsQuery extends AsyncTask<Void, Void, GroupsUser> {

        @Override
        protected GroupsUser doInBackground(Void... params) {
            try {
                int universityId = read_university_id();
                service = OAuthServiceBuilder.getService((University) new UniversityAdapter().getItem(read_university_id()));
                OAuthRequest request = new OAuthRequest(Verb.GET, apiUrls.getStudentGroupsUrl(), service);
                service.signRequest(read_accessToken(), request);
                Response response = request.send();
                responseBody = response.getBody();
                Gson gson = new Gson();
                GroupsUser groups = gson.fromJson(responseBody, GroupsUser.class);

                JsonParser parser = new JsonParser();
                Gson gson2 = new GsonBuilder().setPrettyPrinting().create();

                JsonElement el = parser.parse(responseBody);
                responseBody = gson2.toJson(el);
                return groups;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(GroupsUser o) {
            super.onPostExecute(o);
            try {
                if (o != null) {
                    Integer[] posortowane_semestry = sortowanie_semestrow();
                    semestrAdapter = new SemesterAdapter(context, posortowane_semestry, o.getTerms());
                    spinner.setAdapter(semestrAdapter);
                    responseBody = "";
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Toast.makeText(context, "Zaloguj się!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(context, MainActivity.class));
            }
        }
    }

    public class GradesQuery extends AsyncTask<ArrayList<String>, Void, Integer> {

        private Course course;


        public GradesQuery() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(ArrayList<String>... params) {
            try {
                courses = new ArrayList<Course>();
                //service = OAuthServiceBuilder.getService((University) new UniversityAdapter().getItem(read_university_id()));
                for (int i = 1; i < params[0].size(); i++) {
                    OAuthRequest request = new OAuthRequest(Verb.GET, apiUrls.getStudentCourseUrl(params[0].get(i), params[0].get(0)), service);
                    service.signRequest(accessToken, request);
                    Response response = request.send();
                    responseBody2 = response.getBody();
                    Gson gson = new Gson();
                    course = gson.fromJson(responseBody2, Course.class);
                    courses.add(course);
                    publishProgress();
                }
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            wyswietl_przedmioty_semestru();
        }
    }
}
