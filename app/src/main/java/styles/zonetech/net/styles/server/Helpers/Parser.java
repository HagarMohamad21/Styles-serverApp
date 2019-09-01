package styles.zonetech.net.styles.server.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import styles.zonetech.net.styles.server.Models.Order;
import styles.zonetech.net.styles.server.Models.User;
import styles.zonetech.net.styles.server.R;
import styles.zonetech.net.styles.server.Utils.Common;

public class Parser  {

    public boolean MessageSent=false;
    String status,action,codeMessage;
    Context mContext;
    int code=-1;
    String[] errorcodes;
ArrayList<Order> orders=new ArrayList<>();

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public Parser(Context mContext) {
        this.mContext = mContext;
        initErrorsArray();
    }
    private void initErrorsArray() {
        Resources res = mContext.getResources();
        errorcodes = res.getStringArray(R.array.errorcodes);
    }

    public void parse(String response){

        try {
            JSONArray jsonArray=new JSONArray(response);
            JSONObject jsonObject=jsonArray.getJSONObject(0);

            status=jsonObject.getString("status");
            if(status.equals("error")){
                code=jsonObject.getInt("code");
            }
            else if(status.equals("success")){
                action=jsonObject.getString("action");
                switch (action){


                    case "logintext":
                        JSONArray modelArray=jsonArray.getJSONArray(1);
                        JSONObject userObject=modelArray.getJSONObject(0);
                        getUser(userObject);
                        break;
                        
                    case "orderstext":
                        modelArray=jsonArray.getJSONArray(1);
                        getOrders(modelArray);
                        break;

                    case "recovertext":
                        action="recovertext";
                        break;


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getOrders(JSONArray modelArray) {

        JSONObject orderJson;
        for (int i=0;i<modelArray.length();i++){
            try {
                Order order=new Order();
                orderJson=modelArray.getJSONObject(i);

                order.setOrderId(orderJson.getString("id"));
                order.setCoupon(orderJson.getString("coupon"));
                order.setOrderSaloonId(orderJson.getString("saloonid"));
                order.setOrderStatus(orderJson.getString("status"));
                order.setOrderSaloonArName(orderJson.getString("arname"));
                order.setOrderSaloonEnName(orderJson.getString("enname"));
                order.setOrderTotal(orderJson.getInt("total"));
                order.setOrderSchedule(orderJson.getString("schedule"));
                order.setOrderAddress(orderJson.getString("address"));
                order.setOrderItems(orderJson.getString("items"));
                orders.add(order);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void saveUser() {
        SharedPreferences userPerf= mContext.getSharedPreferences(mContext.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=userPerf.edit();
        Gson gson = new Gson();
        if(Common.currentUser!=null){
            String user = gson.toJson(Common.currentUser);
            editor.putString(Common.CURRENT_USER,user);
            editor.apply();

        }
        else
        {

        }
    }

    private void getUser(JSONObject userObject) {
        User user=new User();
        try {
            user.setEmail(userObject.getString("useremail"));
            user.setId(userObject.getString("id"));
            user.setUserName(userObject.getString("username"));
            user.setSaloonArName(userObject.getString("arname"));
            user.setSaloonEnName(userObject.getString("enname"));
            user.setPhone(userObject.getString("userphone"));
            user.setPassword(userObject.getString("password"));
            Common.currentUser=user;
            saveUser();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getStatus() {
        return status;
    }

    public String getAction() {
        return action;
    }

    public String getCodeMessage() {

        switch (code){
            case 0:
                codeMessage=errorcodes[code];
                break;
            case 1:
                codeMessage=errorcodes[code];
                break;
            case 2:
                codeMessage=errorcodes[code];
                break;
            case 3:
                codeMessage=errorcodes[code];
                break;
            case 4:
                codeMessage=errorcodes[code];
                break;
            case 5:
                codeMessage=errorcodes[code];
                break;
            case 6:
                codeMessage=errorcodes[code];
                break;
            case 7:
                codeMessage=errorcodes[code];
            case 8:
                codeMessage=errorcodes[code];
                break;
        }
        return codeMessage;
    }
}
