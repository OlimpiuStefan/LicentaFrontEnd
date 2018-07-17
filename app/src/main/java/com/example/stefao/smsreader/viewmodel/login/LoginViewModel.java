package com.example.stefao.smsreader.viewmodel.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.LoginService;
import com.example.stefao.smsreader.utils.UserSessionManager;
import com.example.stefao.smsreader.utils.VolleyUtils;
import com.example.stefao.smsreader.view.login.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.OnClick;

/**
 * Created by stefao on 6/8/2018.
 */

public class LoginViewModel {

    ProgressDialog pDialog;
    UserSessionManager session;

    public LoginViewModel(){};

    public void login(final View view, final Context context, List<EditText> userCredentials) {
        session = new UserSessionManager(context);
        final String username = userCredentials.get(0).getText().toString();
        final String password = userCredentials.get(1).getText().toString();
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // String URL = String.format(Constants.REQUEST_TOKEN_URL, username, password);


        pDialog = new ProgressDialog(context);
        pDialog.setTitle("Logging in ...");
        pDialog.setMessage("Please wait");
        pDialog.setCancelable(false);
        pDialog.show();

        String URL = Constants.BASE_URL + "/user/getByUsername/" + username;

        final RequestQueue requestQueue = Volley.newRequestQueue(context);

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
                                loginService.handleResponse(context, session, response.get("email").toString(), response.get("password").toString());
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(100),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void doAutoLogin(String email, String password, final Context context, List<EditText> userCredentials) {
        session = new UserSessionManager(context);
        final String username = userCredentials.get(0).getText().toString();
        //final String password = userCredentials.get(1).getText().toString();
        String URL = Constants.BASE_URL + "/user/getByUsername/" + email;

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        //headers.put("Authorization", "Basic " + Constants.CLIENT_CREDENTIALS_ENCODED);

        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        LoginService loginService = new LoginService();
                        try {
                            loginService.handleResponse(context, session, response.get("email").toString(), response.get("password").toString());
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

    public void register(final Context context, final List<EditText> userCredentials) {
        session = new UserSessionManager(context);
        String email = userCredentials.get(0).getText().toString();
        String password = userCredentials.get(1).getText().toString();
        Float totalBudget = Float.parseFloat(userCredentials.get(2).getText().toString());
        Log.e("==>", totalBudget.toString());
        Log.e("==>", totalBudget.getClass().toString());

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        final JSONObject user = new JSONObject();

        try {
            user.put("email", email);
            user.put("password", password);
            user.put("totalBudget", totalBudget);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonRequest = user;

        JSONObject body = new JSONObject();

        try {
            body.put("email", jsonRequest.get("email"));
            body.put("password", jsonRequest.get("password"));
            body.put("totalBudget", jsonRequest.get("totalBudget"));
            Log.e("==>", jsonRequest.get("totalBudget").toString());
            Log.e("==>", jsonRequest.get("totalBudget").getClass().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        pDialog = new ProgressDialog(context);

        pDialog.setMessage("Registering...");
        pDialog.setCancelable(false);
        pDialog.show();

        String URL = Constants.REGISTER_USER_URL;

        final RequestQueue requestQueue = Volley.newRequestQueue(context);

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
                                loginService.handleResponse(context, session, response.get("email").toString(), response.get("password").toString());
                                doAutoLogin(response.get("email").toString(), response.get("password").toString(),context,userCredentials);
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
                            VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.NO_CONNECTION, context);
                        } else {
                            if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;
                                if (statusCode >= 500) {
                                    VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.SERVER_DOWN, context);
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
