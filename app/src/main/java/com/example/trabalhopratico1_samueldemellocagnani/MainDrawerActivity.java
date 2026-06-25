package com.example.trabalhopratico1_samueldemellocagnani;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

public class MainDrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        MaterialButton btnDemanda = findViewById(R.id.btnDemanda);
        MaterialButton btnChamados = findViewById(R.id.btnChamados);
        MaterialButton btnAtendimentos = findViewById(R.id.btnAtendimentos);

        btnDemanda.setOnClickListener(v -> abrirDemanda());
        btnChamados.setOnClickListener(v -> abrirListagem());
        btnAtendimentos.setOnClickListener(v -> abrirAtendimentos());
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_novo_chamado) {
            abrirDemanda();
        } else if (id == R.id.nav_listagem) {
            abrirListagem();
        } else if (id == R.id.nav_atendimentos) {
            abrirAtendimentos();
        } else if (id == R.id.nav_estatisticas) {
            abrirEstatisticas();
        } else if (id == R.id.nav_sobre) {
            abrirSobre();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void abrirDemanda() {
        Intent intent = new Intent(this, DemandActivity.class);
        startActivity(intent);
    }

    private void abrirListagem() {
        Intent intent = new Intent(this, CallsActivity.class);
        startActivity(intent);
    }

    private void abrirAtendimentos() {
        Intent intent = new Intent(this, CustomerServiceActivity.class);
        startActivity(intent);
    }

    private void abrirEstatisticas() {
        Intent intent = new Intent(this, EstatisticasActivity.class);
        startActivity(intent);
    }

    private void abrirSobre() {
        Intent intent = new Intent(this, SobreActivity.class);
        startActivity(intent);
    }
}
