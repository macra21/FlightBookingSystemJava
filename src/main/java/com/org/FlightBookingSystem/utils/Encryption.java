package com.org.FlightBookingSystem.utils;

import java.nio.charset.StandardCharsets;

public class Encryption {
    static public String SHA256OneWayHash(String text){
        return com.google.common.hash.Hashing.sha256()
                .hashString(text, StandardCharsets.UTF_8)
                .toString();
    }
}
