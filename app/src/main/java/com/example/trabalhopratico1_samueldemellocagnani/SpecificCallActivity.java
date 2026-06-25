package com.example.trabalhopratico1_samueldemellocagnani;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.util.Locale;

public class SpecificCallActivity extends AppCompatActivity {

    private static final String TAG = "SpecificCall";

    private Chamado chamado;
    private DatabaseHelper dbHelper;
    private RadioGroup radioStatus;
    private TextInputEditText inputSolucao;

    @Override
    @SuppressWarnings("deprecation")
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
        ImageView imgChamado = findViewById(R.id.imgChamado);

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
                if (status.equals("Aberto")) {
                    radioStatus.check(R.id.radioAberto);
                } else if (status.equals("Em andamento")) {
                    radioStatus.check(R.id.radioAndamento);
                } else if (status.equals("Concluído")) {
                    radioStatus.check(R.id.radioFinalizada);
                }
            }

            if (chamado.getImagemPath() != null && !chamado.getImagemPath().isEmpty()) {
                File imgFile = new File(chamado.getImagemPath());
                if (imgFile.exists()) {
                    imgChamado.setVisibility(android.view.View.VISIBLE);
                    Uri uri = FileProvider.getUriForFile(this,
                            getPackageName() + ".fileprovider", imgFile);
                    imgChamado.setImageURI(uri);
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

        String novoStatus = "Aberto";
        if (selectedId == R.id.radioAndamento) {
            novoStatus = "Em andamento";
        } else if (selectedId == R.id.radioFinalizada) {
            novoStatus = "Concluído";
        }

        chamado.setStatus(novoStatus);
        chamado.setSolucao(inputSolucao.getText().toString());

        int rows = dbHelper.atualizarChamado(chamado);
        if (rows > 0) {
            atualizarNaNuvem(chamado);
            Toast.makeText(this, "Chamado atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao atualizar chamado", Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarNaNuvem(Chamado chamado) {
        String parseId = chamado.getParseObjectId();
        if (parseId != null && !parseId.isEmpty()) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Chamado");
            query.getInBackground(parseId, (object, e) -> {
                if (e == null) {
                    object.put("status", chamado.getStatus());
                    object.put("descricao", chamado.getDescricao());
                    object.saveInBackground(err -> {
                        if (err != null) {
                            Log.e(TAG, "Erro ao atualizar na nuvem: " + err.getMessage());
                        }
                    });
                } else {
                    Log.e(TAG, "Erro ao buscar na nuvem: " + e.getMessage());
                    runOnUiThread(() -> Toast.makeText(this,
                            "Nao foi possivel sincronizar com a nuvem", Toast.LENGTH_SHORT).show());
                }
            });
        }
    }
}
