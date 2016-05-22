package pl.edu.wat.usos.usosapp;

import android.os.AsyncTask;
import android.util.Log;

import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import pl.edu.wat.usos.usosapp.OAuth.OAuthServiceBuilder;
import pl.edu.wat.usos.usosapp.adapter.UniversityAdapter;
import pl.edu.wat.usos.usosapp.university.University;

/**
 * Created by Rafal on 2016-04-03.
 */
public class LoginTask extends AsyncTask<Void, Void, String[]> {

    private int id;

    public LoginTask(int id) {
        this.id = id;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        try {
            OAuth10aService service = OAuthServiceBuilder.getService((University) new UniversityAdapter().getItem(id));
            final OAuth1RequestToken requestToken = service.getRequestToken();
            String authUrl = service.getAuthorizationUrl(requestToken);
            String[] request_token = {authUrl, requestToken.getToken(), requestToken.getTokenSecret(), requestToken.getRawResponse()};
            return request_token;
        } catch (Exception e){
            return null;
        }
    }
}

