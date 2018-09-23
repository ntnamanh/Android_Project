package com.example.hung.myapplication.HttpHandlerJsondata;

/**
 * Created by Hung on 7/20/2017.
 */

public class Result {
    public String formatted_address;

    public Result(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    @Override
    public String toString() {
        return formatted_address;
    }
}
