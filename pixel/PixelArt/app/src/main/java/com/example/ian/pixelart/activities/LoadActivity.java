package com.example.ian.pixelart.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ian.pixelart.R;
import com.example.ian.pixelart.adapters.DrawingAdapter;
import com.example.ian.pixelart.database.DatabaseHelper;
import com.example.ian.pixelart.interfaces.RecyclerItemTouchHelperListener;
import com.example.ian.pixelart.models.Drawing;
import com.example.ian.pixelart.util.CustomRVItemTouchListener;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ian.
 */

public class LoadActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Drawing> drawings;
    private Drawing drawing;
    private DrawingAdapter adapter;
    private DatabaseHelper myDb;
    private Cursor res;
    private Intent intent;
    public FloatingActionButton fabOne, fabAll;
    private String nameDrawing;
    private Handler handler;

    public LoadActivity(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadimg);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        intent = new Intent(this,MainActivity.class);
        drawings = new ArrayList<>();
        myDb = new DatabaseHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabOne = (FloatingActionButton) findViewById(R.id.fab_deleteOne);
        fabAll = (FloatingActionButton) findViewById(R.id.fab_deleteAll);

        fabOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        fabAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.deleteAllData();
                Toast.makeText(getApplicationContext(), "All drawings were deleted", Toast.LENGTH_LONG).show();
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startActivity(intent);
                    }
                }, 1500);
            }
        });

        res = myDb.getAllData();
        if(res.getCount() == 0){
            //show message
            showMessage("Error","no data found");
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    startActivity(intent);
                }
            }, 1500);
            return;
        }
        //load data
        while (res.moveToNext()){
            drawing = new Drawing(res.getString(1),res.getString(2));
            int id = Integer.parseInt(res.getString(0));
            drawing.setId(id);
            drawings.add(drawing);
        }

        adapter = new DrawingAdapter(drawings,getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(getApplicationContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(250);
        itemAnimator.setRemoveDuration(250);
        recyclerView.setItemAnimator(itemAnimator);

        recyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(getApplicationContext(), recyclerView, new RecyclerItemTouchHelperListener() {
            @Override
            public void onClick(View view, int position) {
                drawing = adapter.getdrawing(position);
                Toast.makeText(getApplicationContext(), "Loading: "+ drawing.getName(), Toast.LENGTH_LONG).show();
                intent.putExtra("COLOR_STRING", drawing.getColors());
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startActivity(intent);
                    }
                }, 1500);
            }
        }));
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void showDialog(){
        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.dialog_delete, null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(inflator);
        final EditText text = inflator.findViewById(R.id.deleteName);
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //user clicked ok
                nameDrawing = text.getText().toString();
                Integer deletedRows =  myDb.deleteData(text.getText().toString().toUpperCase());
                if(deletedRows > 0){
                    Toast.makeText(getApplicationContext(), deletedRows + " row(s) deleted", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "no rows deleted", Toast.LENGTH_SHORT).show();

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

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MemoryLeakApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
