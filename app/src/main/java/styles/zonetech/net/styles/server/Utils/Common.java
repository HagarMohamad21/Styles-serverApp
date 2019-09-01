package styles.zonetech.net.styles.server.Utils;

import styles.zonetech.net.styles.server.Models.User;
import styles.zonetech.net.styles.server.Server.Client;
import styles.zonetech.net.styles.server.Server.IServer;

public class Common {
    public static String domainName="https://www.stylesapps.com/miniapp/";
    public static final String CURRENT_USER = "CURRENT_USER";
    public static final String CONTROL_PANEL="https://www.stylesapps.com/m78i6gcqtp98awr/login";
    public static User currentUser=null;
    public static IServer getAPI(){
        return Client.getClient(domainName).create(IServer.class);
    }




    public static final int REJECT_ORDER_LAYOUT=1;
    public static final int Approve_ORDER_LAYOUT=2;
    public static final int ORDER_DETAIL_LAYOUT=3;
    public static final int FINISH_ORDER_LAYOUT=4;
    public static final int DIALOG_LIST_DEFAULT_SIZE=4;
     public static final int DIALOG_LAYOUT_LOGOUT=5;


    public static final int MenuItemHome=0;
    public static final int MenuItemAccount=1;
    public static final int MenuItemAbout=2;


}
