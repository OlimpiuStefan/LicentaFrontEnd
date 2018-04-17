package com.example.stefao.smsreader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.service.LoginService;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.UserSessionManager;
import com.example.stefao.smsreader.utils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by stefao on 4/17/2018.
 */

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindViews({R.id.input_email, R.id.input_password, R.id.input_budget})
    List<EditText> userCredentials;
    ProgressDialog pDialog;
    UserSessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        session = new UserSessionManager(getApplicationContext());
        ButterKnife.bind(this);
    }

    @OnClick(R.id.link_login)
    public void goToLoginActivity() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }

//    public void registerUser(final JSONObject jsonRequest) {
//
//        final String stringRequest = jsonRequest.toString();
//
//        String URL = Constants.REGISTER_USER_URL;
//
//        final Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
//
//        JSONObject body = new JSONObject();
//
//        try {
//            body.put("username", jsonRequest.get("username"));
//            body.put("email", jsonRequest.get("email"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        final RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        StringRequest jsonObjectRequest = new StringRequest(
//                Request.Method.POST,
//                URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        JSONObject jsonResponse;
//                        String password = null;
//                        String email = null;
//                        try {
//                            jsonResponse = new JSONObject(response);
//                            password = jsonResponse.get("password").toString();
//                            email = jsonResponse.get("email").toString();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        doAutoLogin(email, password);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
//                        error.printStackTrace();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return headers;
//            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    return stringRequest == null ? null : stringRequest.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", stringRequest, "utf-8");
//                    return null;
//                }
//            }
//        };
//        requestQueue.add(jsonObjectRequest);
//    }

    private void doAutoLogin(String email, String password) {
        final String username = userCredentials.get(0).getText().toString();
        String URL = Constants.BASE_URL + "/user/getByUsername/" + username;

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        //headers.put("Authorization", "Basic " + Constants.CLIENT_CREDENTIALS_ENCODED);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        LoginService loginService = new LoginService();
                        try {
                            loginService.handleResponse(getApplicationContext(), session, response.get("email").toString(), response.get("password").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        pDialog.hide();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }

    @OnClick(R.id.btn_signup)
    public void register() {

        String email = userCredentials.get(0).getText().toString();
        String password = userCredentials.get(1).getText().toString();
        Double budget = Double.parseDouble(userCredentials.get(2).getText().toString());

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        final JSONObject user = new JSONObject();

        try {
            user.put("email", email);
            user.put("password", password);
            user.put("budget", budget);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonRequest = user;

        JSONObject body = new JSONObject();

        try {
            body.put("email", jsonRequest.get("email"));
            body.put("password", jsonRequest.get("password"));
            body.put("budget", jsonRequest.get("budget"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        pDialog = new ProgressDialog(this);

        pDialog.setMessage("Registering...");
        pDialog.setCancelable(false);
        pDialog.show();

        String URL = Constants.REGISTER_USER_URL;

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    URL,
                    body,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            {
                                LoginService loginService = new LoginService();
                                try {
                                    loginService.handleResponse(getApplicationContext(), session, response.get("email").toString(), response.get("password").toString());
                                    doAutoLogin(response.get("email").toString(), response.get("password").toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                pDialog.hide();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NoConnectionError) {
                                VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.NO_CONNECTION, RegisterActivity.this);
                            } else {
                                if (error.networkResponse != null) {
                                    int statusCode = error.networkResponse.statusCode;
                                    if (statusCode >= 500) {
                                        VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.SERVER_DOWN, RegisterActivity.this);
                                    }
                                }
                            }
                            pDialog.hide();
                        }
                    }
            )
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            requestQueue.add(request);
        }
}
