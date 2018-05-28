package com.example.stefao.smsreader.utils;

/**
 * Created by stefao on 4/17/2018.
 */

public class Constants {
    public static final String BASE_URL = "https://rocky-wave-99733.herokuapp.com";
    public static final String GET_CATEGORIES_URL = "/user/getCategoriesByUsername/";
    public static final String GET_POIS_FOR_LOGGED_USER_URL = "/poi/getPoisForLoggedUser/";
    public static final String GET_TRANSACTIONS_URL = "/user/getTransactionsByCategoryOfUser/";
    public static final String BASE_SECURE_URL = "https://ancient-wildwood-65338.herokuapp.com/secure";
    private static final String CLIENT_CREDENTIALS = "android-oauth2-client-id:android-oauth2-client-pass";
    public static final String REQUEST_TOKEN_URL = BASE_URL + "/oauth/token?grant_type=password&username=%s&password=%s";
   // public static final String CLIENT_CREDENTIALS_ENCODED = Base64.encodeToString(CLIENT_CREDENTIALS.getBytes(), Base64.NO_WRAP);
    public static final String REQUEST_NEW_TOKEN = BASE_URL + "/oauth/token?grant_type=refresh_token&refresh_token=%s";
    public static final String REGISTER_USER_URL = BASE_URL + "/user/add";
    public static final String ADD_POI_URL = BASE_URL +"/poi/add";
    public static final String ADD_CATEGORY_URL = BASE_URL +"/user/addCategory";
    public static final String ADD_CATEGORY2_URL = BASE_URL +"/user/addCategory2";
    public static final String RECOMMEND_BY_FREQUENCY = BASE_URL +"/recommend/frequency";
    public static final String RECOMMEND_BY_RATING = BASE_URL +"/recommendPoiByRating";

    public static final String ADD_TRANSACTION_TO_USER_URL = BASE_URL +"/user/addTransaction";
    public static final String ADD_TRANSACTION_TO_POI_URL = BASE_URL +"/poi/addTransactionToPoi";
    public static final String ADD_POI_RATING_TO_POI_URL = BASE_URL +"/poi/addPoiRatingToPoi";
    public static final String GET_ALL_GALLERIES_URL = BASE_URL + "/gallery/get/all";

    //error messages
    public static final String NO_CONNECTION = "You are not connected to the internet!";
    public static final String WRONG_CREDENTIALS = "Wrong username or password!";
    public static final String SERVER_DOWN = "Could not connect. Please try again later";
    public static final String ERROR_TITLE = "Error";
    public static final String USERNAME_IN_USE = "This username is already in use!";
    public static final String EMAIL_IN_USE = "This email is already in use!";

    //user calls
    public static final String CHECK_USER = BASE_URL + "/user/checkUser";

    //email regex
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
}
