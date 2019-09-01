package styles.zonetech.net.styles.server.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import styles.zonetech.net.styles.server.Activities.LoginActivity;
import styles.zonetech.net.styles.server.Adapters.OrderDetailsAdapter;
import styles.zonetech.net.styles.server.Helpers.EditTextValidator;
import styles.zonetech.net.styles.server.Interfaces.OnSendBtnClicked;
import styles.zonetech.net.styles.server.Interfaces.OnYesBtnClicked;
import styles.zonetech.net.styles.server.Models.OrderDeatil;
import styles.zonetech.net.styles.server.R;
import styles.zonetech.net.styles.server.Utils.Common;
import styles.zonetech.net.styles.server.Utils.CommonMethods;
import styles.zonetech.net.styles.server.Utils.ItemDecoration;

public class ListDialog extends Dialog {
    ArrayList<OrderDeatil> orderDeatils;

    public void setOrderDeatils(ArrayList<OrderDeatil> orderDeatils) {
        this.orderDeatils = orderDeatils;
    }

    Context mContext;
TextView dialogNameTxt,closeBtn,sendBtn,message;
Button yesBtn,noBtn;
CommonMethods commonMethods;
RecyclerView dialogList;
ConstraintLayout commentView,cancelDialogView;
EditText messageEditTxt;
int givenLayout;
OnYesBtnClicked onYesBtnClicked;
OnSendBtnClicked onSendBtnClicked;
EditTextValidator validator;
RelativeLayout root;
    public void setOnSendBtnClicked(OnSendBtnClicked onSendBtnClicked) {
        this.onSendBtnClicked = onSendBtnClicked;
    }

    public void setOnYesBtnClicked(OnYesBtnClicked onYesBtnClicked) {
        this.onYesBtnClicked = onYesBtnClicked;
    }


    public ListDialog(Context context,int givenLayout) {
        super(context);
        mContext=context;
        validator=new EditTextValidator(mContext);
        this.givenLayout=givenLayout;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.list_dialog_layout);
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initViews();


    }

    private void initViews() {
        closeBtn=findViewById(R.id.closeBtn);
        dialogNameTxt=findViewById(R.id.dialogNameTxt);
        dialogList=findViewById(R.id.dialogList);
        commentView=findViewById(R.id.commentView);
        sendBtn=commentView.findViewById(R.id.sendBtn);
        messageEditTxt=commentView.findViewById(R.id.messageEditTxt);
        cancelDialogView=findViewById(R.id.cancelDialogView);
        message=cancelDialogView.findViewById(R.id.message);
        yesBtn=cancelDialogView.findViewById(R.id.yesBtn);
        noBtn=cancelDialogView.findViewById(R.id.noBtn);
        root=findViewById(R.id.root);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        switch (givenLayout){
            case Common.FINISH_ORDER_LAYOUT:

                cancelDialogView.setVisibility(View.VISIBLE);
                dialogNameTxt.setText(mContext.getString(R.string.finsih_order));
                message.setText(mContext.getString(R.string.order_finishing));

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onYesBtnClicked.onYesBtnClicked();

                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                break;

            case Common.Approve_ORDER_LAYOUT:
                cancelDialogView.setVisibility(View.VISIBLE);
                dialogNameTxt.setText(mContext.getString(R.string.approve_order));
                message.setText(mContext.getString(R.string.order_approval));

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onYesBtnClicked.onYesBtnClicked();

                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                break;

            case Common.ORDER_DETAIL_LAYOUT:
                dialogList.setVisibility(View.VISIBLE);
                cancelDialogView.setVisibility(View.GONE);
                dialogNameTxt.setText(mContext.getString(R.string.order_details));
                if(orderDeatils.size()>1){
                    root.setBackground(ContextCompat.getDrawable(mContext,R.drawable.rounded_corners_dialog_back_with_padding));
                }
                ItemDecoration itemDecoration=new ItemDecoration(mContext);
                dialogList.addItemDecoration(itemDecoration);
                if(orderDeatils.size()<=Common.DIALOG_LIST_DEFAULT_SIZE){
                    ViewGroup.LayoutParams params=dialogList.getLayoutParams();
                    params.height= WindowManager.LayoutParams.WRAP_CONTENT;
                    dialogList.setLayoutParams(params);
                }
                populateList();
                break;
            case Common.REJECT_ORDER_LAYOUT:
                cancelDialogView.setVisibility(View.GONE);
                commentView.setVisibility(View.VISIBLE);
                dialogNameTxt.setText(mContext.getString(R.string.reject_order));
                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(validator.validate(messageEditTxt)){
                            onSendBtnClicked.onSendBtnClicked(messageEditTxt.getText().toString());
                        }

                        else{
                            validator.ShowToast(mContext.getString(R.string.validation_string));
                        }
                    }
                });
                break;


            case Common.DIALOG_LAYOUT_LOGOUT:
                dialogNameTxt.setText(mContext.getString(R.string.logout));
                dialogList.setVisibility(View.GONE);
                cancelDialogView.setVisibility(View.VISIBLE);
                yesBtn.setText(mContext.getString(R.string.logout));
                noBtn.setText(mContext.getString(R.string.cancel));
                message.setText(mContext.getString(R.string.confirm_logout));
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //delete saved user
                        SharedPreferences userPref=mContext.getSharedPreferences(mContext.getPackageName(),0);
                        SharedPreferences.Editor editor=userPref.edit();
                        editor.putString(Common.CURRENT_USER,null);
                        editor.apply();
                        Common.currentUser=null;
                        Intent intent=new Intent(mContext, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(intent);


                        dismiss();

                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                break;

        }





    }

    private void populateList() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
        dialogList.setLayoutManager(layoutManager);
        OrderDetailsAdapter adapter=new OrderDetailsAdapter(mContext,orderDeatils);
        dialogList.setAdapter(adapter);
    }


}
