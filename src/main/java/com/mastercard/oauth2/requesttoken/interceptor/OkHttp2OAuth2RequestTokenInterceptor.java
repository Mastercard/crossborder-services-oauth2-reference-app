package com.mastercard.oauth2.requesttoken.interceptor;

import com.mastercard.oauth2.requesttoken.constants.Oauth2Constants;
import com.mastercard.oauth2.requesttoken.generator.Oauth2RequestTokenGenerator;
import com.mastercard.oauth2.requesttoken.models.TokenInput;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * An OkHttp2 interceptor for computing and adding an OAuth2 request token authorization header to HTTP requests.
 */
public class OkHttp2OAuth2RequestTokenInterceptor implements Interceptor {

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private final Oauth2RequestTokenGenerator clientTokenGenerator;
    private final String consumerKey;
    public OkHttp2OAuth2RequestTokenInterceptor(String oauthKeyFile, String keyAlias, String oauthKeyPassword, String consumerKey) {
        this.clientTokenGenerator = new Oauth2RequestTokenGenerator(oauthKeyFile, keyAlias, oauthKeyPassword);
        this.consumerKey = consumerKey;
    }

    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        TokenInput tokenInput = TokenInput.builder()
                .consumerKey(consumerKey)
                .build();

        String token = Oauth2Constants.BEARER + Oauth2Constants.SPACE + clientTokenGenerator.generateToken(tokenInput);
        builder.addHeader(AUTHORIZATION_HEADER_NAME, token);
        return chain.proceed(builder.build());
    }

}
