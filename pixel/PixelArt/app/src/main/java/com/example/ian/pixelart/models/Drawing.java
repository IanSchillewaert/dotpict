package com.example.ian.pixelart.models;

/**
 * Created by ian.
 */

public class Drawing {
    private int id;

    private String colors;

    private String name;

    public Drawing(String name, String colors){
        this.name = name;
        this.colors=colors;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
