package com.recoded.inventoryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lenovo on 11/17/2017.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public InventoryDbHelper(Context context) {
        super(context, InventoryContract.DATABASE_NAME, null, InventoryContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(InventoryContract.ProductsEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(InventoryContract.ProductsEntry.SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public boolean insert(String title, int price, byte[] image,String suppiler, int quantity) {
        db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryContract.ProductsEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE, price);
        contentValues.put(InventoryContract.ProductsEntry.COLUMN_NAME_IMAGE, image);
        contentValues.put(InventoryContract.ProductsEntry.COLUMN_NAME_QUANTITY, quantity);
        contentValues.put(InventoryContract.ProductsEntry.COLUMN_NAME_SUPPILERNUMBER, suppiler);

        long id = db.insert(InventoryContract.ProductsEntry.TABLE_NAME, null, contentValues);
        return id != -1;
    }

    public Cursor read(long id) {
        db = this.getReadableDatabase();

        String[] projection = {
                InventoryContract.ProductsEntry.COLUMN_NAME_ID,
                InventoryContract.ProductsEntry.COLUMN_NAME_TITLE,
                InventoryContract.ProductsEntry.COLUMN_NAME_IMAGE,
                InventoryContract.ProductsEntry.COLUMN_NAME_QUANTITY,
                InventoryContract.ProductsEntry.COLUMN_NAME_PRICE,
                InventoryContract.ProductsEntry.COLUMN_NAME_SUPPILERNUMBER
        };

        String selection = InventoryContract.ProductsEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {id + ""};

        Cursor cursor = db.query(
                InventoryContract.ProductsEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        return cursor;
    }

    public Cursor read(){
        db = this.getReadableDatabase();
        return db.query(InventoryContract.ProductsEntry.TABLE_NAME,null,null,null,null,null,null);
    }

    public boolean update(long id,String title,int price,byte[] image,String suppiler,int quantity){
        db = this.getWritableDatabase();

        String selection = InventoryContract.ProductsEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { id+"" };

        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryContract.ProductsEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE, price);
        contentValues.put(InventoryContract.ProductsEntry.COLUMN_NAME_IMAGE, image);
        contentValues.put(InventoryContract.ProductsEntry.COLUMN_NAME_SUPPILERNUMBER, suppiler);
        contentValues.put(InventoryContract.ProductsEntry.COLUMN_NAME_QUANTITY, quantity);

        long count = db.update(InventoryContract.ProductsEntry.TABLE_NAME, contentValues, selection,selectionArgs);

        return count !=-1;
    }

    public boolean decreaseQuantity(long id){
        db = this.getWritableDatabase();

        String selection = InventoryContract.ProductsEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { id+"" };

        Cursor c = this.read(id);
        ContentValues contentValues = new ContentValues();

        while (c.moveToNext()) {
            int quantity = c.getInt(c.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_QUANTITY));
            if(quantity>0){
            contentValues.put(InventoryContract.ProductsEntry.COLUMN_NAME_QUANTITY, (quantity-1));
            }
        }
        long count = db.update(InventoryContract.ProductsEntry.TABLE_NAME, contentValues, selection,selectionArgs);

        return count !=-1;
    }

    public void delete(long id){
        db = this.getWritableDatabase();
        String selection = InventoryContract.ProductsEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { id+"" };
        db.delete(InventoryContract.ProductsEntry.TABLE_NAME, selection, selectionArgs);
    }
}
