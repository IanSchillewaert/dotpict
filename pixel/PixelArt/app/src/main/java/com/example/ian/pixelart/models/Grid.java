package com.example.ian.pixelart.models;

import com.example.ian.pixelart.activities.ElementView;

import java.util.ArrayList;
import java.util.List;
public class Grid {

    private static Grid gridInstance;

    private int gridSize;

    private Element[][] grid;

    private Grid(int gridSize) {
        setGridSize(gridSize);
        grid = new Element[gridSize][gridSize];

        //Initialise the grid with new elements
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = new Element();
            }
        }

    }

    public static Grid get(int gridSize) {
        if (gridInstance == null)
            gridInstance = new Grid(gridSize);
        return gridInstance;
    }

    public Element getElement(int i, int j) {
        return grid[i][j];
    }


    public int getGridSize() {
        return gridSize;
    }


    public void setGridSize(int gridSize) {
        if (gridSize < 1)
            throw new IllegalArgumentException("The grid size cannot be less than 1");
        else if (gridSize > 75)
            throw new IllegalArgumentException("The grid size cannot be greater then 75");
        else
            this.gridSize = gridSize;
    }
}
