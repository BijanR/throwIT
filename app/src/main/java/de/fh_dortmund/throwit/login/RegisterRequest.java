package de.fh_dortmund.throwit.login;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {


    private static final String REGISTER_REQUEST_URL = "Domain.php";
    private Map<String, String> params;

    public RegisterRequest(String username, String password, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null); // write in PHP-file
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }


    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
