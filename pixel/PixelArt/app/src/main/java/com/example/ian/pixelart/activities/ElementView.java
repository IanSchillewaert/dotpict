package com.example.ian.pixelart.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ian.pixelart.R;
import com.example.ian.pixelart.models.Element;

public class ElementView extends FrameLayout {

    private Element element;
    public String color = "white";

    public ElementView(Context context, Element element) {
        super(context);
        initElementView(element);
    }


    public ElementView(@NonNull Context context, @Nullable AttributeSet attrs, Element element) {
        super(context, attrs);
        initElementView(element);
    }

    public ElementView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, Element element) {
        super(context, attrs, defStyleAttr);
        initElementView(element);
    }

    private void initElementView(Element element){
        this.element =element;
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.element, this, true);
        updateElementView();
    }

    public void setColor(String color){
        this.color=color;
    }

    public void clear(){
        element.setColor("white");
        updateElementView();
    }

    public void updateElementView(){this.setBackgroundColor(getBackgroundColor());}

    public int getBackgroundColor() {
        switch (element.getColor().toLowerCase()) {
            case "white": return Color.parseColor("#FFFFFF");
            case "black": return Color.parseColor("#000000");
            case "ferrari": return Color.parseColor("#F70D1A");
            case "blue": return Color.parseColor("#0000ff");
            case "yellow": return Color.parseColor("#ffff00");
            case "green": return Color.parseColor("#00cc00");
            case "purple": return Color.parseColor("#9933ff");
            case "aqua":return Color.parseColor("#00FFFF");
            case "fuchsia":return Color.parseColor("#FF00FF");
            case "silver":return Color.parseColor("#C0C0C0");
            case "olive":return Color.parseColor("#808000");
            case "maroon":return Color.parseColor("#800000");
            case "teal":return Color.parseColor("#008080");
            case "lime":return Color.parseColor("#00FF00");
            case "brown":return Color.parseColor("#8B4513");
            case "skin":return Color.parseColor("#FFE4C4");
            case "orange":return Color.parseColor("#f79d00");
            case "sky":return Color.parseColor("#3BB9FF");
            case "pink":return Color.parseColor("#FDD7E4");
            case "lilac":return Color.parseColor("#C8A2C8");
        }

        return Integer.parseInt(element.getColor());
    }

   /* @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        this.element.setColor(color);
        this.setBackgroundColor(getBackgroundColor());
        return false;
    }*/

    public String getcolor(){
        return color;
    }

    public Element getElement() {
        return element;
    }

    public void changeElementColor(int color){
        this.setBackgroundColor(color);
        element.setColor(String.valueOf(color));
    }

    public int getColorInt(){
        return this.getBackgroundColor();
    }

}
