package styles.zonetech.net.styles.server.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.server.Helpers.EditTextValidator;
import styles.zonetech.net.styles.server.Helpers.Parser;
import styles.zonetech.net.styles.server.Helpers.VideoLoader;
import styles.zonetech.net.styles.server.Models.User;
import styles.zonetech.net.styles.server.R;
import styles.zonetech.net.styles.server.Server.IServer;
import styles.zonetech.net.styles.server.Utils.Common;
import styles.zonetech.net.styles.server.Utils.CommonMethods;

import static styles.zonetech.net.styles.server.Helpers.Fonts.SELECT_LANGUAGE;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
CommonMethods commonMethods;
    TextView toolbarTitleTxt,forgotPasswordTxt,personIcon;
    EditText passwordEditTxt,userNameEditTxt;
    Button loginBtn,createAccountBtn;
    Context mContext=LoginActivity.this;
    ConstraintLayout rootSnack;
    IServer server;
    FrameLayout loaderLayout;
    VideoLoader videoLoader;
    EditTextValidator validator;
    Parser parser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        commonMethods=new CommonMethods(mContext);
        server= Common.getAPI();
        parser=new Parser(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        validator=new EditTextValidator(mContext);
        initViews();
        setListeners();
        videoLoader=new VideoLoader(mContext,loaderLayout);


    }

    private void setupLanguage() {
        Log.d(TAG, "setupLanguage: ");
        Locale language= Resources.getSystem().getConfiguration().locale;
        SharedPreferences languagePref =getSharedPreferences(getPackageName(),0);

        String lang=languagePref.getString(SELECT_LANGUAGE,language.getLanguage());
        Locale locale=null;

        if(lang.equals("ar")){
            Log.d(TAG, "setupLanguage: "+lang);
            locale=new Locale("ar");
        }
        else if(lang.equals("en")){
            Log.d(TAG, "setupLanguage: "+lang);
            locale=new Locale("en");
        }

        if(locale!=null){
            Log.d(TAG, "setupLanguage: local is not null");
            getResources().getConfiguration().locale = locale;
            getResources().getConfiguration().setLocale(locale);
            getResources().updateConfiguration(getResources().getConfiguration()
                    , getResources().getDisplayMetrics());
        }


    }
    private boolean isUserLogged(){
        boolean isLogged=false;
        Gson gson = new Gson();
        SharedPreferences userPref=getSharedPreferences(getPackageName(),0);;
        String userJson = userPref.getString(Common.CURRENT_USER, null);
        askPermission();
        if(userJson!=null){
            Common.currentUser = gson.fromJson(userJson, User.class);
            isLogged=true;
        }
        else {
            isLogged=false;
        }
        return isLogged;

    }
    private void initViews() {
        personIcon=findViewById(R.id.personIcon);
        loaderLayout=findViewById(R.id.loaderLayout);
        rootSnack=findViewById(R.id.rootSnack);
        if(isUserLogged()){
            Intent  intent=new Intent(mContext,OrdersActivity.class);
            startActivity(intent);
            finish();
        }

        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        forgotPasswordTxt=findViewById(R.id.forgotPasswordTxt);
        toolbarTitleTxt.setText(getString(R.string.login_toolbar));
        userNameEditTxt=findViewById(R.id.userNameEditTxt);
        passwordEditTxt=findViewById(R.id.passwordEditTxt);
        loginBtn=findViewById(R.id.createAccountBtn);

    }
    private void setListeners() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token= FirebaseInstanceId.getInstance().getToken();
                Log.d(TAG, "onClick: "+token);
                //validate Edit texts are not empty
                if (validator.validate(passwordEditTxt) && validator.validate(userNameEditTxt)) {
                    String username = userNameEditTxt.getText().toString();
                    String password = passwordEditTxt.getText().toString();
                    //login in
                    login("android", token, username, password);
                } else {
                    //show toast
                    validator.ShowToast(getString(R.string.validation_string));
                }
            }
        });



        forgotPasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });


    }
    private void login(String android, String token, String username, final String password) {
        videoLoader.load();
        server.login(android,token,username,password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                videoLoader.stop();
                if(response.body()!=null){
                    Log.d(TAG, "onResponse: "+response.body());
                    parser.parse(response.body());
                    if(parser.getStatus().equals("success")){
                        Intent intent=new Intent(mContext,OrdersActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else{
                        validator.ShowToast(parser.getCodeMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
             videoLoader.stop();
             validator.showSnackbar(rootSnack,false,"");
            }
        });
    }

    private void askPermission(){
        Log.d(TAG, "askPermission: ");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        } }


}
