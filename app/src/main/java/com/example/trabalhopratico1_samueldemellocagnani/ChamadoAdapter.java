package com.example.trabalhopratico1_samueldemellocagnani;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChamadoAdapter extends RecyclerView.Adapter<ChamadoAdapter.ViewHolder> {

    private List<Chamado> chamados;
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public ChamadoAdapter(List<Chamado> chamados, Context context) {
        this.chamados = chamados;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chamado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chamado chamado = chamados.get(position);
        holder.txtId.setText("#" + String.format("%04d", chamado.getId()));
        holder.txtTitulo.setText(chamado.getTitulo());
        holder.txtLocal.setText("Local: " + chamado.getLocal());
        holder.txtStatus.setText(chamado.getStatus());
        holder.txtData.setText(dateFormat.format(new Date(chamado.getDataCriacao())));

        if (chamado.getImagemPath() != null && !chamado.getImagemPath().isEmpty()) {
            File imgFile = new File(chamado.getImagemPath());
            if (imgFile.exists()) {
                holder.imgMiniatura.setVisibility(View.VISIBLE);
                holder.imgMiniatura.setImageBitmap(
                        BitmapFactory.decodeFile(chamado.getImagemPath()));
            } else {
                holder.imgMiniatura.setVisibility(View.GONE);
            }
        } else {
            holder.imgMiniatura.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SpecificCallActivity.class);
            intent.putExtra("chamado", chamado);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chamados.size();
    }

    public void updateList(List<Chamado> newList) {
        this.chamados = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtTitulo, txtLocal, txtStatus, txtData;
        ImageView imgMiniatura;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtIdChamado);
            txtTitulo = itemView.findViewById(R.id.txtTituloChamado);
            txtLocal = itemView.findViewById(R.id.txtLocalChamado);
            txtStatus = itemView.findViewById(R.id.txtStatusChamado);
            txtData = itemView.findViewById(R.id.txtDataChamado);
            imgMiniatura = itemView.findViewById(R.id.imgMiniatura);
        }
    }
}
