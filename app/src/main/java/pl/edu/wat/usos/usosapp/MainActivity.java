package pl.edu.wat.usos.usosapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import pl.edu.wat.usos.usosapp.adapter.UniversityAdapter;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    private Context context = this;
    private UniversityAdapter universityAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);
        universityAdapter = new UniversityAdapter(this);
        spinner.setAdapter(universityAdapter);
    }


    public void login(View view) {
        int position = spinner.getSelectedItemPosition();
        save_university_id(position);
        String[] request_token = new String[0];
        try {
            request_token = new LoginTask(position).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(request_token != null) {
            save_requestToken(request_token);
            String authURL = request_token[0];
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(authURL));
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "Błąd. Sprawdź połączenie z internetem", Toast.LENGTH_LONG).show();
        }
    }

    public void save_requestToken(String[] request_token) {
        SharedPreferences sharedPref = getSharedPreferences("appPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("TOKEN", request_token[1]);
        editor.putString("TOKEN_SECRET", request_token[2]);
        editor.putString("RAW_RESPONSE", request_token[3]);
        editor.commit();
    }

    public void save_university_id(int id) {
        SharedPreferences sharedPref = getSharedPreferences("appPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("UNIVERSITY_ID", id);
        editor.commit();
    }


}

