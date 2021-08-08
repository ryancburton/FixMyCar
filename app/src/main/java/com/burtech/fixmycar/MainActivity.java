package com.burtech.fixmycar;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText mEditCarPart;
    EditText mEditPrice;
    EditText mEditDuration;

    CarDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDB = new CarDatabase(this);

        try
        {
            //new CreateNewCarPart().execute("http://192.168.0.143:44376/api/company/getAllCompanies");
           //new CreateNewCarPart().execute("XXX");
        }
        catch(Exception c)
        {
            Log.e("",  c.getMessage());
        }

        mEditCarPart = findViewById(R.id.CarPart);
        mEditPrice = findViewById(R.id.Price);
        mEditDuration = findViewById(R.id.Duration);

        Button buttonAddUpdate = findViewById(R.id.AddCarPart);

        if(buttonAddUpdate != null)
        {
            buttonAddUpdate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {

                    try
                    {
                        new CreateNewCarPart().execute(mEditCarPart.getText().toString());
                    }
                    catch(Exception c)
                    {
                        Log.e("",  c.getMessage());
                    }

                    /*if(mEditCarPart.getText().toString().equalsIgnoreCase(""))
                    {
                        Toast errorToast = Toast.makeText(MainActivity.this, "Please enter Valid Car Part.", Toast.LENGTH_SHORT);
                        errorToast.show();
                        return;
                    }

                    if(!tryParseInt(mEditPrice.getText().toString()))
                    {
                        Toast errorToast = Toast.makeText(MainActivity.this, "Please enter Price.", Toast.LENGTH_SHORT);
                        errorToast.show();
                        return;
                    }

                    if(!tryParseInt(mEditDuration.getText().toString()))
                    {
                        Toast errorToast = Toast.makeText(MainActivity.this, "Please enter Duruation.", Toast.LENGTH_SHORT);
                        errorToast.show();
                        return;
                    }

                    if (mDB.saveRecord(mEditCarPart.getText().toString(), Integer.parseInt(mEditPrice.getText().toString()), Integer.parseInt(mEditDuration.getText().toString())))
                    {
                        addNewCarPart();
                    }
                    else
                    {
                        updateCarPartTable();
                    }*/
                }
            });
        }

        //addHeadings();
        updateCarPartTable();
    }

    boolean tryParseInt(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    public void addHeadings()
    {
        TableLayout carParts = findViewById(R.id.carPartsTable);
        //carParts.removeAllViews();

        TableRow tableRow = findViewById(R.id.carPartsTableRow);
        TextView colCarPart = findViewById(R.id.colCarPart);
        TextView colPrice = findViewById(R.id.colPrice);
        TextView colDuration = findViewById(R.id.colDuration);

        tableRow.addView(colCarPart);
        tableRow.addView(colPrice);
        tableRow.addView(colDuration);
        carParts.addView(tableRow);
    }

    public void Add_Headings()
    {
        TableLayout carParts = (TableLayout)findViewById(R.id.carPartsTable);
        //carParts.removeAllViews();
        carParts.setStretchAllColumns(true);
        carParts.bringToFront();

        TableRow tr = new TableRow(this);//(TableRow) findViewById(R.id.tableRow);
        TextView c1 = new TextView(this);
        c1.setBackgroundColor(Color.parseColor("#D8FFB9"));
        //c1.setWidth(150);
        c1.setText("Car Part");
        TextView c2 = new TextView(this);
        c2.setBackgroundColor(Color.parseColor("#D8FFB9"));
        c2.setText("Price");
        //c1.setWidth(150);
        TextView c3 = new TextView(this);//(TextView) findViewById(R.id.col3);
        c3.setBackgroundColor(Color.parseColor("#D8FFB9"));
        c3.setText("Duration");
        //c1.setWidth(150);
        tr.addView(c1);
        tr.addView(c2);
        tr.addView(c3);
        carParts.addView(tr);
    }

    public void addNewCarPart()
    {
        TableLayout carParts = findViewById(R.id.carPartsTable);
        //carParts.removeAllViews();
        carParts.setStretchAllColumns(true);
        carParts.bringToFront();

        //TableRow tr = findViewById(R.id.carPartsTableRow);

        TableRow tr = new TableRow(this);

        TextView c1 = new TextView(this);
        c1.setBackgroundColor(Color.parseColor("#D8FFB9"));
        //TableRow.LayoutParams params = new TableRow.LayoutParams(2, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        //c1.setLayoutParams(params);
        c1.setText(mEditCarPart.getText());
        TextView c2 = new TextView(this);
        c2.setBackgroundColor(Color.parseColor("#D8FFB9"));
        c2.setText(mEditPrice.getText());
        //params = new TableRow.LayoutParams(1, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        //c2.setLayoutParams(params);
        TextView c3 = new TextView(this);//(TextView) findViewById(R.id.col3);
        c3.setBackgroundColor(Color.parseColor("#D8FFB9"));
        c3.setText(mEditDuration.getText());
        //params = new TableRow.LayoutParams(1, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        //c3.setLayoutParams(params);

        tr.addView(c1);
        tr.addView(c2);
        tr.addView(c3);
        carParts.addView(tr);
    }

    public void updateCarPartTable()
    {
        Cursor cursor = mDB.getCarPartsList();

        if(cursor == null)
            return;

        TableLayout carParts = findViewById(R.id.carPartsTable);
        //carParts.removeAllViews();
        carParts.setStretchAllColumns(true);
        carParts.bringToFront();

        while(cursor.moveToNext())
        {
            TableRow tr =  new TableRow(this);//(TableRow) findViewById(R.id.tableRow);
            TextView c1 = new TextView(this);
           // c1.setBackgroundColor(Color.parseColor("#D8FFB9"));
            c1.setText(cursor.getString(1));
            TextView c2 = new TextView(this);
            //c1.setBackgroundColor(Color.parseColor("#D8FFB9"));
            c2.setText(cursor.getString(2));
            TextView c3 = new TextView(this);//(TextView) findViewById(R.id.col3);
            //c3.setBackgroundColor(Color.parseColor("#D8FFB9"));
            c3.setText(cursor.getString(3));
            tr.addView(c1);
            tr.addView(c2);
            tr.addView(c3);
            carParts.addView(tr);
        }
    }
}