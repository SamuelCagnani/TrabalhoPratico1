package com.example.trabalhopratico1_samueldemellocagnani;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class DemandActivity extends AppCompatActivity {

    private TextInputEditText inputTitulo, inputDescricao, inputLocal;
    private RadioGroup radioTipo;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_demand);

        dbHelper = new DatabaseHelper(this);

        inputTitulo = findViewById(R.id.inputTitulo);
        inputDescricao = findViewById(R.id.inputDescricao);
        inputLocal = findViewById(R.id.inputLocal);
        radioTipo = findViewById(R.id.radioTipo);

        MaterialButton btnEnviar = findViewById(R.id.btnEnviar);
        MaterialButton btnVoltar = findViewById(R.id.btnVoltar);

        btnEnviar.setOnClickListener(v -> salvarChamado());
        btnVoltar.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void salvarChamado() {
        String titulo = inputTitulo.getText().toString();
        String descricao = inputDescricao.getText().toString();
        String local = inputLocal.getText().toString();

        int selectedId = radioTipo.getCheckedRadioButtonId();
        if (titulo.isEmpty() || descricao.isEmpty() || local.isEmpty() || selectedId == -1) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton radioSelecionado = findViewById(selectedId);
        String tipo = radioSelecionado.getText().toString();

        Chamado chamado = new Chamado();
        chamado.setTitulo(titulo);
        chamado.setDescricao(descricao);
        chamado.setLocal(local);
        chamado.setTipo(tipo);
        chamado.setStatus("Em aberto");
        chamado.setDataCriacao(System.currentTimeMillis());

        long id = dbHelper.inserirChamado(chamado);

        if (id != -1) {
            Toast.makeText(this, "Chamado registrado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao registrar chamado", Toast.LENGTH_SHORT).show();
        }
    }
}
