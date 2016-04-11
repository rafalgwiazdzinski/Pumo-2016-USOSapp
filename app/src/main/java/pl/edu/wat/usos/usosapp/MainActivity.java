package pl.edu.wat.usos.usosapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import java.util.concurrent.ExecutionException;

import pl.edu.wat.usos.usosapp.adapter.UniversityAdapter;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    UniversityAdapter universityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);
        universityAdapter = new UniversityAdapter(this);
        spinner.setAdapter(universityAdapter);
    }

    public void login(View view) throws ExecutionException, InterruptedException {
        int position = spinner.getSelectedItemPosition();
        save_university_id(position);
        String[] request_token = new LoginTask(position).execute().get();
        save_requestToken(request_token);
        String authURL = request_token[0];
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(authURL));
        startActivity(i);
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

