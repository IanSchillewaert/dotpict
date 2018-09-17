package com.example.ian.pixelart.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ian.pixelart.R;
import com.example.ian.pixelart.database.DatabaseHelper;
import com.example.ian.pixelart.models.Drawing;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        /*implements NavigationView.OnNavigationItemSelectedListener */{

    private Canvas myCanvas;
    private Spinner spinner;
    private ImageButton eraser;
    private Switch eraserswitch;
    private ImageButton photoBttn;
    private ImageButton loadButton;
    private ImageButton saveButton;
    private CheckBox BigBrushCheck;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int gridSize;
    private Intent i;
    private String nameDrawing;
    private int [][] colors;
    private String colorString = "";
    private DatabaseHelper myDb;
    private String loadedColors;
    private Cursor res;
    boolean exists = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCanvas = (Canvas)findViewById(R.id.canvas);
        gridSize = myCanvas.getGrid().getGridSize();
        myDb = new DatabaseHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addListenerOnSpinnerItemSelection();
        addListenerOnButton();
        addListenerOnSwitch();
        addListenerOnCheckBox();
        i = new Intent(this,LoadActivity.class);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                loadedColors= null;
            } else {
                loadedColors= extras.getString("COLOR_STRING");
            }
        } else {
            loadedColors= (String) savedInstanceState.getSerializable("COLOR_STRING");
        }

        if (loadedColors!=null){
            loadDrawing(loadedColors);
        }

    }

    private void addListenerOnCheckBox() {
        BigBrushCheck = (CheckBox) findViewById(R.id.BigBrush);
        BigBrushCheck.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(BigBrushCheck.isChecked()){
                    myCanvas.setBig(true);
                    Toast.makeText(MainActivity.this, "Big brush enabled!!", Toast.LENGTH_SHORT).show();
                }else{
                    myCanvas.setBig(false);
                    Toast.makeText(MainActivity.this, "Small brush enabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addListenerOnSwitch() {
        eraserswitch = (Switch) findViewById(R.id.eraserSwitch);
        eraserswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String color = "";
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    color = myCanvas.getColor();
                    Toast.makeText(MainActivity.this, "You can now erase pixels!", Toast.LENGTH_SHORT).show();
                    myCanvas.changeColor("white");
                } else {
                    Toast.makeText(MainActivity.this, "Eraser off!", Toast.LENGTH_SHORT).show();
                    myCanvas.changeColor(color);
                }
            }
        });
    }

    private void addListenerOnButton() {
        eraser = (ImageButton) findViewById(R.id.eraserBttn);
        eraser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(MainActivity.this,
                        "You can now erase pixels for 5 seconds!", Toast.LENGTH_SHORT).show();
                final String color = myCanvas.getColor();
                myCanvas.changeColor("white");

                Runnable r = new Runnable() {
                    @Override
                    public void run(){
                        Toast.makeText(MainActivity.this, "Color has been set back to " + color, Toast.LENGTH_SHORT).show();
                        myCanvas.changeColor(color);
                    }
                };

                Handler h = new Handler();
                h.postDelayed(r, 5000);
            }
        });
        photoBttn = (ImageButton) findViewById(R.id.pictureBttn);
        photoBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        loadButton = (ImageButton) findViewById(R.id.loadBttn);
        saveButton = (ImageButton) findViewById(R.id.saveBttn);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });

    }

    private void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner) findViewById(R.id.color_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //Toast.makeText(parent.getContext(), "Selected color: " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
                myCanvas.changeColor(parent.getItemAtPosition(pos).toString());
                if (eraserswitch.isChecked()){
                    eraserswitch.setChecked(false);
                    Toast.makeText(MainActivity.this, "Eraser off!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_draw:
                newDraw();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void newDraw(){
        myCanvas.clearCanvas();
        Toast.makeText(getApplicationContext(), "Canvas cleared", Toast.LENGTH_SHORT).show();

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap resized = Bitmap.createScaledBitmap(imageBitmap,gridSize,gridSize,false);
            int width = resized.getWidth();
            int height = resized.getHeight();
            int[] pixels = new int[width*height];
            resized.getPixels(pixels,0,width,0,0,width,height);
            int[][] pixeldouble = new int[gridSize][gridSize];

            //pixels omzetten naar dubbele array
            for(int i = 0; i < gridSize; i++){
                for(int j=0; j< gridSize;j++){
                    pixeldouble[i][j] = pixels[(j*gridSize)+i];
                }
            }

            //kleuren
            for(int i = 0; i < gridSize; i++){
                for(int j=0; j< gridSize;j++){
                    myCanvas.setPictureColor(pixeldouble[i][j],j,i);
                }
            }
        }
    }
    private void showDialog(){
        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.dialog_add, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflator);
        final EditText text = inflator.findViewById(R.id.saveName);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //user clicked ok, get the name value
                        nameDrawing = text.getText().toString().toUpperCase().trim();
                        res = myDb.getAllData();
                        while (res.moveToNext()){
                            String name = res.getString(1);
                            if(nameDrawing.equalsIgnoreCase(name))
                                exists = true;
                        }
                        res.moveToPosition(-1);
                        if(nameDrawing == "" || nameDrawing == null || nameDrawing.isEmpty() || nameDrawing.trim().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Enter a name please", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                        else if(exists){
                            Toast.makeText(getApplicationContext(), "The name "+ nameDrawing + " already exists, please choose a new one", Toast.LENGTH_LONG).show();
                            exists = false;
                            dialog.dismiss();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "saving " + nameDrawing + "...", Toast.LENGTH_LONG).show();
                            saveDrawing();
                            boolean isInserted = myDb.insertData(nameDrawing, colorString);
                            if (isInserted)
                                Toast.makeText(getApplicationContext(), nameDrawing + " saved succesfully", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), "saving of " + nameDrawing + " failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //user clicked cancel
                        dialog.cancel();
                    }
                }).setTitle("Geef een naam");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void saveDrawing(){
        int[] pixels = new int[gridSize*gridSize];
        colors = new int[gridSize][gridSize];

        //get kleuren
        for(int i = 0; i < gridSize; i++){
            for(int j=0; j< gridSize;j++){
                colors[i][j] = myCanvas.getColorInt(i,j);
            }
        }

        //fill pixels
        for(int i = 0; i < gridSize; i++){
            for(int j=0; j< gridSize;j++){
                pixels[j + gridSize*i] = colors[i][j];
            }
        }

        //make colorstring
        colorString = "";
        for(int i:pixels){
            colorString += i+", ";
        }
        colorString = colorString.substring(0,colorString.length() -2);
    }

    public void loadDrawing(String colors){
        String colorsplit = colors;
        String[] singleString = colorsplit.split(",");
        int [] singleInt = new int[gridSize*gridSize];
        int counter = 0;
        for (String c: singleString){
            c= c.trim();
            singleInt[counter] = Integer.parseInt(c);
            counter++;
        }

        //kleuren
        for(int i = 0; i < gridSize; i++){
            for(int j=0; j< gridSize;j++){
                myCanvas.setPictureColor(singleInt[gridSize*i + j],i,j);
            }
        }
    }
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MemoryLeakApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
