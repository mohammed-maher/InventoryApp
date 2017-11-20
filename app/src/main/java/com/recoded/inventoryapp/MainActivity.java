package com.recoded.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.recoded.inventoryapp.data.InventoryContract;
import com.recoded.inventoryapp.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private InventoryDbHelper dbHelper;
    private TextView noItemsText;
    private Cursor data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton addButton = findViewById(R.id.add_button);
        listView = findViewById(R.id.products_list);
        dbHelper = new InventoryDbHelper(this);
        noItemsText = findViewById(R.id.no_items_text);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DetailsActivity.class));
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                Cursor c= (Cursor)adapterView.getItemAtPosition(i);
                long id= c.getInt(c.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_ID));
                intent.putExtra("itemID",id);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        populateList();
    }

    public void populateList(){
        Cursor c = dbHelper.read();
        if(c.getCount()>0){
            InventoryCursorAdapter adapter = new InventoryCursorAdapter(this,c);
            listView.setAdapter(adapter);
        }else{
            listView.setVisibility(View.GONE);
            noItemsText.setVisibility(View.VISIBLE);
        }

    }
}
