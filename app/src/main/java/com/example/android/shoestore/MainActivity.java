package com.example.android.shoestore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.default_action:
                return true;
            case R.id.add_a_new_pair:
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
