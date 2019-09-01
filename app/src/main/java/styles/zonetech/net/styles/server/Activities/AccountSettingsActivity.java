package styles.zonetech.net.styles.server.Activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.server.Dialogs.ListDialog;
import styles.zonetech.net.styles.server.Helpers.EditTextValidator;
import styles.zonetech.net.styles.server.Helpers.Parser;
import styles.zonetech.net.styles.server.Helpers.VideoLoader;
import styles.zonetech.net.styles.server.Models.User;
import styles.zonetech.net.styles.server.R;
import styles.zonetech.net.styles.server.Server.IServer;
import styles.zonetech.net.styles.server.Utils.Common;
import styles.zonetech.net.styles.server.Utils.CommonMethods;

public class AccountSettingsActivity extends AppCompatActivity {
    private static final String SELECT_LANGUAGE="Locale.Helper.Selected.Language";
    Button menuBtn,backBtn,saveBtn,logoutBtn,copyLinkBtn;
    TextView toolbarTitleTxt;
    SwitchCompat languageSwitch;
    boolean Arabic;
    private CommonMethods commonMethods;
    EditText nameEditTxt,userNameEditTxt,mobileEditTxt,passwordEditTxt;
    private EditTextValidator validator;
    IServer server;
    FrameLayout loaderLayout;
    VideoLoader videoLoader;
    Parser parser;
    int givenLayoutType;
    ConstraintLayout rootSnack;
    Context mContext=AccountSettingsActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        parser=new Parser(mContext);
        server= Common.getAPI();
        validator=new EditTextValidator(mContext);
        initViews();
        videoLoader=new VideoLoader(mContext,loaderLayout);
        setListeners();
        initUserData();
    }


    private void initUserData() {

        User currentUser=Common.currentUser;
        if(currentUser!=null){
            userNameEditTxt.setText(currentUser.getEmail());
            nameEditTxt.setText(currentUser.getUserName());
            mobileEditTxt.setText(currentUser.getPhone());
            passwordEditTxt.setText(currentUser.getPassword());

        }

    }




    private void initViews() {
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.account));
        languageSwitch=findViewById(R.id.languageSwitch);
        saveBtn=findViewById(R.id.saveBtn);
        nameEditTxt=findViewById(R.id.nameEditTxt);
        userNameEditTxt=findViewById(R.id.userNameEditTxt);
        mobileEditTxt=findViewById(R.id.mobileEditTxt);
        passwordEditTxt=findViewById(R.id.passwordEditTxt);
        loaderLayout=findViewById(R.id.loaderLayout);
        logoutBtn=findViewById(R.id.logoutBtn);
        Locale language= Resources.getSystem().getConfiguration().locale;
        SharedPreferences languagePref =getSharedPreferences(getPackageName(),0);
        String lang=languagePref.getString(SELECT_LANGUAGE,language.getLanguage());
        rootSnack=findViewById(R.id.rootSnack);
        copyLinkBtn=findViewById(R.id.copyLinkBtn);
        if(lang.equals("ar")){
            Arabic=true;
        }
    }


    private void setListeners() {

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonMethods.showMenu();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //save arabic language for app
                    if(!Arabic){
                        SharedPreferences languagePref =getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=languagePref.edit();
                        editor.putString(SELECT_LANGUAGE,"ar");
                        editor.apply();
                        mStartActivity(mContext,new LoginActivity());
                    }



                    else if(Arabic){
                        SharedPreferences languagePref =getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=languagePref.edit();
                        editor.putString(SELECT_LANGUAGE,"en");
                        editor.apply();
                        mStartActivity(mContext,new LoginActivity());
                    }

                }


            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validator.validate(passwordEditTxt)
                        &&validator.validate(userNameEditTxt)
                        &&validator.validate(mobileEditTxt)
                        &&validator.validate(nameEditTxt)){

                    //edit account
                    String userName=nameEditTxt.getText().toString();
                    String useremail=userNameEditTxt.getText().toString();
                    String userphone=mobileEditTxt.getText().toString();
                    String password=passwordEditTxt.getText().toString();
                        editAccount(userName,useremail,userphone,password);

                }
                else{
                    //show toast
                    validator.ShowToast(getString(R.string.validation_string));
                }
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                givenLayoutType=Common.DIALOG_LAYOUT_LOGOUT;
                ListDialog listsDialog=new ListDialog(mContext,givenLayoutType);
                listsDialog.show();
            }
        });

        copyLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //copy link to clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", Common.CONTROL_PANEL);
                clipboard.setPrimaryClip(clip);

                validator.ShowToast(getString(R.string.copied_link));
            }
        });

    }


    private void editAccount(final String userName, final String useremail, final String userphone, final String password) {

        videoLoader.load();
        final String CurrentId= String.valueOf(Common.currentUser.getId());
        server.editAccount(CurrentId,userName,useremail,userphone,password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                videoLoader.stop();
                if(response.body()!=null){
                    parser.parse(response.body());
                    if(parser.getStatus().equals("success")){
                        Common.currentUser=new User();
                        Common.currentUser.setId(CurrentId);
                        Common.currentUser.setEmail(useremail);
                        Common.currentUser.setPhone(userphone);
                        Common.currentUser.setSaloonEnName(userName);
                        Common.currentUser.setSaloonArName(userName);
                        Common.currentUser.setPassword(password);
                        validator.ShowToast(getString(R.string.updateAccoutText));
                    }
                    else if(parser.getStatus().equals("error")){
                        validator.ShowToast(parser.getCodeMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                videoLoader.stop();
            }
        });

    }

    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}
