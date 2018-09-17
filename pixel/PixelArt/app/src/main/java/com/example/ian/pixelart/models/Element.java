package com.example.ian.pixelart.models;


public class Element {

    private String color;


    public Element() {
        this.color = "white";
    }


    public Element(String color){
        this.color=color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean noColor(){
        return this.color.equals("white");
    }
}
