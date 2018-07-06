package com.example.android.shoestore;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.android.shoestore.data.ShoesContract.ShoeEntry;

public class ShoesCursorAdapter extends CursorAdapter {

    public ShoesCursorAdapter(Context context, Cursor c ){
        super(context,c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView type = view.findViewById(R.id.type);
        TextView colour = view.findViewById(R.id.colour);

        int typeColumnNumber = cursor.getColumnIndex(ShoeEntry.COLUMN_TYPE);
        int colourColumnNumber = cursor.getColumnIndex(ShoeEntry.COLUMN_COLOUR);

        String typeString = cursor.getString(typeColumnNumber);
        String colourString = cursor.getString(colourColumnNumber);

        if(TextUtils.isEmpty(typeString)){
            typeString = context.getString(R.string.unknown_type);
        }

        if(TextUtils.isEmpty(colourString)){
            colourString = context.getString(R.string.unknown_colour);
        }

        type.setText(typeString);
        colour.setText(colourString);
    }
}
