package com.example.hospital;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ConsultaAdapter extends RecyclerView.Adapter<ConsultaAdapter.ConsultaViewHolder> {

    private Context context;
    private List<ConsultaModel> consultaList;

    public ConsultaAdapter(Context context, List<ConsultaModel> consultaList) {
        this.context = context;
        this.consultaList = consultaList;
    }

    @NonNull
    @Override
    public ConsultaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_consulta, parent, false);
        return new ConsultaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultaViewHolder holder, int position) {
        ConsultaModel consulta = consultaList.get(position);

        holder.tvFechaConsulta.setText("Fecha: " + (consulta.getFechaConsultaProgramada() != null ? consulta.getFechaConsultaProgramada() : "N/A"));
        holder.tvSintomas.setText("Síntomas: " + (consulta.getSintomas() != null ? consulta.getSintomas() : "N/A"));
        holder.tvDiagnostico.setText("Diagnóstico: " + (consulta.getDiagnostico() != null && !consulta.getDiagnostico().isEmpty() ? consulta.getDiagnostico() : "Pendiente"));
        holder.tvTratamiento.setText("Tratamiento: " + (consulta.getTratamiento() != null && !consulta.getTratamiento().isEmpty() ? consulta.getTratamiento() : "Pendiente"));
        holder.tvEstado.setText("Estado: " + (consulta.getEstado() != null ? consulta.getEstado() : "N/A"));

    }

    @Override
    public int getItemCount() {
        return consultaList.size();
    }

    public static class ConsultaViewHolder extends RecyclerView.ViewHolder {
        TextView tvFechaConsulta, tvSintomas, tvDiagnostico, tvTratamiento, tvEstado;

        public ConsultaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFechaConsulta = itemView.findViewById(R.id.textViewItemFechaConsulta);
            tvSintomas = itemView.findViewById(R.id.textViewItemSintomas);
            tvDiagnostico = itemView.findViewById(R.id.textViewItemDiagnostico);
            tvTratamiento = itemView.findViewById(R.id.textViewItemTratamiento);
            tvEstado = itemView.findViewById(R.id.textViewItemEstadoConsulta);
        }
    }

    public void actualizarConsultas(List<ConsultaModel> nuevasConsultas) {
        this.consultaList.clear();
        if (nuevasConsultas != null) {
            this.consultaList.addAll(nuevasConsultas);
        }
        notifyDataSetChanged();
    }
}