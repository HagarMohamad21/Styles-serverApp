package styles.zonetech.net.styles.server.Models;

public class Order {
    String orderId,orderSaloonId,orderStatus,orderSaloonEnName,orderSaloonArName;
    int orderTotal;
    String orderSchedule,orderReasons,orderAddress,orderItems;
    String coupon;

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSaloonId() {
        return orderSaloonId;
    }

    public void setOrderSaloonId(String orderSaloonId) {
        this.orderSaloonId = orderSaloonId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderSaloonEnName() {
        return orderSaloonEnName;
    }

    public void setOrderSaloonEnName(String orderSaloonEnName) {
        this.orderSaloonEnName = orderSaloonEnName;
    }

    public String getOrderSaloonArName() {
        return orderSaloonArName;
    }

    public void setOrderSaloonArName(String orderSaloonArName) {
        this.orderSaloonArName = orderSaloonArName;
    }

    public int getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(int orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderSchedule() {
        return orderSchedule;
    }

    public void setOrderSchedule(String orderSchedule) {
        this.orderSchedule = orderSchedule;
    }

    public String getOrderReasons() {
        return orderReasons;
    }

    public void setOrderReasons(String orderReasons) {
        this.orderReasons = orderReasons;
    }


    public String getOrderId() {
        return orderId;
    }
}
