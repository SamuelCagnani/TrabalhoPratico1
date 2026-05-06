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
        TextView txtTituloDetalhe = findViewById(R.id.txtTituloDetalhe);
        TextView txtLocalDetalhe = findViewById(R.id.txtLocalDetalhe);
        TextView txtDescricaoDetalhe = findViewById(R.id.txtDescricaoDetalhe);
        
        radioStatus = findViewById(R.id.radioStatus);
        inputSolucao = findViewById(R.id.inputSolucao);
        MaterialButton btnEnviar = findViewById(R.id.btnEnviar);
        MaterialButton btnVoltar = findViewById(R.id.btnVoltar);

        if (chamado != null) {
            txtNumeroChamado.setText("#" + String.format(Locale.getDefault(), "%04d", chamado.getId()));
            txtTituloDetalhe.setText(chamado.getTitulo());
            txtLocalDetalhe.setText("Local: " + chamado.getLocal());
            txtDescricaoDetalhe.setText(chamado.getDescricao());
            inputSolucao.setText(chamado.getSolucao());

            String status = chamado.getStatus();
            if (status != null) {
                if (status.contains("Em aberto")) {
                    radioStatus.check(R.id.radioAberto);
                } else if (status.contains("Andamento")) {
                    radioStatus.check(R.id.radioAndamento);
                } else if (status.contains("Finalizada")) {
                    radioStatus.check(R.id.radioFinalizada);
                }
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

        String novoStatus = "Em aberto";
        if (selectedId == R.id.radioAndamento) {
            novoStatus = "Em andamento";
        } else if (selectedId == R.id.radioFinalizada) {
            novoStatus = "Finalizada";
        }

        chamado.setStatus(novoStatus);
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
