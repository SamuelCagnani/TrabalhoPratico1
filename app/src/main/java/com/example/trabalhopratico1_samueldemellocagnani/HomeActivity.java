package com.example.trabalhopratico1_samueldemellocagnani;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MaterialButton btnVoltar = findViewById(R.id.btnVoltar);
        MaterialButton btnDemanda = findViewById(R.id.btnDemanda);
        MaterialButton btnChamados = findViewById(R.id.btnChamados);
        MaterialButton btnAtendimentos = findViewById(R.id.btnAtendimentos);

        btnDemanda.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, DemandActivity.class);
            startActivity(intent);
        });

        btnChamados.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CallsActivity.class);
            startActivity(intent);
        });

        btnAtendimentos.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CustomerServiceActivity.class);
            startActivity(intent);
        });

        btnVoltar.setOnClickListener(v -> finish());
    }
}