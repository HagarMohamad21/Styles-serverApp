package styles.zonetech.net.styles.server.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Locale;

import styles.zonetech.net.styles.server.Dialogs.MenuDialog;
import styles.zonetech.net.styles.server.Helpers.Fonts;

import static styles.zonetech.net.styles.server.Helpers.Fonts.SELECT_LANGUAGE;

public class CommonMethods {
    Context mContext;
    MenuDialog menuDialog;
    public CommonMethods(Context mContext) {
        this.mContext = mContext;
    }

    public void setupFont(View view){
        Fonts fonts=new Fonts(mContext);
        fonts.setTypeFce(view);
    }



    public void setupMenu() {
        this.menuDialog=new MenuDialog(mContext);
        SharedPreferences languagePref =mContext.getSharedPreferences(mContext.getPackageName(),0);
        String lang=languagePref.getString(SELECT_LANGUAGE," default value");
        Window window = this.menuDialog.getWindow();
        this.menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = window.getAttributes();
        if(lang.equals("ar")){

            wlp.gravity = Gravity.TOP|Gravity.LEFT;
        }
        else   if(lang.equals("en") ){

            wlp.gravity = Gravity.TOP|Gravity.RIGHT;
        }
        else{

            Locale language= Resources.getSystem().getConfiguration().locale;
            languagePref =mContext.getSharedPreferences(mContext.getPackageName(),0);

            lang=languagePref.getString(SELECT_LANGUAGE,language.getLanguage());

            if(lang.equals("ar")){

                wlp.gravity = Gravity.TOP|Gravity.LEFT;
            }
            else   if(lang.equals("en") ){
                wlp.gravity = Gravity.TOP|Gravity.RIGHT;
            }
        }

        wlp.x=40;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

    }

    public void showMenu(){
        if(menuDialog!=null){
            menuDialog.show();
        }
    }
}
