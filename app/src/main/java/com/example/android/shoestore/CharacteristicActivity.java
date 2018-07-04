package com.example.android.shoestore;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characteristic);
        ButterKnife.bind(this);
        setupSpinner();
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
                genderType = 1;
            } else if(genderSelect.equals(R.string.gender_female)){
                genderType = 2;
            } else {
                genderType = 0;
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
