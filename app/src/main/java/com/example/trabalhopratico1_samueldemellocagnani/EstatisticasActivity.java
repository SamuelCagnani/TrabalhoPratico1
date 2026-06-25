package com.example.trabalhopratico1_samueldemellocagnani;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class EstatisticasActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView txtTotal, txtAbertos, txtAndamento, txtConcluidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);

        dbHelper = new DatabaseHelper(this);

        txtTotal = findViewById(R.id.txtTotal);
        txtAbertos = findViewById(R.id.txtAbertos);
        txtAndamento = findViewById(R.id.txtAndamento);
        txtConcluidos = findViewById(R.id.txtConcluidos);

        MaterialButton btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish());

        carregarEstatisticas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarEstatisticas();
    }

    private void carregarEstatisticas() {
        txtTotal.setText(String.valueOf(dbHelper.getTotalChamados()));
        txtAbertos.setText(String.valueOf(dbHelper.getChamadosAbertos()));
        txtAndamento.setText(String.valueOf(dbHelper.getChamadosEmAndamento()));
        txtConcluidos.setText(String.valueOf(dbHelper.getChamadosConcluidos()));
    }
}
