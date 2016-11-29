package com.djonique.birdays;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    public static final String DETAIL_NAME = "DetailName";
    public static final String DETAIL_PHONE = "DetailPhone";
    public static final String DETAIL_EMAIL = "DetailEmail";
    public static final String DETAIL_DATE = "DetailDate";
    public static final String DETAIL_AGE = "DetailAge";

    TextView tvName, tvPhone, tvEmail, tvDate, tvAge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = ((Toolbar) findViewById(R.id.toolbarDetail));
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitle(" ");
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        tvName = ((TextView) findViewById(R.id.tvDetailName));
        tvPhone = ((TextView) findViewById(R.id.tvDetailPhone));
        tvEmail = ((TextView) findViewById(R.id.tvDetailEmail));
        tvDate = ((TextView) findViewById(R.id.tvDetailDate));
        tvAge = ((TextView) findViewById(R.id.tvDetailAge));

        Intent intent = getIntent();
        String name = intent.getStringExtra(DETAIL_NAME);
        tvName.setText(name);

        long phoneNumber = intent.getLongExtra(DETAIL_PHONE, 0);
        if (phoneNumber == 0) {
            tvPhone.setText(R.string.undefined);
        } else tvPhone.setText(String.valueOf(phoneNumber));

        String email = intent.getStringExtra(DETAIL_EMAIL);
        if (email.equals(" ")) {
            tvEmail.setText(R.string.undefined);
        } else tvEmail.setText(email);

        long date = intent.getLongExtra(DETAIL_DATE, 0);
        tvDate.setText(Utils.getDate(date));

        int age = intent.getIntExtra(DETAIL_AGE, 0);
        tvAge.setText(String.valueOf(age));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_back_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case android.R.id.home:
                this.onBackPressed();
                break;
        }
        return true;
    }
}
