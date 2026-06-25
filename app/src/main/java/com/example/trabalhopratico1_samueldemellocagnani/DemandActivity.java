package com.example.trabalhopratico1_samueldemellocagnani;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DemandActivity extends AppCompatActivity {

    private static final String TAG = "DemandActivity";

    private TextInputEditText inputTitulo, inputDescricao, inputLocal;
    private RadioGroup radioTipo, radioStatus;
    private DatabaseHelper dbHelper;
    private ImageView imgPreview;
    private MaterialButton btnFoto;
    private String imagemPath = null;
    private Uri fotoUri;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                try {
                    int code = result.getResultCode();
                    if (code == RESULT_OK) {
                        Log.d(TAG, "Foto capturada com sucesso");

                        File imgFile = new File(imagemPath);
                        if (imgFile.exists() && imgFile.length() > 0) {
                            runOnUiThread(() -> {
                                imgPreview.setVisibility(android.view.View.VISIBLE);
                                imgPreview.setImageURI(fotoUri);
                                btnFoto.setText("Alterar Foto");
                            });
                            Log.d(TAG, "Preview exibido. Tamanho: " + imgFile.length() + " bytes");
                        } else {
                            Log.e(TAG, "Arquivo de imagem vazio ou inexistente: " + imagemPath);
                            imagemPath = null;
                            Toast.makeText(DemandActivity.this, "Foto nao foi salva. Tente novamente.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (code == RESULT_CANCELED) {
                        Log.d(TAG, "Usuario cancelou a captura da foto");
                        imagemPath = null;
                    } else {
                        Log.w(TAG, "Resultado inesperado da camera: " + code);
                        imagemPath = null;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Erro no callback da camera: " + e.getMessage(), e);
                    imagemPath = null;
                    runOnUiThread(() -> Toast.makeText(DemandActivity.this,
                            "Erro ao processar foto. Tente novamente.", Toast.LENGTH_SHORT).show());
                }
            });

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
        radioStatus = findViewById(R.id.radioStatus);
        imgPreview = findViewById(R.id.imgPreview);
        btnFoto = findViewById(R.id.btnFoto);

        MaterialButton btnEnviar = findViewById(R.id.btnEnviar);
        MaterialButton btnVoltar = findViewById(R.id.btnVoltar);

        btnFoto.setOnClickListener(v -> abrirCamera());
        btnEnviar.setOnClickListener(v -> salvarChamado());
        btnVoltar.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void abrirCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (intent.resolveActivity(getPackageManager()) == null) {
                Toast.makeText(this, "Camera indisponivel neste dispositivo", Toast.LENGTH_LONG).show();
                return;
            }

            File fotoFile;
            try {
                fotoFile = criarArquivoImagem();
            } catch (IOException e) {
                Log.e(TAG, "Erro ao criar arquivo de imagem: " + e.getMessage(), e);
                Toast.makeText(this, "Erro ao preparar camera. Tente novamente.", Toast.LENGTH_SHORT).show();
                return;
            }

            fotoUri = FileProvider.getUriForFile(this,
                    getPackageName() + ".fileprovider", fotoFile);

            Log.d(TAG, "Abrindo camera. URI: " + fotoUri);
            Log.d(TAG, "Arquivo: " + imagemPath);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            List<ResolveInfo> resInfoList = getPackageManager()
                    .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo info : resInfoList) {
                String packageName = info.activityInfo.packageName;
                grantUriPermission(packageName, fotoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Log.d(TAG, "Permissao concedida para: " + packageName);
            }

            cameraLauncher.launch(intent);

        } catch (Exception e) {
            Log.e(TAG, "Erro ao abrir camera: " + e.getMessage(), e);
            Toast.makeText(this, "Erro ao abrir camera. Verifique as permissoes.", Toast.LENGTH_LONG).show();
        }
    }

    private File criarArquivoImagem() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String nomeArquivo = "IMG_" + timeStamp + "_";
        File dirImagens = new File(getFilesDir(), "imagens");
        if (!dirImagens.exists()) {
            dirImagens.mkdirs();
            Log.d(TAG, "Diretorio imagens criado em: " + dirImagens.getAbsolutePath());
        }
        File imagem = File.createTempFile(nomeArquivo, ".jpg", dirImagens);
        imagemPath = imagem.getAbsolutePath();
        return imagem;
    }

    private void salvarChamado() {
        String titulo = inputTitulo.getText().toString();
        String descricao = inputDescricao.getText().toString();
        String local = inputLocal.getText().toString();

        int selectedId = radioTipo.getCheckedRadioButtonId();
        int selectedStatusId = radioStatus.getCheckedRadioButtonId();

        if (titulo.isEmpty() || descricao.isEmpty() || local.isEmpty() || selectedId == -1 || selectedStatusId == -1) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imagemPath == null || imagemPath.isEmpty()) {
            Toast.makeText(this, "Recomendado: tire uma foto do problema", Toast.LENGTH_LONG).show();
        }

        RadioButton radioSelecionado = findViewById(selectedId);
        String tipo = radioSelecionado.getText().toString();

        String status = "Aberto";
        if (selectedStatusId == R.id.radioAndamento) {
            status = "Em andamento";
        } else if (selectedStatusId == R.id.radioConcluido) {
            status = "Concluído";
        }

        Chamado chamado = new Chamado();
        chamado.setTitulo(titulo);
        chamado.setDescricao(descricao);
        chamado.setLocal(local);
        chamado.setTipo(tipo);
        chamado.setStatus(status);
        chamado.setDataCriacao(System.currentTimeMillis());
        chamado.setImagemPath(imagemPath);

        long id = dbHelper.inserirChamado(chamado);

        if (id != -1) {
            chamado.setId((int) id);
            salvarNaNuvem(chamado);
            Toast.makeText(this, "Chamado registrado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao registrar chamado", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarNaNuvem(Chamado chamado) {
        ParseObject parseChamado = new ParseObject("Chamado");
        parseChamado.put("titulo", chamado.getTitulo());
        parseChamado.put("descricao", chamado.getDescricao());
        parseChamado.put("local", chamado.getLocal());
        parseChamado.put("status", chamado.getStatus());
        parseChamado.put("data_criacao", new Date(chamado.getDataCriacao()));

        if (chamado.getImagemPath() != null && !chamado.getImagemPath().isEmpty()) {
            String nomeArquivo = new File(chamado.getImagemPath()).getName();
            parseChamado.put("imagem_nome", nomeArquivo);
        } else {
            parseChamado.put("imagem_nome", "");
        }

        parseChamado.saveInBackground(e -> {
            if (e == null) {
                String objectId = parseChamado.getObjectId();
                dbHelper.atualizarParseObjectId(chamado.getId(), objectId);
                Log.i(TAG, "Chamado salvo na nuvem. ObjectId: " + objectId);
            } else {
                Log.e(TAG, "Erro ao salvar na nuvem: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(DemandActivity.this,
                        "Salvo localmente, mas falhou ao sincronizar com a nuvem", Toast.LENGTH_LONG).show());
            }
        });
    }
}
