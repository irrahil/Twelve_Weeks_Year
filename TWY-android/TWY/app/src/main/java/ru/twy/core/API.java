package ru.twy.core;

import ru.twy.connection.ClientConnect;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by irit on 18.10.16.
 */
public class API {
    private static API ourInstance = new API();
    private static final int LEVEL_API = Settings.LEVEL_API;
    protected Settings settings;
    protected ClientConnect httpClient;

    public static API getInstance() {
        return ourInstance;
    }

    private API() {
    }


    public void init() {
        settings = Settings.getInstance();
        httpClient = ClientConnect.getInstance();
        httpClient.connectToURL(settings.getServerAddr() + "/api" );
    }



    public int LoginToServer(boolean reinit) {
        if (reinit)
            this.init();
        StringBuilder post_request = new StringBuilder();
        post_request.append("cmd=login");
        post_request.append("&user_login=");
        post_request.append(settings.getUserName() );
        post_request.append("&user_pass=");
        post_request.append(settings.getUserPass() );
        post_request.append("&device_id=");
        post_request.append(settings.getDeviceId() );
        String result = httpClient.sendPost(post_request.toString() );
        if (result != null) {
            if (result.indexOf("Error!") != -1)
                return Settings.CONNECTION_ERROR;
            if (result.indexOf("Server ref") != -1)
                return Settings.SERVER_ERROR;
            try {
                JSONObject obj = new JSONObject(result);
                settings.setUserToken(obj.getString("token"));
                return Settings.LOGIN_CORRECT;
            } catch (JSONException e) {
                e.printStackTrace();
                return Settings.JSON_ERROR;
            }
        } else
            return Settings.CONNECTION_ERROR;
    }


}
