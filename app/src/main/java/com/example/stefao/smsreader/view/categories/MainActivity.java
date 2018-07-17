package com.example.stefao.smsreader.view.categories;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stefao.smsreader.view.feedback.FeedbackActivity;
import com.example.stefao.smsreader.view.recommend.RecommendActivity;
import com.example.stefao.smsreader.view.settings.SettingsActivity;
import com.example.stefao.smsreader.view.utils.MultiSpinner;
import com.example.stefao.smsreader.R;
import com.example.stefao.smsreader.view.transactions.TransactionsActivity;
import com.example.stefao.smsreader.view.utils.Utility;
import com.example.stefao.smsreader.model.CategoryDTO;
import com.example.stefao.smsreader.model.PoiDTO;
import com.example.stefao.smsreader.application.PersistService;
import com.example.stefao.smsreader.utils.location.LocationUtils;
import com.example.stefao.smsreader.utils.location.UserLocation;
import com.example.stefao.smsreader.utils.Constants;
import com.example.stefao.smsreader.utils.UserSessionManager;
import com.example.stefao.smsreader.utils.VolleyUtils;
import com.example.stefao.smsreader.viewmodel.categories.CategoriesViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.LocationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

import static com.example.stefao.smsreader.utils.UserSessionManager.KEY_EMAIL;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
    private int PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationManager;
    LocationUtils locationUtils;
    UserLocation userLocation;
    JSONArray categoriesResponse = new JSONArray();
    JSONArray poisResponse = new JSONArray();
    UserSessionManager userSessionManager;
    CategoryAdapter categoryAdapter;
    PoiAdapter poiAdapter;
    ListView listview;
    ListView poiListView;
    Context mContext;
    Activity mActivity;
    private PersistService persistService;
    SwipeRefreshLayout swipeRefreshLayout;
    SwipeRefreshLayout swipeRefreshLayoutNewCat;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.e("==>>>>>>>>",String.valueOf(bundle.getDouble("userLocationLongitude")));
        }
    };

    private MultiSpinner spinnerSelectInitialCategories;
    private ArrayAdapter<String> adapter;
    ProgressDialog pDialog;
    ProgressDialog pDialogFetch;
    //private Spinner spinner1, spinner2;
    //private Button btnSubmit;
    private Button setBudgetBtn;
    CategoriesViewModel categoriesViewModel;
    private android.support.design.widget.CoordinatorLayout backgroundColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        mActivity=this;
//        backgroundColor = (android.support.design.widget.CoordinatorLayout) findViewById(R.id.categories_layout);
//        backgroundColor.setBackgroundColor(getColorWithAlpha(Color.GREEN, 0.9f));
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_SMS_RECEIVE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        super.onCreate(savedInstanceState);
        //locationUtils = new LocationUtils(this, this);
        //userLocation = new UserLocation();
        categoriesViewModel = new CategoriesViewModel();

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.action_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setBackgroundDrawable(new ColorDrawable(Color.BLACK));






//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                Log.e("==>", messageText);
//                userLocation.setLocationSetted(false);
//                getLocation();
//            }
//        });
//
//        final Button button = findViewById(R.id.button_id);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                //getData(v);
//            }
//        });

        userSessionManager = new UserSessionManager(this);
        listview = (ListView) findViewById(R.id.categories_list);
        ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.category_header, listview,false);
        listview.addHeaderView(headerView);
        Log.e("==>",userSessionManager.getUserDetails().get(KEY_EMAIL));
        //getCategories(Constants.BASE_URL+Constants.GET_CATEGORIES_URL+userSessionManager.getUserDetails().get(KEY_EMAIL));

        Log.e("==>>>>",categoriesResponse.toString());
        ArrayList<CategoryDTO> categoriesArray = new Gson().fromJson(categoriesResponse.toString(), new TypeToken<List<CategoryDTO>>(){}.getType());
        categoryAdapter = new CategoryAdapter(this,categoriesArray);
        listview.setAdapter(categoryAdapter);
        categoriesViewModel.getCategories(Constants.BASE_URL+Constants.GET_CATEGORIES_URL+userSessionManager.getUserDetails().get(KEY_EMAIL),mContext,listview,spinnerSelectInitialCategories,categoryAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryDTO item = (CategoryDTO)parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext,TransactionsActivity.class);
                intent.putExtra("CategoryDTO",item);
                startActivity(intent);
            }
        });

//        poiListView = (ListView) findViewById(R.id.pois_list);
//        getPoisForLoggedUser(Constants.BASE_URL+Constants.GET_POIS_FOR_LOGGED_USER_URL+userSessionManager.getUserDetails().get(KEY_EMAIL));
//        Log.e("==>>>>",poisResponse.toString());
//        ArrayList<PoiDTO> poiArray = new Gson().fromJson(poisResponse.toString(), new TypeToken<List<PoiDTO>>(){}.getType());
//        poiAdapter = new PoiAdapter(this,poiArray);
//        poiListView.setAdapter(poiAdapter);
//        poiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                PoiDTO item = (PoiDTO)parent.getItemAtPosition(position);
//                Intent intent = new Intent(mContext,FeedbackActivity.class);
//                intent.putExtra("PoiDTO",item);
//                startActivity(intent);
//            }
//        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.add("cafe");
        adapter.add("books");
        adapter.add("restaurant");
        adapter.add("supermarket");
        adapter.add("nightclub");
        adapter.add("clothes");
        adapter.add("fuel");

        // get spinnerSelectInitialCategories and set adapter
        spinnerSelectInitialCategories = (MultiSpinner) findViewById(R.id.spinnerMulti);
        spinnerSelectInitialCategories.setVisibility(View.GONE);
        spinnerSelectInitialCategories.setAdapter(adapter, false, onSelectedListener);

        // set initial selection
        boolean[] selectedItems = new boolean[adapter.getCount()];
        selectedItems[1] = true; // select second item
        spinnerSelectInitialCategories.setSelected(selectedItems);


        swipeRefreshLayout = findViewById(R.id.swiperefreshcat);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        //Intent i = new Intent(getApplicationContext(), TransactionsActivity.class);
                        //startActivity(i);
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }
        );

//        swipeRefreshLayoutNewCat = findViewById(R.id.swiperefreshnewcat);
//        swipeRefreshLayoutNewCat.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//
//                        // This method performs the actual data-refresh operation.
//                        // The method calls setRefreshing(false) when it's finished.
//                        //Intent i = new Intent(getApplicationContext(), TransactionsActivity.class);
//                        //startActivity(i);
//                        Intent intent = getIntent();
//                        finish();
//                        startActivity(intent);
//                    }
//                }
//        );

        //addItemsOnSpinner2();
        //addListenerOnButton();
        //addListenerOnSpinnerItemSelection();
        ButterKnife.bind(this);
        //addListenerOnButtonSetBudget();

    }

    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

//    public void addListenerOnButtonSetBudget() {
//        setBudgetBtn = (Button) findViewById(R.id.button_setBudget);
//        setBudgetBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final int position = listview.getPositionForView((View) v.getParent());
//                Log.e("butonul clickuit", String.valueOf(position));
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setTitle("Insert an amount");
//
//// Set up the input
//                final EditText input = new EditText(mContext);
//// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//                input.setInputType(InputType.TYPE_CLASS_TEXT);
//                builder.setView(input);
//
//// Set up the buttons
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.e("budget",input.getText().toString());
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//                builder.show();
//            }
//
//        });
//
//    };

    public void clickedSetBudget(View view){
        Button setBudgetButton = (Button)view;
        final int position = listview.getPositionForView((View) view.getParent());
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Insert an amount");
        final CategoryDTO categoryDTO = (CategoryDTO)listview.getAdapter().getItem(position);

        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("budget",input.getText().toString());
                setCategoryBudget(categoryDTO.getId(),userSessionManager.getUserDetails().get(KEY_EMAIL),Float.parseFloat(input.getText().toString()));
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.parseColor("#1FDB84"));

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#1FDB84"));


        //builder.show();
    }


//    @OnClick(R.id.recommend_button)
//    public void displayCategoriesSpinner() {
//        //spinner1.setVisibility(View.VISIBLE);
//        //spinner2.setVisibility(View.VISIBLE);
//        addItemsOnSpinner2();
//        addListenerOnButton();
//        addListenerOnSpinnerItemSelection();
//    }

//    public void addItemsOnSpinner2() {
//
//        spinner2 = (Spinner) findViewById(R.id.spinner2);
//        List<String> list = new ArrayList<String>();
//        list.add("list 1");
//        list.add("list 2");
//        list.add("list 3");
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner2.setAdapter(dataAdapter);
//    }

//    public void addListenerOnSpinnerItemSelection() {
//        spinner1 = (Spinner) findViewById(R.id.spinner1);
//        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
//    }

    // get the selected dropdown list value
//    public void addListenerOnButton() {
//
//        spinner1 = (Spinner) findViewById(R.id.spinner1);
//        //spinner2 = (Spinner) findViewById(R.id.spinner2);
//        btnSubmit = (Button) findViewById(R.id.btnSubmit);
//
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(mActivity,
//                        "OnClickListener : " +
//                                "\nSpinner 1 : " + String.valueOf(spinner1.getSelectedItem()),
//                                //+
//                                //"\nSpinner 2 : " + String.valueOf(spinner2.getSelectedItem()),
//                        Toast.LENGTH_SHORT).show();
//
//                recommendByCategory(String.valueOf(spinner1.getSelectedItem()));
//                recommendByRating(String.valueOf(spinner1.getSelectedItem()));
//            }
//
//        });
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent= new Intent(this, PersistService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        registerReceiver(receiver,new IntentFilter(PersistService.AVAILABLE_DATA));
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        PersistService.MyBinder b = (PersistService.MyBinder) binder;
        persistService = b.getService();
//        Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        persistService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {

        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items

            StringBuilder builder = new StringBuilder();
            ArrayList<String> categories = new ArrayList<>();
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    builder.append(adapter.getItem(i)).append(" ");
                    categories.add(adapter.getItem(i));
                }
            }

            addCategories(categories,userSessionManager.getUserDetails().get(KEY_EMAIL),30);
            //getCategories(Constants.BASE_URL+Constants.GET_CATEGORIES_URL+userSessionManager.getUserDetails().get(KEY_EMAIL));
            categoriesViewModel.getCategories(Constants.BASE_URL+Constants.GET_CATEGORIES_URL+userSessionManager.getUserDetails().get(KEY_EMAIL),mContext,listview,spinnerSelectInitialCategories,categoryAdapter);
            Activity act = mActivity;
//            act.runOnUiThread(new Runnable(){
//                @Override
//                public void run() {
                    listview.setVisibility(View.VISIBLE);
                    spinnerSelectInitialCategories.setVisibility(View.GONE);
               // } });
            Toast.makeText(MainActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SMS_RECEIVE) {
            Log.i("TAG", "MY_PERMISSIONS_REQUEST_SMS_RECEIVE --> YES");
        }
    }

//    public void getLocation() {
//
////        Double latitude = locationUtils.getLocation().getLatitude();
////        Double longitude = locationUtils.getLocation().getLongitude();
////        Log.e("==>", latitude.toString());
////        Log.e("==>", longitude.toString());
////        ((TextView) findViewById(R.id.text_view_id)).setText(latitude.toString());
////        Toast.makeText(getApplicationContext(), latitude.toString(), Toast.LENGTH_LONG).show();
//
//
//        locationUtils.getLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                // Got last known location. In some rare situations this can be null.
//                if (location != null) {
//                    Double currentLatitude = location.getLatitude();
//                    Double currentLongitude = location.getLongitude();
//                    userLocation.setLatitude(location.getLatitude());
//                    userLocation.setLongitude(location.getLongitude());
//                    userLocation.setLocationSetted(true);
//                    Log.e("==>", currentLatitude.toString());
//                    Log.e("==>", currentLongitude.toString());
//                    Log.e("==>", userLocation.getLatitude() + "");
//                    String nominatimQuery = "https://nominatim.openstreetmap.org/reverse?email=so5olimpiu@yahoo.com&format=json&lat=" + userLocation.getLatitude() + "&lon=" + userLocation.getLongitude() + "&extratags=1&namedetails=1";
//                    getData(nominatimQuery);
//                    ((TextView) findViewById(R.id.text_view_id)).setText(currentLatitude.toString());
//                    Toast.makeText(getApplicationContext(), currentLatitude.toString(), Toast.LENGTH_LONG).show();
//                }
//            }
//        }).addOnFailureListener(this, new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("==>", "INAINTEEROARE");
//                e.printStackTrace();
//                Log.e("==>", "DUPAEROARE");
//            }
//        });
//        //return locationResult.getResult();
//    }
//
//    public void getData(String url) {
//        //String url = "https://rocky-wave-99733.herokuapp.com/demo";
//        Log.d("==>", "INAINTEEEEEE");
//        RequestQueue queue = Volley.newRequestQueue(this);
//        final Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type","application/json");
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        ((TextView) findViewById(R.id.text_view_id)).setText(response.toString());
//                        Log.e("==>", response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        ((TextView) findViewById(R.id.text_view_id)).setText("try again");
//                        Log.e("==>", "EROAREEEEEE");
//                    }
//                }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return headers;
//            }
//        };
//        // SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
//        queue.add(jsonObjectRequest);
//        Log.e("==>", "DUPAAA");
//    }

//    public void getCategories(String url) {
//        //String url = "https://rocky-wave-99733.herokuapp.com/demo";
//        Log.d("==>", "INAINTEEEEEE");
//        RequestQueue queue = Volley.newRequestQueue(this);
//        final Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type","application/json");
//
//        pDialogFetch = new ProgressDialog(this);
//        pDialogFetch.setTitle("Fetching your categories");
//        pDialogFetch.setMessage("Please wait...");
//        pDialogFetch.setCancelable(false);
//        pDialogFetch.show();
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//
//                    @Override
//                    public void onResponse(JSONArray response) {
//
//                        Log.e("==>", response.toString());
//                        categoriesResponse=response;
//                        ArrayList<CategoryDTO> categoriesArray = new Gson().fromJson(categoriesResponse.toString(), new TypeToken<List<CategoryDTO>>(){}.getType());
//                        if (categoriesArray.size()==0){
//                            listview.setVisibility(View.GONE);
//                            spinnerSelectInitialCategories.setVisibility(View.VISIBLE);
//                        }
//                        categoryAdapter.clear();
//                        categoryAdapter.addAll(categoriesArray);
//                        categoryAdapter.notifyDataSetChanged();
//                        Utility.setListViewHeightBasedOnChildren(listview);
//                        pDialogFetch.hide();
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        ((TextView) findViewById(R.id.text_view_id)).setText("try again");
//                        Log.e("==>", "EROAREEEEEE");
//                        pDialogFetch.hide();
//                    }
//                }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return headers;
//            }
//        };
//        // SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
//        queue.add(jsonArrayRequest);
//        Log.e("==>", "DUPAAA");
//    }

    public void addCategories(ArrayList<String> subcategories, String username, float categoryBudget) {

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Adding your first categories");
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        JSONArray arrayBody = new JSONArray();
        for (String subcategory : subcategories) {
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("subcategory", subcategory);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arrayBody.put(requestBody);
        }


        String URL = Constants.ADD_CATEGORY2_URL+"/"+username+"/"+categoryBudget;

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.POST,
                URL,
                arrayBody,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        {
                            Log.e("==>", response.toString());
                        }
                        pDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.NO_CONNECTION, getApplicationContext());
                        } else {
                            if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;
                                if (statusCode >= 500) {
                                    VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.SERVER_DOWN, getApplicationContext());
                                }
                            }
                        }
                        pDialog.hide();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        requestQueue.add(request);
    }

    public void setCategoryBudget(Long categoryId, String username, float categoryBudget) {

        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String URL = Constants.SET_CATEGORY_BUDGET_URL+"/"+categoryBudget+"/"+categoryId+"/"+username;

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        {
                            Log.e("==>", response.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.NO_CONNECTION, getApplicationContext());
                        } else {
                            if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;
                                if (statusCode >= 500) {
                                    VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.SERVER_DOWN, getApplicationContext());
                                }
                            }
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        requestQueue.add(request);
    }


//    public void recommendByCategory(String subcategory) {
//
//        final Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
//
//
////        final JSONObject requestBody = new JSONObject();
////
////        try {
////            requestBody.put("subcategory", subcategory);
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
//
//
//        String URL = Constants.RECOMMEND_BY_FREQUENCY+"/"+subcategory;
//
//        final RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.GET,
//                URL,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        {
//                            Toast.makeText(mActivity,
//                                    "poiul recomandat este"+response,
//                                    Toast.LENGTH_SHORT).show();
//                            Log.e("====>",response.toString());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (error instanceof NoConnectionError) {
//                            VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.NO_CONNECTION, getApplicationContext());
//                        } else {
//                            if (error.networkResponse != null) {
//                                int statusCode = error.networkResponse.statusCode;
//                                if (statusCode >= 500) {
//                                    VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.SERVER_DOWN, getApplicationContext());
//                                }
//                            }
//                        }
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return headers;
//            }
//        };
//        requestQueue.add(request);
//    }
//
//    public void recommendByRating(String subcategory) {
//
//        final Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
//
//
////        final JSONObject requestBody = new JSONObject();
////
////        try {
////            requestBody.put("subcategory", subcategory);
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
//
//
//        String URL = Constants.RECOMMEND_BY_RATING+"/"+subcategory;
//
//        final RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.GET,
//                URL,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        {
//                            Toast.makeText(mActivity,
//                                    "poiul recomandat este"+response,
//                                    Toast.LENGTH_SHORT).show();
//                            Log.e("rating",response.toString());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (error instanceof NoConnectionError) {
//                            VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.NO_CONNECTION, getApplicationContext());
//                        } else {
//                            if (error.networkResponse != null) {
//                                int statusCode = error.networkResponse.statusCode;
//                                if (statusCode >= 500) {
//                                    VolleyUtils.buildAlertDialog(Constants.ERROR_TITLE, Constants.SERVER_DOWN, getApplicationContext());
//                                }
//                            }
//                        }
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return headers;
//            }
//        };
//        requestQueue.add(request);
//    }


    public void getPoisForLoggedUser(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");

//        pDialogFetch = new ProgressDialog(this);
//        pDialogFetch.setTitle("Fetching your categories");
//        pDialogFetch.setMessage("Please wait...");
//        pDialogFetch.setCancelable(false);
//        pDialogFetch.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("==>", response.toString());
                        poisResponse=response;
                        ArrayList<PoiDTO> poisArray = new Gson().fromJson(poisResponse.toString(), new TypeToken<List<PoiDTO>>(){}.getType());
                        if (poisArray.size()==0){
                            poiListView.setVisibility(View.GONE);
                        }
                        poiAdapter.clear();
                        poiAdapter.addAll(poisArray);
                        poiAdapter.notifyDataSetChanged();
                        Utility.setListViewHeightBasedOnChildren(poiListView);
                        //pDialogFetch.hide();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
//                        ((TextView) findViewById(R.id.text_view_id)).setText("try again");
                        Log.e("==>", "EROAREEEEEE");
                        //pDialogFetch.hide();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        // SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
        queue.add(jsonArrayRequest);
        Log.e("==>", "DUPAAA");
    }

}
