package styles.zonetech.net.styles.server.Models;

public class MenuModel {

    String menuName, iconName;

    public MenuModel(String menuName, String iconName) {
        this.menuName = menuName;
        this.iconName = iconName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

}
