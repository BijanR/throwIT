package de.fh_dortmund.throwit.login;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest {


    private static final String LOGIN_REQUEST_URL = "Domain.php";
    private Map<String, String> params;

    public LoginRequest(String username, String password, Response.Listener<String> listener){

        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }


    @Override
    public Map<String, String> getParams(){
        return params;
    }



}
