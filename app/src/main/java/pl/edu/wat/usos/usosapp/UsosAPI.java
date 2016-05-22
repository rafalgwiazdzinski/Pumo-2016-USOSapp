package pl.edu.wat.usos.usosapp;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

import pl.edu.wat.usos.usosapp.university.University;

/**
 * Created by Rafal on 2016-04-03.
 */
public class UsosAPI extends DefaultApi10a {

    static String baseUrl;

    private static final String AUTHORIZE_URL = "/oauth/authorize?oauth_token=%s";
    private static final String REQUEST_TOKEN_RESOURCE = "/oauth/request_token";
    private static final String ACCESS_TOKEN_RESOURCE = "/oauth/access_token";
    private static final String SCOPES = "?scopes=cards|grades|email|photo|studies"; // przykladowe

    public UsosAPI() {
    }

    public static UsosAPI setUniversity(University university) {
        baseUrl = university.getServiceUrl();
        return new UsosAPI();
    }

    @Override
    public String getAccessTokenEndpoint() {
        return baseUrl + ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return baseUrl + REQUEST_TOKEN_RESOURCE + SCOPES;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(baseUrl + AUTHORIZE_URL, requestToken.getToken());
    }
}

