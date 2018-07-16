package com.example.android.shoestore;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shoestore.data.ShoesContract.ShoeEntry;
import com.example.android.shoestore.data.ShoesDbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class CharacteristicActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

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
    @BindView(R.id.supplier_number)
    EditText supplierNumber;
    @BindView(R.id.quantity)
    TextView quantity;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.text_supplier)
    TextView textSupplier;
    @BindView(R.id.minus_button)
    Button minusButton;
    @BindView(R.id.plus_button)
    Button plusButton;

    private int genderType;

    private ShoesDbHelper dbHelper;

    private Uri currentShoesUri;

    private static final int EXISTING_SHOES_LOADER = 0;

    private boolean shoesHasChanged = false;

    private int quantityValue = 1;

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
        currentShoesUri = intent.getData();

        if (currentShoesUri == null) {
            setTitle(getString(R.string.add_pair));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_pair));
            getLoaderManager().initLoader(EXISTING_SHOES_LOADER, null, this);
        }

        editTextType.setOnTouchListener(touchListener);
        editTextColour.setOnTouchListener(touchListener);
        editTextSize.setOnTouchListener(touchListener);
        minusButton.setOnTouchListener(touchListener);
        plusButton.setOnTouchListener(touchListener);
        supplierNumber.setOnTouchListener(touchListener);
        spinnerGender.setOnTouchListener(touchListener);

    }

    private boolean saveShoes() {

        boolean allesKlar = false;

        String typeString = editTextType.getText().toString().trim();
        String colourString = editTextColour.getText().toString().trim();
        String sizeString = editTextSize.getText().toString().trim();
        String quantityString = quantity.getText().toString();
        String numberString = supplierNumber.getText().toString();

        if (currentShoesUri == null &&
                TextUtils.isEmpty(typeString) &&
                TextUtils.isEmpty(colourString) &&
                TextUtils.isEmpty(sizeString) &&
                TextUtils.isEmpty(quantityString) &&
                TextUtils.isEmpty(numberString) &&
                genderType == ShoeEntry.GENDER_TYPE_METRO) {
            allesKlar = true;
            return allesKlar;
        }

        ContentValues values = new ContentValues();

        values.put(ShoeEntry.COLUMN_TYPE, typeString);
        if (TextUtils.isEmpty(typeString)) {
            Toast.makeText(this, getString(R.string.type_req), Toast.LENGTH_SHORT).show();
            return allesKlar;
        }

        values.put(ShoeEntry.COLUMN_COLOUR, colourString);
        if (TextUtils.isEmpty(colourString)) {
            Toast.makeText(this, getString(R.string.supp_name_req), Toast.LENGTH_SHORT).show();
            return allesKlar;
        }

        values.put(ShoeEntry.COLUMN_GENDER, genderType);

        values.put(ShoeEntry.COLUMN_QUANTITY, quantityString);
        if (TextUtils.isEmpty(quantityString) || Integer.parseInt(quantityString) == 0){
            Toast.makeText(this, getString(R.string.shoe_quantity),Toast.LENGTH_SHORT).show();
            return allesKlar;
        }
        values.put(ShoeEntry.COLUMN_NUMBER, numberString);
        if (TextUtils.isEmpty(numberString)){
            Toast.makeText(this, getString(R.string.number_req),Toast.LENGTH_SHORT).show();
            return allesKlar;
        }

        int size = 0;
        if (!TextUtils.isEmpty(sizeString)) {
            size = Integer.parseInt(sizeString);
        }
        values.put(ShoeEntry.COLUMN_SIZE, size);
        if (TextUtils.isEmpty(sizeString)) {
            Toast.makeText(this, R.string.shoes_price, Toast.LENGTH_SHORT).show();
            return allesKlar;
        }
        if (currentShoesUri == null) {
            Uri newUri = getContentResolver().insert(ShoeEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.error_with_saving),
                        Toast.LENGTH_SHORT).show();
            } else {
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
        allesKlar = true;
        return allesKlar;
    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.gender_array,
                android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderSpinnerAdapter);
    }


    @OnItemSelected(R.id.spinnerGender)
    public void setSpinnerData(AdapterView<?> parent, int position) {
        String genderSelect = (String) parent.getItemAtPosition(position);
        if (!TextUtils.isEmpty(genderSelect)) {
            if (genderSelect.equals(R.string.gender_male)) {
                genderType = ShoeEntry.GENDER_TYPE_MALE;
            } else if (genderSelect.equals(R.string.gender_female)) {
                genderType = ShoeEntry.GENDER_TYPE_FEMALE;
            } else {
                genderType = ShoeEntry.GENDER_TYPE_METRO;
            }
        }
    }

    @OnItemSelected(value = R.id.spinnerGender, callback = OnItemSelected.Callback.NOTHING_SELECTED)
    public void nothingSelected() {
        genderType = ShoeEntry.GENDER_TYPE_METRO;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_characteristic, menu);
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
        switch (item.getItemId()) {
            case R.id.save_button:
                if (saveShoes()) {
                    finish();
                }
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                currentShoesUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int typeColumnIndex = cursor.getColumnIndex(ShoeEntry.COLUMN_TYPE);
            int colourColumnIndex = cursor.getColumnIndex(ShoeEntry.COLUMN_COLOUR);
            int genderColumnIndex = cursor.getColumnIndex(ShoeEntry.COLUMN_GENDER);
            int sizeColumnIndex = cursor.getColumnIndex(ShoeEntry.COLUMN_SIZE);
            int quantityColumnIndex = cursor.getColumnIndex(ShoeEntry.COLUMN_QUANTITY);
            int numberColumnIndex = cursor.getColumnIndex(ShoeEntry.COLUMN_NUMBER);

            String type = cursor.getString(typeColumnIndex);
            String colour = cursor.getString(colourColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            int size = cursor.getInt(sizeColumnIndex);
            quantityValue = cursor.getInt(quantityColumnIndex);
            int number = cursor.getInt(numberColumnIndex);


            editTextType.setText(type);
            editTextColour.setText(colour);
            editTextSize.setText(Integer.toString(size));
            quantity.setText(Integer.toString(quantityValue));
            supplierNumber.setText(Integer.toString(number));

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
    public void onLoaderReset(Loader<Cursor> loader) {
        editTextType.setText("");
        editTextColour.setText("");
        editTextSize.setText("");
        spinnerGender.setSelection(0);
        quantity.setText("1");
        supplierNumber.setText("");
    }

    @OnClick(R.id.button2)
    public void onViewClicked() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", supplierNumber.getText().toString(), null));
        startActivity(intent);
    }

    @OnClick(R.id.minus_button)
    public void onMinusButtonClicked() {
        if (quantityValue <= 1 ) {
            quantityValue = 1;
            displayQuantity();
            Toast.makeText(this, R.string.lower_than, Toast.LENGTH_SHORT).show();
        } else {
            quantityValue--;
            displayQuantity();
        }
    }

    @OnClick(R.id.plus_button)
    public void onPlusButtonClicked() {
        quantityValue++;
        displayQuantity();
    }

    public void displayQuantity() {
        quantity.setText(String.valueOf(quantityValue));
    }
}
