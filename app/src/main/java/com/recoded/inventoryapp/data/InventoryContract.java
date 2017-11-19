package com.recoded.inventoryapp.data;

import android.provider.BaseColumns;

/**
 * Created by Lenovo on 11/16/2017.
 */

public class InventoryContract implements BaseColumns {
    public static final String DATABASE_NAME="inventory.db";
    public static final int DATABASE_VERSION = 1;
    public static final class ProductsEntry{
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_SUPPILERNUMBER = "suppilernumber";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + InventoryContract.ProductsEntry.TABLE_NAME + " (" +
                        InventoryContract.ProductsEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        InventoryContract.ProductsEntry.COLUMN_NAME_TITLE + " TEXT," +
                        InventoryContract.ProductsEntry.COLUMN_NAME_PRICE + " INTEGER," +
                        InventoryContract.ProductsEntry.COLUMN_NAME_IMAGE + " BLOB," +
                        InventoryContract.ProductsEntry.COLUMN_NAME_SUPPILERNUMBER + " INTEGER," +
                        InventoryContract.ProductsEntry.COLUMN_NAME_QUANTITY + " INTEGER)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + InventoryContract.ProductsEntry.TABLE_NAME;
    }
}
