package utils;

import models.Courier;

import java.util.UUID;

public class CourierGenerator {

    public static Courier getRandomCourier() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        return new Courier(
                "login_" + unique,
                "pass_" + unique,
                "name_" + unique
        );
    }
}