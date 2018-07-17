package com.example.stefao.smsreader.view.login;

/**
 * Created by stefao on 4/16/2018.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;


import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.utils.UserSessionManager;
import com.example.stefao.smsreader.viewmodel.login.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    @BindViews({R.id.input_email, R.id.input_password})
    List<EditText> userCredentials;
    ProgressDialog pDialog;
    UserSessionManager session;
    LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        loginViewModel = new LoginViewModel();
        session = new UserSessionManager(getApplicationContext());
        ButterKnife.bind(this);
//        userCredentials.get(0).setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (!b) {
//                    userCredentials.get(0).setHintTextColor((Color.parseColor("#1FDB84")));
//                } else {
//                    // set which color you want when edittext is focused
//                }
//            }});
    }


    @OnClick(R.id.link_signup)
    public void goToRegisterActivity() {
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_login)
    public void login (final View view){
        loginViewModel.login(view, this, userCredentials);
    }
//    public void login(final View view) {
//        final String username = userCredentials.get(0).getText().toString();
//        final String password = userCredentials.get(1).getText().toString();
//        final Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
//
//        // String URL = String.format(Constants.REQUEST_TOKEN_URL, username, password);
//
//
//        pDialog = new ProgressDialog(this);
//        pDialog.setTitle("Logging in ...");
//        pDialog.setMessage("Please wait");
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        String URL = Constants.BASE_URL + "/user/getByUsername/" + username;
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
//                            if (password.equals(response.get("password").toString())) {
//                                loginService.handleResponse(getApplicationContext(), session, response.get("email").toString(), response.get("password").toString());
//                                Log.e("==>>",response.get("email").toString());
//                            }
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
//                return headers;
//            }
//        };
//        requestQueue.add(jsonObjectRequest);
//    }

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

