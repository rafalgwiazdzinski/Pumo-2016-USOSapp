package pl.edu.wat.usos.usosapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import pl.edu.wat.usos.usosapp.API.User;
import pl.edu.wat.usos.usosapp.OAuth.OAuthServiceBuilder;
import pl.edu.wat.usos.usosapp.adapter.UniversityAdapter;
import pl.edu.wat.usos.usosapp.university.University;

public class CallbackActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView name, sex, email;
    private String oauth_token;
    private String oauth_verifier;
    int university_id;
    private Context context = this;

    private DrawerLayout drawerLayout;
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    private String[] fragments;// = {"FRAGMENT 1", "FRAGMENT 2"};
    private ActionBarDrawerToggle drawerListener;

    private OAuth1AccessToken accessToken;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);

        fragments = getResources().getStringArray(R.array.array_string);

        listView = (ListView) findViewById(R.id.navList);
        listAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, fragments);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListener = new ActionBarDrawerToggle(this, drawerLayout, null , R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };



        drawerLayout.addDrawerListener(drawerListener);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        email = (TextView) findViewById(R.id.email);
        Uri uri = getIntent().getData();
        oauth_token = uri.getQueryParameter("oauth_token");
        oauth_verifier = uri.getQueryParameter("oauth_verifier");
        saveOauthVerifier();
        university_id = read_university_id();
        context = this;
        new AccessTokenTask().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch(position){
            case 0:
                startActivity(new Intent(this, GradesActivity.class));
                break;
            default:
                AlertDialog.Builder alert = new AlertDialog.Builder(CallbackActivity.this);
                alert.setTitle("O programie");
                alert.setMessage("Aplikacja USOSapp została wykonana w ramach projektu z przedmiotu PUMO\n\nSkład podgrupy:\n    Rafał Gwiaździński\n    Adrian Malczyk\n    Michał Turlej\n\nGrupa: E3C2S1");
                alert.setPositiveButton("OK", null);
                alert.show();
                break;
        }

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.relativeLayout, fragment). commit();

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerListener.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerListener.onConfigurationChanged(newConfig);
    }

    public void saveOauthVerifier() {
        SharedPreferences sharedPref = getSharedPreferences("appPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("OAUTH_VERIFIER", oauth_verifier);
        editor.commit();
    }

    public OAuth1RequestToken readRequestToken() {

        SharedPreferences sharedPref = getSharedPreferences("appPref", Context.MODE_PRIVATE);
        OAuth1RequestToken requestToken = new OAuth1RequestToken(sharedPref.getString("TOKEN", ""), sharedPref.getString("TOKEN_SECRET", ""), sharedPref.getString("RAW_RESPONSE", ""));
        return requestToken;
    }

    public int read_university_id() {
        SharedPreferences sharedPref = getSharedPreferences("appPref", Context.MODE_PRIVATE);
        int id = sharedPref.getInt("UNIVERSITY_ID", 0);
        return id;
    }

    public void setInformations() {
        name.setText(user.getFirst_name() + " " + user.getLast_name());
        if(user.getSex().equals("M"))
            sex.setText("Mężczyzna");
        else
            sex.setText("Kobieta");
        email.setText(user.getEmail());
    }

    public void saveAccessToken() {
        SharedPreferences sharedPref = getSharedPreferences("appPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ACCESS_TOKEN", accessToken.getToken());
        editor.putString("ACCESS_TOKEN_SECRET", accessToken.getTokenSecret());
        editor.commit();
    }

    private void save_student_ID(){
        SharedPreferences sharedPref = context.getSharedPreferences("appPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("USER_ID", user.getId());
        editor.commit();
    }

    public class AccessTokenTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                OAuth10aService service = OAuthServiceBuilder.getService((University) new UniversityAdapter().getItem(university_id));
                accessToken = service.getAccessToken(readRequestToken(), oauth_verifier);
                OAuthRequest request = new OAuthRequest(Verb.GET, new ApiUrls(university_id).getStudentInfoUrl(), service);
                service.signRequest(accessToken, request);
                Response response = request.send();
                String responseBody = response.getBody();
                Gson gson = new Gson();

                user = gson.fromJson(responseBody, User.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                setInformations();
                save_student_ID();
                saveAccessToken();
            } catch (NullPointerException e) {
                Toast.makeText(context, "Błąd podczas wczytywania danych, zrestartuj aplikacje i spóbuj ponownie.", Toast.LENGTH_LONG).show();
            }
        }
    }
}

