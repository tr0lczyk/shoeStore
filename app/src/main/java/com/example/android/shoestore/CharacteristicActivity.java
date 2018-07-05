package com.example.android.shoestore;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shoestore.data.ShoesContract;
import com.example.android.shoestore.data.ShoesContract.ShoeEntry;
import com.example.android.shoestore.data.ShoesDbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class CharacteristicActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characteristic);
        ButterKnife.bind(this);
        setupSpinner();
        dbHelper = new ShoesDbHelper(this);
    }

    private void insertPet(){
        String typeData = editTextType.getText().toString().trim();
        String colourData = editTextColour.getText().toString().trim();
        int sizeData = Integer.parseInt(editTextSize.getText().toString().trim());

        ContentValues values = new ContentValues();
        values.put(ShoeEntry.COLUMN_TYPE, typeData);
        values.put(ShoeEntry.COLUMN_COLOUR, colourData);
        values.put(ShoeEntry.COLUMN_GENDER, genderType);
        values.put(ShoeEntry.COLUMN_SIZE, sizeData);

        Uri newUri = getContentResolver().insert(ShoeEntry.CONTENT_URI, values);

        if (newUri == null) {
            Toast.makeText(this, getString(R.string.shoes_save_fail),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.save_success),
                    Toast.LENGTH_SHORT).show();
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
        genderType = 0;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_characteristic,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_button:
                insertPet();
                finish();
                return true;
            case R.id.delete_button:
            return true;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
