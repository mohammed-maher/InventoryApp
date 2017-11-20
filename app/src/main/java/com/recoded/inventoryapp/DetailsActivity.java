package com.recoded.inventoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.recoded.inventoryapp.data.InventoryContract;
import com.recoded.inventoryapp.data.InventoryDbHelper;

import java.io.ByteArrayOutputStream;

public class DetailsActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private InventoryDbHelper dbHelper = new InventoryDbHelper(this);
    private ImageView productImage;
    private EditText titleText;
    private EditText priceText;
    private EditText quantityText;
    private EditText suppilerText;
    private Button changeImageButton;
    private Button increaseQuantityButton;
    private Button decreaseQuantityButton;
    private Button saveButton;
    private Button deleteButton;
    private Button orderMoreButton;
    private long itemId;
    private Boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        productImage = findViewById(R.id.product_image);
        titleText = findViewById(R.id.title_edit_text);
        priceText = findViewById(R.id.price_edit_text);
        quantityText = findViewById(R.id.quantity_edit_text);
        suppilerText = findViewById(R.id.suppiler_edit_text);
        increaseQuantityButton = findViewById(R.id.increase_quantity_button);
        decreaseQuantityButton = findViewById(R.id.decrease_quantity_button);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_button);
        orderMoreButton = findViewById(R.id.ordermore_button);
        changeImageButton = findViewById(R.id.change_image_button);

        if (getIntent().hasExtra("itemID")) {
            editMode = true;
            itemId = getIntent().getLongExtra("itemID", 0);
            deleteButton.setVisibility(View.VISIBLE);
            orderMoreButton.setVisibility(View.VISIBLE);
            Cursor c = dbHelper.read(itemId);
            while (c.moveToNext()) {
                byte[] imageBytes = c.getBlob(c.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_IMAGE));
                String title = c.getString(c.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_TITLE));
                int price = c.getInt(c.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_PRICE));
                int quantity = c.getInt(c.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_QUANTITY));
                final String suppilerNumber = c.getString(c.getColumnIndex(InventoryContract.ProductsEntry.COLUMN_NAME_SUPPILERNUMBER));
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                productImage.setImageBitmap(bitmap);
                titleText.setText(title);
                priceText.setText(price + "");
                quantityText.setText(quantity + "");
                suppilerText.setText(suppilerNumber + "");

                orderMoreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialPhoneNumber(suppilerNumber);
                    }
                });

            }

        }

        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = AskOption();
                dialog.show();
            }
        });

        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               increaseQuantity(quantityText);
            }
        });

        decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseQuantity(quantityText);
            }
        });
    }

    public boolean checkEntries() {
        if (titleText.getText().toString().equals("") || priceText.getText().toString().equals("") || quantityText.getText().toString().equals("") || suppilerText.getText().toString().equals("") || productImage.getDrawable() == null) {
            Toast.makeText(this, R.string.fill_fields_message, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void saveData() {
        if (checkEntries()) {
            try {
                BitmapDrawable drawable = (BitmapDrawable) productImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageBytes = stream.toByteArray();

                String title = titleText.getText().toString();
                int price = Integer.valueOf(priceText.getText().toString());
                int quantity = Integer.valueOf(quantityText.getText().toString());
                String suppilerNumber = suppilerText.getText().toString().trim();
                Boolean state;
                if (!editMode) {
                    state = dbHelper.insert(title, price, imageBytes, suppilerNumber, quantity);
                    if (state) {
                        Toast.makeText(this, getString(R.string.item_added_message), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(this, R.string.add_error_message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    state = dbHelper.update(itemId, title, price, imageBytes, suppilerNumber, quantity);
                    if (state) {
                        Toast.makeText(this, getString(R.string.item_update_message), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(this, R.string.add_error_message, Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle(R.string.delete_string)
                .setMessage(R.string.delete_confirmation_message)
                .setIcon(R.drawable.delete)

                .setPositiveButton(R.string.delete_string, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dbHelper.delete(itemId);
                        Toast.makeText(DetailsActivity.this, R.string.delete_confirmation_string, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                        dialog.dismiss();
                    }

                })

                .setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    public void decreaseQuantity(EditText editText){
        if(isNumber(editText)){
         int number = Integer.parseInt(editText.getText().toString());
         if(number > 0){
             editText.setText((number-1)+"");
            }
        }else{
            editText.setText(0+"");
        }
    }

    public void increaseQuantity(EditText editText){
        if(isNumber(editText)){
            int number = Integer.parseInt(editText.getText().toString());
                editText.setText((number+1)+"");
        }else{
            editText.setText(0+"");
        }
    }

    public boolean isNumber(EditText editText){
        String regexStr = "^[0-9]*$";
        String text  = editText.getText().toString();
        if(!text.equals("") && text.trim().matches(regexStr))
        {
            return true;
        }

        return false;
    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            productImage.setImageBitmap(imageBitmap);
        }
    }
}
