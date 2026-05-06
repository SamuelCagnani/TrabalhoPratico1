package com.example.trabalhopratico1_samueldemellocagnani;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class SpecificCallActivity extends AppCompatActivity {

    private Chamado chamado;
    private DatabaseHelper dbHelper;
    private RadioGroup radioStatus;
    private TextInputEditText inputSolucao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_specific_call);

        dbHelper = new DatabaseHelper(this);
        chamado = (Chamado) getIntent().getSerializableExtra("chamado");

        TextView txtNumeroChamado = findViewById(R.id.txtNumeroChamado);
        radioStatus = findViewById(R.id.radioStatus);
        inputSolucao = findViewById(R.id.inputSolucao);
        MaterialButton btnEnviar = findViewById(R.id.btnEnviar);
        MaterialButton btnVoltar = findViewById(R.id.btnVoltar);

        if (chamado != null) {
            txtNumeroChamado.setText("#" + String.format(Locale.getDefault(), "%04d", chamado.getId()));
            inputSolucao.setText(chamado.getSolucao());

            if ("Em aberto".equals(chamado.getStatus())) {
                radioStatus.check(R.id.radioAberto);
            } else if ("Em andamento".equals(chamado.getStatus())) {
                radioStatus.check(R.id.radioAndamento);
            } else if ("Finalizada".equals(chamado.getStatus())) {
                radioStatus.check(R.id.radioFinalizada);
            }
        }

        btnEnviar.setOnClickListener(v -> salvarAlteracoes());
        btnVoltar.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void salvarAlteracoes() {
        int selectedId = radioStatus.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Selecione um status", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton radioSelecionado = findViewById(selectedId);
        chamado.setStatus(radioSelecionado.getText().toString());
        chamado.setSolucao(inputSolucao.getText().toString());

        int rows = dbHelper.atualizarChamado(chamado);
        if (rows > 0) {
            Toast.makeText(this, "Chamado atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao atualizar chamado", Toast.LENGTH_SHORT).show();
        }
    }
}
