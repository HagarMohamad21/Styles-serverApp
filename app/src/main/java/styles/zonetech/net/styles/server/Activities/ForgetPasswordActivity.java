package styles.zonetech.net.styles.server.Activities;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.server.Helpers.EditTextValidator;
import styles.zonetech.net.styles.server.Helpers.Fonts;
import styles.zonetech.net.styles.server.Helpers.Parser;
import styles.zonetech.net.styles.server.Helpers.VideoLoader;
import styles.zonetech.net.styles.server.R;
import styles.zonetech.net.styles.server.Server.IServer;
import styles.zonetech.net.styles.server.Utils.Common;
import styles.zonetech.net.styles.server.Utils.CommonMethods;

public class ForgetPasswordActivity extends AppCompatActivity {
    TextView toolbarTitleTxt;
    Button menuBtn,backBtn,sendBtn;
    EditText userNameEditTxt;
    private Context mContext=ForgetPasswordActivity.this;
    private CommonMethods commonMethods;
    EditTextValidator validator;
    ConstraintLayout rootSnack;
    IServer server;
    String language;
    Parser parser;
    VideoLoader videoLoader;
    FrameLayout loaderLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        server= Common.getAPI();
        validator=new EditTextValidator(mContext);
        parser=new Parser(mContext);
        videoLoader=new VideoLoader(mContext,loaderLayout);
        setListeners();
        if(Fonts.isArabic){
            language="ar";

        }
        else{
            language="en";
        }
    }


        private void initView() {
            toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
            toolbarTitleTxt.setText(getString(R.string.recovery));
            userNameEditTxt=findViewById(R.id.userNameEditTxt);
            sendBtn=findViewById(R.id.sendBtn);
            rootSnack=findViewById(R.id.rootSnack);
            loaderLayout=findViewById(R.id.loaderLayout);
        }


    private void setListeners() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validator.validate(userNameEditTxt)){
                    videoLoader.load();
                    server.recoverPassword(userNameEditTxt.getText().toString(),language).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            parser.parse(response.body());
                            if(parser.getStatus().equals("success")){
                                if(parser.getAction().equals("recovertext")){
                                    videoLoader.stop();
                                    validator.ShowToast(getString(R.string.email_recovery));
                                    finish();
                                }
                            }
                          else {
                              videoLoader.stop();
                              validator.ShowToast(parser.getCodeMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            videoLoader.stop();
                            validator.showSnackbar(rootSnack,false,"");
                        }
                    });

                }
                else{
                    validator.ShowToast(getString(R.string.validation_string));
                }
            }
        });
    }
}
