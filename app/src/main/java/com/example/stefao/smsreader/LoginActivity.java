package com.example.stefao.smsreader;

/**
 * Created by stefao on 4/16/2018.
 */

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
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.stefao.smsreader.service.LoginService;
import com.example.stefao.smsreader.utils.UserSessionManager;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.VolleyUtils;

import static com.example.stefao.smsreader.utils.UserSessionManager.KEY_EMAIL;

public class LoginActivity extends AppCompatActivity {

    @BindViews({R.id.input_email, R.id.input_password})
    List<EditText> userCredentials;
    ProgressDialog pDialog;
    UserSessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new UserSessionManager(getApplicationContext());
        ButterKnife.bind(this);
    }

    @OnClick(R.id.link_signup)
    public void goToRegisterActivity() {
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_login)
    public void login(final View view) {
        final String username = userCredentials.get(0).getText().toString();
        final String password = userCredentials.get(1).getText().toString();
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // String URL = String.format(Constants.REQUEST_TOKEN_URL, username, password);


        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Trying to log in..");
        pDialog.setMessage("Please wat...");
        pDialog.setCancelable(false);
        pDialog.show();

        String URL = Constants.BASE_URL + "/user/getByUsername/" + username;

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        LoginService loginService = new LoginService();
                        try {
                            if (password.equals(response.get("password").toString())) {
                                loginService.handleResponse(getApplicationContext(), session, response.get("email").toString(), response.get("password").toString());
                                Log.e("==>>",response.get("email").toString());
                            }
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
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                URL,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            getUserData(username, response.get("access_token").toString(), response.get("refresh_token").toString(), Long.parseLong(response.get("expires_in").toString()));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (error instanceof NoConnectionError) {
//                            VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.NO_CONNECTION, LoginActivity.this);
//                        } else {
//                            if (error.networkResponse != null) {
//                                int statusCode = error.networkResponse.statusCode;
//                                if (statusCode == 400) {
//                                    VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.WRONG_CREDENTIALS, LoginActivity.this);
//                                } else {
//                                    VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.SERVER_DOWN, LoginActivity.this);
//                                }
//                            }
//                        }
//
//                        if (error instanceof TimeoutError) {
//                            VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.SERVER_DOWN, LoginActivity.this);
//                        }
//
//                        pDialog.hide();
//                    }
//                }
//        )
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return VolleyUtils.getBasicAuthHeaders();
//            }
//        };



//    private void getUserData(String username) {
//        String URL = Constants.BASE_SECURE_URL + "/user/getByUsername/" + username;
//
//        final RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET,
//                URL,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        LoginService loginService = new LoginService();
//                        try {
//                            loginService.handleResponse(getApplicationContext(), session, response.get("email").toString(), response.get("password").toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        pDialog.hide();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        pDialog.hide();
//                    }
//                }
//        )
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return VolleyUtils.getBearerAuthheaders(accessToken);
//            }
//        };
//        requestQueue.add(jsonObjectRequest);
//    }

}

