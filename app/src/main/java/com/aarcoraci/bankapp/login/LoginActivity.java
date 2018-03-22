package com.aarcoraci.bankapp.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.aarcoraci.bankapp.R;
import com.aarcoraci.bankapp.balance.BalanceActivity;

/*
https://dribbble.com/shots/2923916-Veeery-clean-Bank/attachments/606736
 */

public class LoginActivity extends AppCompatActivity {

    private ImageView fingerPrintImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fingerPrintImage = findViewById(R.id.fingerprintImage);
        fingerPrintImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, BalanceActivity.class);
                startActivity(intent);
            }
        });
    }
}
