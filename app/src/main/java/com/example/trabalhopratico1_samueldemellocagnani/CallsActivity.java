package com.example.trabalhopratico1_samueldemellocagnani;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CallsActivity extends AppCompatActivity {

    private RecyclerView recyclerChamados;
    private ChamadoAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Chamado> listaChamados = new ArrayList<>();
    private TextInputEditText inputBusca;
    private String statusFiltro = "Todos";
    private Long dataInicioFiltro = null;
    private Long dataFimFiltro = null;
    private MaterialButton btnData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calls);

        dbHelper = new DatabaseHelper(this);

        recyclerChamados = findViewById(R.id.recyclerChamados);
        inputBusca = findViewById(R.id.inputBusca);
        btnData = findViewById(R.id.btnData);
        MaterialButton btnStatus = findViewById(R.id.btnStatus);
        MaterialButton btnVoltar = findViewById(R.id.btnVoltar);

        recyclerChamados.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChamadoAdapter(listaChamados, this);
        recyclerChamados.setAdapter(adapter);

        carregarChamados();

        inputBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                carregarChamados();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnData.setOnClickListener(v -> mostrarDatePicker());
        btnStatus.setOnClickListener(v -> mostrarDialogFiltroStatus());
        btnVoltar.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void carregarChamados() {
        String query = inputBusca.getText().toString();
        listaChamados = dbHelper.listarChamados(query, statusFiltro, dataInicioFiltro, dataFimFiltro);
        adapter.updateList(listaChamados);
    }

    private void mostrarDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(year1, monthOfYear, dayOfMonth, 0, 0, 0);
            cal.set(Calendar.MILLISECOND, 0);
            dataInicioFiltro = cal.getTimeInMillis();

            cal.set(year1, monthOfYear, dayOfMonth, 23, 59, 59);
            cal.set(Calendar.MILLISECOND, 999);
            dataFimFiltro = cal.getTimeInMillis();

            btnData.setText(String.format(java.util.Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year1));
            carregarChamados();
        }, year, month, day);

        // Adiciona um botão para limpar o filtro
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEUTRAL, "Limpar", (dialog, which) -> {
            dataInicioFiltro = null;
            dataFimFiltro = null;
            btnData.setText("Filtrar Data");
            carregarChamados();
        });

        datePickerDialog.show();
    }

    private void mostrarDialogFiltroStatus() {
        String[] opcoes = {"Todos", "Em aberto", "Em andamento", "Finalizada"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtrar por Status");
        builder.setItems(opcoes, (dialog, which) -> {
            statusFiltro = opcoes[which];
            carregarChamados();
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarChamados();
    }
}
