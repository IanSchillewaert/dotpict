package com.example.ian.pixelart.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.GridLayout;
import com.example.ian.pixelart.models.Grid;
import com.example.ian.pixelart.R;

public class Canvas extends GridLayout {

    private Grid grid;
    private ElementView[][] views;
    int gridsize = 30;
    private boolean Big = false;


    public Canvas(Context context) {
        super(context);
        initBoard(context);
    }


    public Canvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBoard(context);

    }

    public Canvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBoard(context);

    }

    private void initBoard(Context context) {
        grid = Grid.get(gridsize);
        views = new ElementView[gridsize][gridsize];
        this.setRowCount(grid.getGridSize());
        this.setColumnCount(grid.getGridSize());

        int width = calculateCardWidth();

        for(int i = 0; i< grid.getGridSize(); i++){
            for(int j= 0; j <grid.getGridSize(); j++){
                ElementView v = new ElementView(this.getContext(),grid.getElement(i,j));
                views[i][j] = v;
                addView(v, width,width);
                Log.i("Info", "Added a view " + i + " " + j);
            }
        }
    }

    private int calculateCardWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int actbarh=getActionBarHeight();
        return Math.min(displayMetrics.widthPixels,displayMetrics.heightPixels - actbarh) / grid.getGridSize();
    }

    public int getActionBarHeight() {
        final TypedArray ta = getContext().getTheme().obtainStyledAttributes(
                new int[] {android.R.attr.actionBarSize});
        int actionBarHeight = (int) ta.getDimension(0, 0);
        return actionBarHeight;
    }

    public Grid getGrid() {
        return grid;
    }


    public void clearCanvas(){
        for(int i =0; i<getChildCount(); i++){
            ElementView v = (ElementView)getChildAt(i);
            v.clear();
        }
    }

    public void changeColor(String color){
        for(int i =0; i<getChildCount(); i++){
            ElementView v = (ElementView)getChildAt(i);
            v.setColor(color);
        }
    }

    public String getColor(){
        String color = "black";
        for(int i =0; i<getChildCount(); i++){
            ElementView v = (ElementView)getChildAt(i);
            color = v.getcolor();
        }
        return color;
    }

    public void setPictureColor(int color, int i, int j){
        views[i][j].changeElementColor(color);
    }

    public int getColorInt(int i, int j){
        return views[i][j].getColorInt();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();
        int width = calculateCardWidth();
        int xval = (int) Math.floor(x / width);
        int yval = (int) Math.floor(y / width);

        if(Big){
            if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN) {
                if (xval >= 1 && xval < gridsize-1 && yval >= 1 && yval < gridsize-1) {
                    views[yval][xval].getElement().setColor(views[yval][xval].getcolor());
                    views[yval+1][xval].getElement().setColor(views[yval+1][xval].getcolor());
                    views[yval][xval+1].getElement().setColor(views[yval][xval+1].getcolor());
                    views[yval+1][xval+1].getElement().setColor(views[yval+1][xval+1].getcolor());
                    views[yval][xval].setBackgroundColor(views[yval][xval].getBackgroundColor());
                    views[yval+1][xval].setBackgroundColor(views[yval+1][xval].getBackgroundColor());
                    views[yval][xval+1].setBackgroundColor(views[yval][xval+1].getBackgroundColor());
                    views[yval+1][xval+1].setBackgroundColor(views[yval+1][xval+1].getBackgroundColor());
                }
                if (xval == 0 || xval == gridsize -1 || yval == 0 || yval == gridsize -1){
                    if (xval >= 0 && xval < gridsize && yval >= 0 && yval < gridsize) {
                        views[yval][xval].getElement().setColor(views[yval][xval].getcolor());
                        views[yval][xval].setBackgroundColor(views[yval][xval].getBackgroundColor());
                    }
                }
            }
        }else {
            if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN) {
                if (xval >= 0 && xval < gridsize && yval >= 0 && yval < gridsize) {
                    views[yval][xval].getElement().setColor(views[yval][xval].getcolor());
                    views[yval][xval].setBackgroundColor(views[yval][xval].getBackgroundColor());
                }
            }
        }
        return true;
    }

    public void setBig(boolean big){
        this.Big = big;
    }
}
