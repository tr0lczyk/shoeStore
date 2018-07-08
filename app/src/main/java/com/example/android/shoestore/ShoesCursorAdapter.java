package com.example.android.shoestore;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shoestore.data.ShoesContract.ShoeEntry;

public class ShoesCursorAdapter extends CursorAdapter {

    public ShoesCursorAdapter(Context context, Cursor c ){
        super(context,c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_two, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView type = view.findViewById(R.id.type_filling);
        TextView price = view.findViewById(R.id.quantity_filling);
        TextView quantityNumber = view.findViewById(R.id.price_filling);
        TextView sale = view.findViewById(R.id.sale_button);

        int typeColumnNumber = cursor.getColumnIndex(ShoeEntry.COLUMN_TYPE);
        int priceColumnNumber = cursor.getColumnIndex(ShoeEntry.COLUMN_SIZE);
        int quantityColumnNumber = cursor.getColumnIndex(ShoeEntry.COLUMN_QUANTITY);

        String typeString = cursor.getString(typeColumnNumber);
        String priceString = cursor.getString(priceColumnNumber);
        String quantityString = cursor.getString(quantityColumnNumber);
        final int quantityToChange = Integer.parseInt(quantityString);
        final Long id = cursor.getLong(cursor.getColumnIndex(ShoeEntry._ID));

        if(TextUtils.isEmpty(typeString)){
            typeString = context.getString(R.string.unknown_type);
        }

        if(TextUtils.isEmpty(priceString)){
            priceString = context.getString(R.string.unknown_colour);
        }

        if(TextUtils.isEmpty(quantityString)){
            quantityString = context.getString(R.string.unknown_quantity);
        }

        type.setText(typeString);
        price.setText(priceString);
        quantityNumber.setText(quantityString);

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = ContentUris.withAppendedId(ShoeEntry.CONTENT_URI, id);
                if (quantityToChange >= 1) {
                    int updatedQuantity = quantityToChange - 1;
                    ContentValues values = new ContentValues();
                    values.put(ShoeEntry.COLUMN_QUANTITY, updatedQuantity);
                    int i = context.getContentResolver().update(uri, values, null,
                            null);
                } else {
                    Toast.makeText(context, context.getString(R.string.no_shoes),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
