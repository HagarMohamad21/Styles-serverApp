package styles.zonetech.net.styles.server.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.server.Adapters.OrdersAdapter;
import styles.zonetech.net.styles.server.Helpers.EditTextValidator;
import styles.zonetech.net.styles.server.Helpers.EmptyListInitiator;
import styles.zonetech.net.styles.server.Helpers.Fonts;
import styles.zonetech.net.styles.server.Helpers.Parser;
import styles.zonetech.net.styles.server.Helpers.VideoLoader;
import styles.zonetech.net.styles.server.Interfaces.OnListEmpty;
import styles.zonetech.net.styles.server.Models.Order;
import styles.zonetech.net.styles.server.Models.User;
import styles.zonetech.net.styles.server.R;
import styles.zonetech.net.styles.server.Server.IServer;
import styles.zonetech.net.styles.server.Utils.Common;
import styles.zonetech.net.styles.server.Utils.CommonMethods;

public class OrdersActivity extends AppCompatActivity implements OnListEmpty {
   private ArrayList<Order>orders=new ArrayList<>();
    private Context mContext=OrdersActivity.this;
    TextView toolbarTitleTxt;
    private CommonMethods commonMethods;
    LinearLayout linearRoot;
    EmptyListInitiator emptyListInitiator;
    IServer server;
    Parser parser;
    FrameLayout loaderLayout;
    VideoLoader videoLoader;
    ConstraintLayout rootSnack;
    private EditTextValidator validator;
    RecyclerView ordersList;
    private static final String TAG = "OrdersActivity";
     Button menuBtn,backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        if (Common.currentUser==null)
            isUserLogged();
        initViews();
        videoLoader=new VideoLoader(mContext,loaderLayout);
        validator=new EditTextValidator(mContext);
        server= Common.getAPI();
        parser=new Parser(mContext);
        getOrders();
        setListeners();
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
    }

    private void getOrders() {
        videoLoader.load();
        server.orders(Common.currentUser.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                videoLoader.stop();
                if (response.body()!=null){
                    parser.parse(response.body());
                    if(parser.getStatus().equals("success")){
                        orders=parser.getOrders();
                        Log.d(TAG, "onResponse: "+response.body());
                        if(orders.size()==0){
                            emptyListInitiator.setMessage(mContext.getString(R.string.no_orders));
                        }

                        else {
                            populateList();
                        }

                    }
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
              videoLoader.stop();
              validator.showSnackbar(rootSnack,false,"");
            }
        });
    }

    private void populateList() {
        OrdersAdapter adapter=new OrdersAdapter(orders,mContext);
        adapter.setOnListEmpty(this);
        ordersList.setLayoutManager(new GridLayoutManager(mContext,1));
        ordersList.setAdapter(adapter);

    }

    private void initViews() {
        ordersList=findViewById(R.id.ordersList);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        if (Fonts.isArabic)
        toolbarTitleTxt.setText(Common.currentUser.getSaloonArName());
        else
            toolbarTitleTxt.setText(Common.currentUser.getSaloonEnName());
        linearRoot=findViewById(R.id.linearRoot);
        emptyListInitiator=new EmptyListInitiator(linearRoot);
        loaderLayout=findViewById(R.id.loaderLayout);
        rootSnack=findViewById(R.id.root);


    }

    @Override
    public void onListEmpty() {
        emptyListInitiator.setMessage(mContext.getString(R.string.no_orders));
    }

    private boolean isUserLogged(){
        boolean isLogged=false;
        Gson gson = new Gson();
        SharedPreferences userPref=getSharedPreferences(getPackageName(),0);;
        String userJson = userPref.getString(Common.CURRENT_USER, null);
        if(userJson!=null){
            Common.currentUser = gson.fromJson(userJson, User.class);
            isLogged=true;
        }
        else {
            isLogged=false;
        }
        return isLogged;

    }

}
