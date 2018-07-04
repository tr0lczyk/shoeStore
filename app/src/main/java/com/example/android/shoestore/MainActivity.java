package com.example.android.shoestore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    private void insertPet(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShoeEntry.COLUMN_TYPE, "Elegant");
        values.put(ShoeEntry.COLUMN_COLOUR, "Blue");
        values.put(ShoeEntry.COLUMN_GENDER, ShoeEntry.GENDER_TYPE_MALE);
        values.put(ShoeEntry.COLUMN_SIZE, 44);

        long newIdEntry = database.insert(ShoeEntry.TABLE_NAME,null,values);
        Log.v("MainActivity","New entry with ID " + newIdEntry);

    }

    private void displayInfo() {
        SQLiteDatabase dataBase = dbHelper.getReadableDatabase();
        Cursor cursor = dataBase.rawQuery("SELECT * FROM " + ShoeEntry.TABLE_NAME, null);
        try {
            displayInfo.setText("Number of rows in pets database table: " + cursor.getCount());
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
