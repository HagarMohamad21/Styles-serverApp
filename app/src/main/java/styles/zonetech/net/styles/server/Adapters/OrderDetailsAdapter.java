package styles.zonetech.net.styles.server.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import styles.zonetech.net.styles.server.Helpers.Fonts;
import styles.zonetech.net.styles.server.Models.OrderDeatil;
import styles.zonetech.net.styles.server.R;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.DialogViewHolder> {

    Context mContext;
    ArrayList<OrderDeatil>orderDeatils;

    public OrderDetailsAdapter(Context mContext, ArrayList<OrderDeatil> orderDeatils) {
        this.mContext = mContext;
        this.orderDeatils = orderDeatils;
    }

    @NonNull
    @Override
    public DialogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View layout= LayoutInflater.from(mContext).inflate(R.layout.dialog_list_item,viewGroup,false);
            return new DialogViewHolder(layout);}

    @Override
    public void onBindViewHolder(@NonNull DialogViewHolder dialogViewHolder, int i) {
     dialogViewHolder.bind(orderDeatils.get(i));
    }

    @Override
    public int getItemCount() {
        return orderDeatils.size();
    }

    class DialogViewHolder extends RecyclerView.ViewHolder{
        Fonts fonts;
        TextView itemNameTxt, rightMostTxt, itemDetailsTxt;
         CheckBox itemCheckBox;
        public DialogViewHolder(@NonNull View itemView) {
            super(itemView);
            fonts=new Fonts(mContext);
            fonts.setTypeFce(itemView);
            itemNameTxt=itemView.findViewById(R.id.itemNameTxt);
            itemDetailsTxt=itemView.findViewById(R.id.serviceDetailsTxt);
            rightMostTxt=itemView.findViewById(R.id.rightMostTxt);
            itemCheckBox=itemView.findViewById(R.id.itemCheckBox);
            itemCheckBox.setVisibility(View.GONE);
        }

        public void bind(OrderDeatil orderDeatil) {

            String person=orderDeatil.getGetOrderDetailsPerson();
            if (person.equals("1"))
                itemNameTxt.setText(mContext.getString(R.string.children));
            else{
                itemNameTxt.setText(mContext.getString(R.string.adults));
            }
            itemDetailsTxt.setText(orderDeatil.getOrderDetailsServices());
            rightMostTxt.setText(orderDeatil.getOrderDetailsTotal());
        }
    }
}
