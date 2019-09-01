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

import styles.zonetech.net.styles.server.Dialogs.MenuDialog;
import styles.zonetech.net.styles.server.Helpers.Fonts;
import styles.zonetech.net.styles.server.Interfaces.DialogListener;
import styles.zonetech.net.styles.server.Models.MenuModel;
import styles.zonetech.net.styles.server.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    Context mContext;
    ArrayList<MenuModel> models;
    DialogListener dialogListener;

    public MenuAdapter(Context mContext, ArrayList<MenuModel> models) {
        this.mContext = mContext;
        this.models = models;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout= LayoutInflater.from(mContext).inflate(R.layout.dialog_list_item,null,false);
        return new MenuViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder menuViewHolder, int i) {
        menuViewHolder.bind(models.get(i));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener=dialogListener;
    }


    class MenuViewHolder extends RecyclerView.ViewHolder{
        TextView itemNameTxt,rightMostTxt,itemDetailsTxt,itemIcon;
        CheckBox itemCheckBox;
        Fonts fonts;
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTxt=itemView.findViewById(R.id.itemNameTxt);
            rightMostTxt=itemView.findViewById(R.id.rightMostTxt);
            itemDetailsTxt=itemView.findViewById(R.id.serviceDetailsTxt);
            itemIcon=itemView.findViewById(R.id.itemIcon);
            itemCheckBox=itemView.findViewById(R.id.itemCheckBox);
            fonts=new Fonts(mContext);
            fonts.setTypeFce(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(dialogListener!=null){
                        int pos=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            dialogListener.onClickRow(pos);
                        }
                    }
                }
            });
        }

        public void bind(MenuModel menuModel) {
            itemNameTxt.setText(menuModel.getMenuName());
            itemNameTxt.setTypeface(fonts.mediumFont);
            itemDetailsTxt.setVisibility(View.GONE);
            rightMostTxt.setVisibility(View.GONE);
            itemIcon.setVisibility(View.VISIBLE);
            itemCheckBox.setVisibility(View.INVISIBLE);
            itemIcon.setText(menuModel.getIconName());
        }
    }
}
