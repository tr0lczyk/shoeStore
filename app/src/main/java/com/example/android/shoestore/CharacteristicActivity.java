package com.example.android.shoestore;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.shoestore.data.ShoesContract.ShoeEntry;
import com.example.android.shoestore.data.ShoesDbHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class CharacteristicActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    @BindView(R.id.textViewColour)
    TextView textViewColour;
    @BindView(R.id.textViewType)
    TextView textViewType;
    @BindView(R.id.textViewGender)
    TextView textViewGender;
    @BindView(R.id.textViewSize)
    TextView textViewSize;
    @BindView(R.id.editTextType)
    EditText editTextType;
    @BindView(R.id.editTextColour)
    EditText editTextColour;
    @BindView(R.id.editTextSize)
    EditText editTextSize;
    @BindView(R.id.spinnerGender)
    Spinner spinnerGender;

    private int genderType;

    private ShoesDbHelper dbHelper;

    private Uri currentShoesUri;

    private static final int EXISTING_SHOES_LOADER = 0;

    private boolean shoesHasChanged = false;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            shoesHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characteristic);
        ButterKnife.bind(this);
        setupSpinner();
        dbHelper = new ShoesDbHelper(this);

        Intent intent = getIntent();
        Uri currentShoesUri = intent.getData();

        if (currentShoesUri == null){
            setTitle(getString(R.string.add_pair));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_pair));
            getLoaderManager().initLoader(EXISTING_SHOES_LOADER, null,  this);
        }

        editTextType.setOnTouchListener(touchListener);
        editTextColour.setOnTouchListener(touchListener);
        editTextSize.setOnTouchListener(touchListener);
        spinnerGender.setOnTouchListener(touchListener);
    }

    private void saveShoes() {
        String typeString = editTextType.getText().toString().trim();
        String colourString = editTextColour.getText().toString().trim();
        String sizeString = editTextSize.getText().toString().trim();

        if (currentShoesUri == null &&
                TextUtils.isEmpty(typeString) && TextUtils.isEmpty(colourString) &&
                TextUtils.isEmpty(sizeString) && genderType == ShoeEntry.GENDER_TYPE_METRO) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(ShoeEntry.COLUMN_TYPE, typeString);
        values.put(ShoeEntry.COLUMN_COLOUR, colourString);
        values.put(ShoeEntry.COLUMN_GENDER, genderType);
        int size = 0;
        if (!TextUtils.isEmpty(sizeString)) {
            size = Integer.parseInt(sizeString);
        }
        values.put(ShoeEntry.COLUMN_SIZE, size);
        if (currentShoesUri == null) {
            Uri newUri = getContentResolver().insert(ShoeEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.error_with_saving),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.pair_saved),
                        Toast.LENGTH_SHORT).show();
            }

        } else {

            int rowsAffected = getContentResolver().update(currentShoesUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_fail),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_success),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupSpinner(){
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.gender_array,
                android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderSpinnerAdapter);
    }

    @OnItemSelected(R.id.spinnerGender)
    public void setSpinnerData(AdapterView<?> parent, int position){
        String genderSelect = (String) parent.getItemAtPosition(position);
        if (!TextUtils.isEmpty(genderSelect)){
            if(genderSelect.equals(R.string.gender_male)){
                genderType = ShoeEntry.GENDER_TYPE_MALE;
            } else if(genderSelect.equals(R.string.gender_female)){
                genderType = ShoeEntry.GENDER_TYPE_FEMALE;
            } else {
                genderType = ShoeEntry.GENDER_TYPE_METRO;
            }
        }
    }

    @OnItemSelected(value = R.id.spinnerGender, callback = OnItemSelected.Callback.NOTHING_SELECTED)
    public void nothingSelected(){
        genderType = ShoeEntry.GENDER_TYPE_METRO;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_characteristic,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentShoesUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_button);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_button:
                saveShoes();
                finish();
                return true;
            case R.id.delete_button:
                showDeleteConfirmationDialog();
                return true;
            case R.id.home:
                if (!shoesHasChanged) {
                    NavUtils.navigateUpFromSameTask(CharacteristicActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(CharacteristicActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.discard_and_quit);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.continue_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_this_pair);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteShoes();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteShoes() {
        if (currentShoesUri != null) {
            int rowsDeleted = getContentResolver().delete(currentShoesUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_fail),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_success),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (!shoesHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] project = {
                ShoeEntry._ID,
                ShoeEntry.COLUMN_TYPE,
                ShoeEntry.COLUMN_COLOUR,
                ShoeEntry.COLUMN_GENDER,
                ShoeEntry.COLUMN_SIZE
        };

        return new CursorLoader(this,
                currentShoesUri,
                project,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int typeColumnIndex = cursor.getColumnIndex(ShoeEntry.COLUMN_TYPE);
            int colourColumnIndex = cursor.getColumnIndex(ShoeEntry.COLUMN_COLOUR);
            int genderColumnIndex = cursor.getColumnIndex(ShoeEntry.COLUMN_GENDER);
            int sizeColumnIndex = cursor.getColumnIndex(ShoeEntry.COLUMN_SIZE);

            String name = cursor.getString(typeColumnIndex);
            String breed = cursor.getString(colourColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            int size = cursor.getInt(sizeColumnIndex);

            editTextType.setText(name);
            editTextColour.setText(breed);
            editTextSize.setText(Integer.toString(size));

            switch (gender) {
                case ShoeEntry.GENDER_TYPE_MALE:
                    spinnerGender.setSelection(1);
                    break;
                case ShoeEntry.GENDER_TYPE_FEMALE:
                    spinnerGender.setSelection(2);
                    break;
                default:
                    spinnerGender.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        editTextType.setText("");
        editTextColour.setText("");
        editTextSize.setText("");
        spinnerGender.setSelection(0);
    }
}
