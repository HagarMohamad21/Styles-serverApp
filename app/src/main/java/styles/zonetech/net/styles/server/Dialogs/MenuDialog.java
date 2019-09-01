package styles.zonetech.net.styles.server.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import java.util.ArrayList;

import styles.zonetech.net.styles.server.Activities.AboutActivity;
import styles.zonetech.net.styles.server.Activities.AccountSettingsActivity;
import styles.zonetech.net.styles.server.Activities.OrdersActivity;
import styles.zonetech.net.styles.server.Adapters.MenuAdapter;
import styles.zonetech.net.styles.server.Interfaces.DialogListener;
import styles.zonetech.net.styles.server.Models.MenuModel;
import styles.zonetech.net.styles.server.R;
import styles.zonetech.net.styles.server.Utils.ItemDecoration;

import static styles.zonetech.net.styles.server.Utils.Common.MenuItemAbout;
import static styles.zonetech.net.styles.server.Utils.Common.MenuItemAccount;
import static styles.zonetech.net.styles.server.Utils.Common.MenuItemHome;

public class MenuDialog extends Dialog implements DialogListener {

    Context context;
RecyclerView menuList;

    private static final int  DIALOG_LAYOUT_TYPE_MENU=10;
    private static int givenLayoutType;
ArrayList<MenuModel>list=new ArrayList<>();
    public MenuDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu_layout);
        initViews();
       initMenuList();
        getWindow().getAttributes().windowAnimations = R.style.dialogsAnimationStyle;
         givenLayoutType=DIALOG_LAYOUT_TYPE_MENU;
         setupRecyclerView(list);

    }


    private void initViews() {
        menuList=findViewById(R.id.menuList);
 }



    private void initMenuList(){
        MenuModel itemOne=new MenuModel(context.getString(R.string.home),context.getString(R.string.homeIcon));
        MenuModel itemThree=new MenuModel(context.getString(R.string.account),context.getString(R.string.personIcon));
        MenuModel itemFive=new MenuModel(context.getString(R.string.about),context.getString(R.string.infoIcon));
      list.add(itemOne);list.add(itemThree);list.add(itemFive);
    }




    private void setupRecyclerView(ArrayList<MenuModel> list) {
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        ItemDecoration itemDecoration=new ItemDecoration(context);
        MenuAdapter adapter;
        adapter=new MenuAdapter(context,list);
        adapter.setDialogListener(this);
        menuList.setAdapter(adapter);
        menuList.setLayoutManager(layoutManager);
        menuList.addItemDecoration(itemDecoration);


    }

    @Override
    public void onClickRow(int position) {
      //open corresponding activity

       switch (position){

           case MenuItemHome:
               if(!(context instanceof OrdersActivity)){
                   mStartActivity(context,new OrdersActivity());
                   dismiss();}

               else {dismiss();}

               break;

           case MenuItemAccount:
                   if(!(context instanceof AccountSettingsActivity)){
                       mStartActivity(context,new AccountSettingsActivity());
                       dismiss();}

                   else {
                       dismiss();
                   }

               break;

           case MenuItemAbout:
               if(!(context instanceof AboutActivity)){
                   mStartActivity(context,new AboutActivity());
                   dismiss();}
               else {
                   dismiss();
               }
               break;

       }
    }



    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        context.startActivity(intent);
    }


}
