package pl.edu.wat.usos.usosapp.OAuth;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth10aService;

import pl.edu.wat.usos.usosapp.UsosAPI;
import pl.edu.wat.usos.usosapp.university.University;

/**
 * Created by Rafal on 2016-04-10.
 */
public class OAuthServiceBuilder {
    public static OAuth10aService getService(University university) {
        return new ServiceBuilder()
                .apiKey(university.consumerKey)
                .apiSecret(university.consumerSecret)
                .callback("usosapp://")
                .build(UsosAPI.setUniversity(university));
    }
}
