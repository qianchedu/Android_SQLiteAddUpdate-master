package com.dsz.asqlitetest;

import java.io.Serializable;

/**
 * Created by Soham on 17-Mar-15.
 */
public class RestaurantModel implements Serializable {
    private long id;
    private String name;
    private String address;
    private int type;

    public RestaurantModel() {
    }

    public RestaurantModel(String name, String address, int type) {
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
