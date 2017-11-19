package com.recoded.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.recoded.inventoryapp.data.InventoryContract;
import com.recoded.inventoryapp.data.InventoryDbHelper;

/**
 * Created by Lenovo on 11/17/2017.
 */

public class InventoryCursorAdapter extends CursorAdapter {
    Context mContext;
    public InventoryCursorAdapter(Context context, Cursor c)
    {
        super(context, c, 0);
        this.mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.product_list_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView titleText= view.findViewById(R.id.title_text_view);
        TextView priceText= view.findViewById(R.id.price_text_view);
        ImageView imageView = view.findViewById(R.id.item_image);
        TextView quantityText = view.findViewById(R.id.quantity_text_view);
        Button sellButton = view.findViewById(R.id.order_button);

        final long itemId = cursor.getLong(cursor.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_ID));
        String title = cursor.getString(cursor.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_TITLE));
        int price = cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE));
        byte[] image = cursor.getBlob(cursor.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_IMAGE));
        int quantity = cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_QUANTITY));

        titleText.setText(title);
        priceText.setText("$"+price);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image , 0, image .length);
        imageView.setImageBitmap(bitmap);
        quantityText.setText(quantity+"");

        if(quantity <= 0){
            sellButton.setEnabled(false);
            sellButton.setText(R.string.sold_out_string);
        }


        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof MainActivity) {
                    InventoryDbHelper dbHelper = new InventoryDbHelper(context);
                    dbHelper.decreaseQuantity(itemId);
                    ((MainActivity)mContext).populateList();
                }
            }
        });

    }
}
