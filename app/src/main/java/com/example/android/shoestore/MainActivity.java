package com.example.android.shoestore;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.android.shoestore.data.ShoesContract.ShoeEntry;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.empty_view_title)
    TextView emptyViewTitle;
    @BindView(R.id.empty_view_subtitle)
    TextView emptyViewSubtitle;
    @BindView(R.id.empty_layout)
    ConstraintLayout emptyLayout;

    private static int SHOES_LOADER = 0;

    ShoesCursorAdapter shoesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CharacteristicActivity.class);
                startActivity(intent);
            }
        });

        ListView shoesListView = findViewById(R.id.shoes_list);
        shoesListView.setEmptyView(emptyLayout);
        shoesAdapter = new ShoesCursorAdapter(this, null);
        shoesListView.setAdapter(shoesAdapter);

        shoesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, CharacteristicActivity.class);
                Uri currentShoesUri = ContentUris.withAppendedId(ShoeEntry.CONTENT_URI, id);
                intent.setData(currentShoesUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(SHOES_LOADER, null, this);
    }

    private void insertShoes() {
        ContentValues values = new ContentValues();
        values.put(ShoeEntry.COLUMN_TYPE, "Elegant");
        values.put(ShoeEntry.COLUMN_COLOUR, "Blue");
        values.put(ShoeEntry.COLUMN_GENDER, ShoeEntry.GENDER_TYPE_MALE);
        values.put(ShoeEntry.COLUMN_SIZE, 44);

        Uri newUri = getContentResolver().insert(ShoeEntry.CONTENT_URI, values);
    }

    private void deleteAllShoes() {
        int rowsDeleted = getContentResolver().delete(ShoeEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from shoes database");
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
                insertShoes();
                return true;
            case R.id.delete_all:
                deleteAllShoes();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ShoeEntry._ID,
                ShoeEntry.COLUMN_TYPE,
                ShoeEntry.COLUMN_COLOUR};

        return new CursorLoader(this,
                ShoeEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        shoesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        shoesAdapter.swapCursor(null);
    }
}