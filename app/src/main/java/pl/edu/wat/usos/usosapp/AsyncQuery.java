package pl.edu.wat.usos.usosapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.google.gson.Gson;

import pl.edu.wat.usos.usosapp.API.User;
import pl.edu.wat.usos.usosapp.OAuth.OAuthServiceBuilder;
import pl.edu.wat.usos.usosapp.adapter.UniversityAdapter;
import pl.edu.wat.usos.usosapp.university.University;

/**
 * Created by Rafal on 2016-05-13.
 */
    public class AsyncQuery extends AsyncTask<Void, Void, Object> {
        User user;

        OAuth1AccessToken accessToken;

        Context context;
        int university_id;
        private String oauth_verifier;
        private String url;

        public AsyncQuery(Context context, String url) {
            this.url = url;
            this.context = context;
            this.university_id = read_university_id();
            this.oauth_verifier = readOauthVerifier();
            accessToken = read_accessToken();
        }

        public OAuth1RequestToken readRequestToken() {

            SharedPreferences sharedPref = context.getSharedPreferences("appPref", Context.MODE_PRIVATE);
            OAuth1RequestToken requestToken = new OAuth1RequestToken(sharedPref.getString("TOKEN", ""), sharedPref.getString("TOKEN_SECRET", ""), sharedPref.getString("RAW_RESPONSE", ""));
            return requestToken;
        }

        public String readOauthVerifier() {
            SharedPreferences sharedPref = context.getSharedPreferences("appPref", Context.MODE_PRIVATE);
            String verifier = sharedPref.getString("OAUTH_VERIFIER", "");
            return verifier;
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
            return new OAuth1AccessToken(access_token,access_token_secret);
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                OAuth10aService service = OAuthServiceBuilder.getService((University) new UniversityAdapter().getItem(university_id));
                OAuthRequest request = new OAuthRequest(Verb.GET, url, service);
                service.signRequest(accessToken, request);
                Response response = request.send();
                String responseBody = response.getBody();
                Gson gson = new Gson();

                user = gson.fromJson(responseBody, User.class);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return user;
        }
    }
