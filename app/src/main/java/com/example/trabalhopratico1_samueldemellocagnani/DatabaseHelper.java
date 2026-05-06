package com.example.trabalhopratico1_samueldemellocagnani;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ResolvaJa.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CHAMADOS = "chamados";
    private static final String COL_ID = "id";
    private static final String COL_TITULO = "titulo";
    private static final String COL_DESCRICAO = "descricao";
    private static final String COL_LOCAL = "local";
    private static final String COL_TIPO = "tipo";
    private static final String COL_STATUS = "status";
    private static final String COL_DATA = "data_criacao";
    private static final String COL_SOLUCAO = "solucao";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_CHAMADOS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITULO + " TEXT, " +
                COL_DESCRICAO + " TEXT, " +
                COL_LOCAL + " TEXT, " +
                COL_TIPO + " TEXT, " +
                COL_STATUS + " TEXT, " +
                COL_DATA + " INTEGER, " +
                COL_SOLUCAO + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAMADOS);
        onCreate(db);
    }

    public long inserirChamado(Chamado chamado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITULO, chamado.getTitulo());
        values.put(COL_DESCRICAO, chamado.getDescricao());
        values.put(COL_LOCAL, chamado.getLocal());
        values.put(COL_TIPO, chamado.getTipo());
        values.put(COL_STATUS, chamado.getStatus());
        values.put(COL_DATA, chamado.getDataCriacao());
        values.put(COL_SOLUCAO, chamado.getSolucao());

        return db.insert(TABLE_CHAMADOS, null, values);
    }

    public int atualizarChamado(Chamado chamado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STATUS, chamado.getStatus());
        values.put(COL_SOLUCAO, chamado.getSolucao());

        return db.update(TABLE_CHAMADOS, values, COL_ID + " = ?", new String[]{String.valueOf(chamado.getId())});
    }

    public List<Chamado> listarChamados(String query, String status, Long dataInicio, Long dataFim) {
        List<Chamado> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        if (query != null && !query.isEmpty()) {
            selection.append("(").append(COL_TITULO).append(" LIKE ? OR ").append(COL_ID).append(" LIKE ?)");
            selectionArgs.add("%" + query + "%");
            selectionArgs.add("%" + query + "%");
        }

        if (status != null && !status.isEmpty() && !status.equals("Todos")) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append(COL_STATUS).append(" = ?");
            selectionArgs.add(status);
        }

        if (dataInicio != null && dataFim != null) {
            if (selection.length() > 0) selection.append(" AND ");
            selection.append(COL_DATA).append(" BETWEEN ? AND ?");
            selectionArgs.add(String.valueOf(dataInicio));
            selectionArgs.add(String.valueOf(dataFim));
        }

        String sel = selection.length() == 0 ? null : selection.toString();
        String[] args = selectionArgs.isEmpty() ? null : selectionArgs.toArray(new String[0]);

        Cursor cursor = db.query(TABLE_CHAMADOS, null, sel, args, null, null, COL_DATA + " DESC");

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Chamado c = new Chamado(
                        cursor.getInt(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_TITULO)),
                        cursor.getString(cursor.getColumnIndex(COL_DESCRICAO)),
                        cursor.getString(cursor.getColumnIndex(COL_LOCAL)),
                        cursor.getString(cursor.getColumnIndex(COL_TIPO)),
                        cursor.getString(cursor.getColumnIndex(COL_STATUS)),
                        cursor.getLong(cursor.getColumnIndex(COL_DATA)),
                        cursor.getString(cursor.getColumnIndex(COL_SOLUCAO))
                );
                lista.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
}
