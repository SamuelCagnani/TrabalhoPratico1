package com.example.trabalhopratico1_samueldemellocagnani;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CustomerServiceActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ChamadoAdapter adapter;
    private List<Chamado> listaChamados = new ArrayList<>();
    private TextView txtQtdChamados;
    private TextInputEditText inputBusca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_service);

        dbHelper = new DatabaseHelper(this);

        txtQtdChamados = findViewById(R.id.txtQtdChamados);
        inputBusca = findViewById(R.id.inputBusca);
        RecyclerView recyclerChamadosAbertos = findViewById(R.id.recyclerChamadosAbertos);
        MaterialButton btnVoltar = findViewById(R.id.btnVoltar);

        recyclerChamadosAbertos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChamadoAdapter(listaChamados, this);
        recyclerChamadosAbertos.setAdapter(adapter);

        carregarChamadosFila();

        inputBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                carregarChamadosFila();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnVoltar.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void carregarChamadosFila() {
        String query = inputBusca.getText().toString();
        // Na fila, mostramos apenas os que estão "Aberto"
        listaChamados = dbHelper.listarChamados(query, "Aberto", null, null);
        adapter.updateList(listaChamados);
        txtQtdChamados.setText(String.valueOf(listaChamados.size()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarChamadosFila();
    }
}
