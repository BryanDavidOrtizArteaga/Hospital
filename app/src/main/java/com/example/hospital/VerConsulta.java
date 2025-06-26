// VerConsulta.java
package com.example.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VerConsulta extends AppCompatActivity {

    private RecyclerView recyclerViewConsultas;
    private ConsultaAdapter consultaAdapter;
    private List<ConsultaModel> listaDeConsultas;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference consultasReference;

    private ProgressBar progressBar;
    private TextView textViewNoConsultas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_consulta);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Mis Consultas");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        recyclerViewConsultas = findViewById(R.id.recyclerViewConsultas);
        progressBar = findViewById(R.id.progressBarVerConsultas);
        textViewNoConsultas = findViewById(R.id.textViewNoConsultas);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        consultasReference = FirebaseDatabase.getInstance().getReference("Consultas");

        listaDeConsultas = new ArrayList<>();
        consultaAdapter = new ConsultaAdapter(this, listaDeConsultas);

        recyclerViewConsultas.setHasFixedSize(true);
        recyclerViewConsultas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewConsultas.setAdapter(consultaAdapter);

        if (firebaseUser == null) {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(VerConsulta.this, MainActivity.class));
            finish();
            return;
        }

        cargarConsultasDelUsuario();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarConsultasDelUsuario() {
        progressBar.setVisibility(View.VISIBLE);
        textViewNoConsultas.setVisibility(View.GONE);
        recyclerViewConsultas.setVisibility(View.GONE);

        String uidUsuarioActual = firebaseUser.getUid();

        Query queryConsultasUsuario = consultasReference.orderByChild("uidPaciente").equalTo(uidUsuarioActual);

        queryConsultasUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaDeConsultas.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ConsultaModel consulta = snapshot.getValue(ConsultaModel.class);
                        if (consulta != null) {
                            listaDeConsultas.add(consulta);
                        }
                    }

                    Collections.reverse(listaDeConsultas);
                    consultaAdapter.actualizarConsultas(listaDeConsultas);

                    if (listaDeConsultas.isEmpty()) {
                        textViewNoConsultas.setVisibility(View.VISIBLE);
                        recyclerViewConsultas.setVisibility(View.GONE);
                    } else {
                        textViewNoConsultas.setVisibility(View.GONE);
                        recyclerViewConsultas.setVisibility(View.VISIBLE);
                    }

                } else {
                    textViewNoConsultas.setVisibility(View.VISIBLE);
                    recyclerViewConsultas.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(VerConsulta.this, "Error al cargar consultas: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                textViewNoConsultas.setText("Error al cargar datos.");
                textViewNoConsultas.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}