package styles.zonetech.net.styles.server.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.server.Dialogs.ListDialog;
import styles.zonetech.net.styles.server.Helpers.EditTextValidator;
import styles.zonetech.net.styles.server.Helpers.Fonts;
import styles.zonetech.net.styles.server.Helpers.Parser;
import styles.zonetech.net.styles.server.Interfaces.OnListEmpty;
import styles.zonetech.net.styles.server.Interfaces.OnSendBtnClicked;
import styles.zonetech.net.styles.server.Interfaces.OnYesBtnClicked;
import styles.zonetech.net.styles.server.Models.Order;
import styles.zonetech.net.styles.server.Models.OrderDeatil;
import styles.zonetech.net.styles.server.R;
import styles.zonetech.net.styles.server.Server.IServer;
import styles.zonetech.net.styles.server.Utils.Common;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
ArrayList<Order> orders;
Context mContext;
OnListEmpty onListEmpty;

    public void setOnListEmpty(OnListEmpty onListEmpty) {
        this.onListEmpty = onListEmpty;
    }

    public OrdersAdapter(ArrayList<Order> orders, Context mContext) {
        this.orders = orders;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout= LayoutInflater.from(mContext).inflate(R.layout.order_list_item,viewGroup,false);
        return new OrderViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i) {
     orderViewHolder.bind(orders.get(i));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        Fonts fonts;
        Order order;
        public TextView dateTxt,nameTxt,priceTxt,statusTxt,calendarIcon;
        public Button detailsBtn,approveBtn,rejectBtn,finishBtn;
        ConstraintLayout root;
        Parser parser;
        IServer server;
        EditTextValidator validator;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            root=itemView.findViewById(R.id.root);
            dateTxt=itemView.findViewById(R.id.dateTxt);
            nameTxt=itemView.findViewById(R.id.nameTxt);
            priceTxt=itemView.findViewById(R.id.priceTxt);
            detailsBtn=itemView.findViewById(R.id.detailsBtn);
            rejectBtn=itemView.findViewById(R.id.rejectBtn);
            approveBtn=itemView.findViewById(R.id.approveBtn);
            statusTxt=itemView.findViewById(R.id.statusTxt);
            calendarIcon=itemView.findViewById(R.id.calendarIcon);
            finishBtn=itemView.findViewById(R.id.finishBtn);
            fonts=new Fonts(mContext);
            fonts.setTypeFce(itemView);
            server= Common.getAPI();
            parser=new Parser(mContext);
            validator=new EditTextValidator(mContext);
        }



        public void bind(final Order order){
            this.order=order;
            dateTxt.setText(order.getOrderSchedule());
            priceTxt.setText(String.valueOf(order.getOrderTotal())+" EGP");
            if(Fonts.isArabic)
                nameTxt.setText(order.getOrderSaloonArName());
            else
                nameTxt.setText(order.getOrderSaloonEnName());



            Typeface iconsFont=Typeface.createFromAsset(mContext.getAssets(),"fonts/styles.ttf");
            calendarIcon.setTypeface(iconsFont);

            setListeners();
            switch (order.getOrderStatus()){ //  {approved,rejected,pending}    //

                case "1":
                    approveBtn.setVisibility(View.INVISIBLE);
                    finishBtn.setVisibility(View.VISIBLE);
                    statusTxt.setText(mContext.getString(R.string.approved));
                    rejectBtn.setEnabled(false);
                    statusTxt.setBackground(ContextCompat.getDrawable(mContext,R.drawable.down_rounded_corners_green));
                    break;

                case "0":
                    statusTxt.setText(mContext.getString(R.string.pending));
                    approveBtn.setVisibility(View.VISIBLE);
                    finishBtn.setVisibility(View.INVISIBLE);
                    statusTxt.setBackground(ContextCompat.getDrawable(mContext,R.drawable.down_rounded_corners_orange));

                    break;


            }
        }

        private void setListeners() {
            finishBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ListDialog dialog=new ListDialog(mContext,Common.FINISH_ORDER_LAYOUT);
                    dialog.show();
                    dialog.setOnYesBtnClicked(new OnYesBtnClicked() {
                        @Override
                        public void onYesBtnClicked() {
                            finishOrder();
                            dialog.dismiss();
                        }
                    });


                }
            });
            detailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ListDialog dialog=new ListDialog(mContext,Common.ORDER_DETAIL_LAYOUT);
                    dialog.setOrderDeatils(setupOrder());
                    dialog.show();
                }
            });

            approveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ListDialog dialog=new ListDialog(mContext,Common.Approve_ORDER_LAYOUT);
                    dialog.show();
                    dialog.setOnYesBtnClicked(new OnYesBtnClicked() {
                        @Override
                        public void onYesBtnClicked() {
                            approveOrder();
                            dialog.dismiss();
                        }
                    });
                }
            });

            rejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ListDialog dialog=new ListDialog(mContext,Common.REJECT_ORDER_LAYOUT);
                    dialog.show();
                    dialog.setOnSendBtnClicked(new OnSendBtnClicked() {
                        @Override
                        public void onSendBtnClicked(String reasons) {
                            rejectOrder(reasons);
                            dialog.dismiss();
                        }
                    });
                }
            });
        }

        private void finishOrder() {
            server.approve(order.getOrderId()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body()!=null){
                        Log.d(TAG, "onResponse: "+response.body());
                        parser.parse(response.body());
                        if(parser.getStatus().equals("success")){
                            validator.ShowToast(mContext.getString(R.string.order_finish_toast));
                           orders.remove(getAdapterPosition());
                           notifyItemRemoved(getAdapterPosition());
                            if(orders.size()==0){
                                onListEmpty.onListEmpty();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    validator.showSnackbar(root,false,mContext.getString(R.string.connection_validation));
                }
            });

        }

        private ArrayList<OrderDeatil> setupOrder() {
            ArrayList<OrderDeatil> orderDetailsArray=new ArrayList<>();
            try {
                JSONArray orderDetails=new JSONArray(order.getOrderItems());
                JSONObject orderDetailJson;
                OrderDeatil orderDetail=new OrderDeatil();
                for (int i=0;i<orderDetails.length();i++){
                    orderDetailJson=orderDetails.getJSONObject(i);
                    orderDetail.setOrderDetailsTotal(orderDetailJson.getString("total"));
                    orderDetail.setOrderDetailsServices(orderDetailJson.getString("services"));
                    orderDetail.setGetOrderDetailsPerson(orderDetailJson.getString("child"));
                    orderDetailsArray.add(orderDetail);


                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return orderDetailsArray;
        }

        private static final String TAG = "OrderViewHolder";
        private void rejectOrder(String reasons) {
            server.reject(order.getOrderId(),reasons).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body()!=null){
                        Log.d(TAG, "onResponse: "+response.body());
                        parser.parse(response.body());
                        if(parser.getStatus().equals("success")){
                            orders.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            validator.ShowToast(mContext.getString(R.string.order_reject_toast));
                            Log.d(TAG, "onResponse: "+orders.size());
                            if(orders.size()==0){
                                onListEmpty.onListEmpty();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    validator.showSnackbar(root,false,mContext.getString(R.string.connection_validation));

                }
            });
        }

        private void approveOrder() {
            server.approve(order.getOrderId()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body()!=null){
                        Log.d(TAG, "onResponse: "+response.body());
                        parser.parse(response.body());
                        if(parser.getStatus().equals("success")){
                            validator.ShowToast(mContext.getString(R.string.order_approve_toast));
                            statusTxt.setText(mContext.getString(R.string.approved));
                            rejectBtn.setEnabled(false);
                            statusTxt.setBackground(ContextCompat.getDrawable(mContext,R.drawable.down_rounded_corners_green));
                            approveBtn.setVisibility(View.INVISIBLE);
                            finishBtn.setVisibility(View.VISIBLE);

                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                 validator.showSnackbar(root,false,mContext.getString(R.string.connection_validation));
                }
            });
        }
    }

}
