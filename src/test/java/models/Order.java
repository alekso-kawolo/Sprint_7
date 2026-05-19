package models;

import java.util.List;

public class Order {
    private String firstName = "Вася";
    private String lastName = "Пупкин";
    private String address = "Москва";
    private String metroStation = "ВДНХ";
    private String phone = "+71234567890";
    private int rentTime = 3;
    private String deliveryDate = "2026-05-20";
    private String comment = "Желательно с крышей";
    private List<String> color;

    public Order(List<String> color) {
        this.color = color;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public String getPhone() {
        return phone;
    }

    public int getRentTime() {
        return rentTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getComment() {
        return comment;
    }

    public List<String> getColor() {
        return color;
    }
}