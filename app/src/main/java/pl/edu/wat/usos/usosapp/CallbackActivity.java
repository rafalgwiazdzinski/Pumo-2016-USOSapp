package pl.edu.wat.usos.usosapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.wat.usos.usosapp.OAuth.OAuthServiceBuilder;
import pl.edu.wat.usos.usosapp.adapter.UniversityAdapter;
import pl.edu.wat.usos.usosapp.university.University;

public class CallbackActivity extends AppCompatActivity {

    TextView name, sex, email;
    private String oauth_token;
    private String oauth_verifier;
    int university_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        email = (TextView) findViewById(R.id.email);
        Uri uri = getIntent().getData();
        oauth_token = uri.getQueryParameter("oauth_token");
        oauth_verifier = uri.getQueryParameter("oauth_verifier");
        university_id = read_university_id();
        new AccessTokenTask().execute();
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

    public void setInformations(String sName, String sSex, String sEmail) {
        name.setText(sName);
        if(sSex.equals("M"))
            sex.setText("Mężczyzna");
        else
            sex.setText("Kobieta");
        email.setText(sEmail);
    }

    public class AccessTokenTask extends AsyncTask<Void, Void, Void> {
        String expiration;
        JSONObject jsonObject;

        String sName, sSex, sEmail;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                OAuth10aService service = OAuthServiceBuilder.getService((University) new UniversityAdapter().getItem(university_id));
                final OAuth1AccessToken accessToken = service.getAccessToken(readRequestToken(), oauth_verifier);

                OAuthRequest request = new OAuthRequest(Verb.GET, "https://apps.uwm.edu.pl/services/users/user?user_id=53033&fields=id|first_name|last_name|sex|email", service);
                service.signRequest(accessToken, request);
                Response response = request.send();
                String responseBody = response.getBody();
                jsonObject = new JSONObject(responseBody);
                sName = jsonObject.getString("first_name") + " " + jsonObject.getString("last_name");
                sSex = jsonObject.getString("sex");
                sEmail = jsonObject.getString("email");
                expiration = jsonObject.toString(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setInformations(sName, sSex, sEmail);
        }
    }
}

