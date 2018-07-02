package com.example.android.shoestore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

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
}
