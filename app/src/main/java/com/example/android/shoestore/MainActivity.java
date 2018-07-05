package com.example.android.shoestore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.android.shoestore.data.ShoesContract.ShoeEntry;
import com.example.android.shoestore.data.ShoesDbHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.displayInfo)
    TextView displayInfo;

    private ShoesDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dbHelper = new ShoesDbHelper(this);
        displayInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayInfo();
    }

    private void insertPet(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShoeEntry.COLUMN_TYPE, "Elegant");
        values.put(ShoeEntry.COLUMN_COLOUR, "Blue");
        values.put(ShoeEntry.COLUMN_GENDER, ShoeEntry.GENDER_TYPE_MALE);
        values.put(ShoeEntry.COLUMN_SIZE, 44);

        long newIdEntry = database.insert(ShoeEntry.TABLE_NAME,null,values);
        Log.v("MainActivity","New entry with ID " + newIdEntry);

        Uri newUri = getContentResolver().insert(ShoeEntry.CONTENT_URI, values);
    }

    private void displayInfo() {
//        SQLiteDatabase dataBase = dbHelper.getReadableDatabase();

        String[] project = {
                ShoeEntry._ID,
                ShoeEntry.COLUMN_TYPE,
                ShoeEntry.COLUMN_COLOUR,
                ShoeEntry.COLUMN_GENDER,
                ShoeEntry.COLUMN_SIZE
        };

        Cursor cursor = getContentResolver().query(ShoeEntry.CONTENT_URI,
                project,
                null,
                null,
                null);


        try {
            displayInfo.setText("Number of rows in shoes database table: " + cursor.getCount() + " shoes.\n\n");
            displayInfo.append(ShoeEntry._ID + " - " + ShoeEntry.COLUMN_TYPE + " - " +
                    ShoeEntry.COLUMN_COLOUR + " - " +  ShoeEntry.COLUMN_GENDER + " - " +
                    ShoeEntry.COLUMN_SIZE + "\n");

            int idColumnNumber = cursor.getColumnIndex(ShoeEntry._ID);
            int typeColumnNumber = cursor.getColumnIndex(ShoeEntry.COLUMN_TYPE);
            int colourColumnNumber = cursor.getColumnIndex(ShoeEntry.COLUMN_COLOUR);
            int genderColumnNumber = cursor.getColumnIndex(ShoeEntry.COLUMN_GENDER);
            int sizeColumnNumber = cursor.getColumnIndex(ShoeEntry.COLUMN_SIZE);

            while (cursor.moveToNext()){
                int currentId = cursor.getInt(idColumnNumber);
                String currentType = cursor.getString(typeColumnNumber);
                String currentColour = cursor.getString(colourColumnNumber);
                int currentGender = cursor.getInt(genderColumnNumber);
                int currentSize = cursor.getInt(sizeColumnNumber);
                displayInfo.append(("\n" + currentId + " - " +
                        currentType + " - " +
                        currentColour + " - " +
                        currentGender + " - " +
                        currentSize));
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.default_action:
                insertPet();
                displayInfo();
                return true;
            case R.id.delete_all:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.floatingActionButton)
    public void onFabClicked() {
        Intent intent = new Intent(this, CharacteristicActivity.class);
        startActivity(intent);
    }
}
