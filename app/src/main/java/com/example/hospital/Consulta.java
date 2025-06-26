// Consulta.java
package com.example.hospital;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Consulta extends AppCompatActivity {

    TextView textViewUidPaciente, textViewNombrePaciente, textViewCorreoPaciente, textViewFechaHoraActual;
    EditText editTextSintomas, editTextDiagnostico, editTextTratamiento;
    TextView textViewFechaSeleccionada;
    Button botonCalendario, botonGuardarConsulta;
    TextView textViewEstado;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference usuariosReference;
    DatabaseReference consultasReference;

    ProgressDialog progressDialog;

    private String fechaConsultaSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Registrar Consulta");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        textViewUidPaciente = findViewById(R.id.Uid_Paciente);
        textViewNombrePaciente = findViewById(R.id.Nombre_Paciente);
        textViewCorreoPaciente = findViewById(R.id.Correo_Paciente);
        textViewFechaHoraActual = findViewById(R.id.Fecha_Hora);
        editTextSintomas = findViewById(R.id.Sintomas);
        editTextDiagnostico = findViewById(R.id.Diagnostico);
        editTextTratamiento = findViewById(R.id.Tratamiento);
        botonCalendario = findViewById(R.id.Calendario);
        textViewFechaSeleccionada = findViewById(R.id.Fecha);
        textViewEstado = findViewById(R.id.Estado);
        botonGuardarConsulta = findViewById(R.id.Boton_Guardar_Consulta);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        usuariosReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        consultasReference = FirebaseDatabase.getInstance().getReference("Consultas");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor espera");
        progressDialog.setCanceledOnTouchOutside(false);

        if (firebaseUser != null) {
            cargarInformacionUsuario();
            SimpleDateFormat dateFormatRegistro = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            String fechaHoraRegistroActual = dateFormatRegistro.format(new Date());
            textViewFechaHoraActual.setText("Registro el: " + fechaHoraRegistroActual);
        } else {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(Consulta.this, MainActivity.class));
            return;
        }

        botonCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoCalendario();
            }
        });

        botonGuardarConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarYGuardarConsulta();
            }
        });
    }

    private void cargarInformacionUsuario() {
        String uid = firebaseUser.getUid();
        textViewUidPaciente.setText("UID: " + uid);

        usuariosReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String correo = snapshot.child("correo").getValue(String.class);

                    if (nombre != null) {
                        textViewNombrePaciente.setText("Nombre: " + nombre);
                    }
                    if (correo != null) {
                        textViewCorreoPaciente.setText("Correo: " + correo);
                    }
                } else {
                    Toast.makeText(Consulta.this, "Datos del usuario no encontrados.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Consulta.this, "Error al cargar datos del usuario: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoCalendario() {
        final Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        fechaConsultaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year);
                        textViewFechaSeleccionada.setText(fechaConsultaSeleccionada);
                    }
                }, anio, mes, dia);
        datePickerDialog.show();
    }

    private void validarYGuardarConsulta() {
        String uidPaciente = textViewUidPaciente.getText().toString().replace("UID: ", "").trim();
        String nombrePaciente = textViewNombrePaciente.getText().toString().replace("Nombre: ", "").trim();
        String correoPaciente = textViewCorreoPaciente.getText().toString().replace("Correo: ", "").trim();
        String sintomas = editTextSintomas.getText().toString().trim();
        String diagnostico = editTextDiagnostico.getText().toString().trim();
        String tratamiento = editTextTratamiento.getText().toString().trim();
        String estadoConsulta = "Pendiente";

        if (TextUtils.isEmpty(uidPaciente)) {
            Toast.makeText(this, "No se pudo obtener el UID del paciente.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(sintomas)) {
            Toast.makeText(this, "Ingrese los síntomas.", Toast.LENGTH_SHORT).show();
            editTextSintomas.setError("Síntomas requeridos");
            editTextSintomas.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(fechaConsultaSeleccionada)) {
            Toast.makeText(this, "Seleccione la fecha de la consulta.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Guardando consulta...");
        progressDialog.show();

        String idConsulta = consultasReference.push().getKey();

        if (idConsulta == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Error al generar ID para la consulta.", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormatGuardado = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String fechaHoraGuardado = dateFormatGuardado.format(new Date());

        Map<String, Object> datosConsulta = new HashMap<>();
        datosConsulta.put("idConsulta", idConsulta);
        datosConsulta.put("uidPaciente", uidPaciente);
        datosConsulta.put("nombrePaciente", nombrePaciente);
        datosConsulta.put("correoPaciente", correoPaciente);
        datosConsulta.put("sintomas", sintomas);
        datosConsulta.put("diagnostico", diagnostico);
        datosConsulta.put("tratamiento", tratamiento);
        datosConsulta.put("fechaConsultaProgramada", fechaConsultaSeleccionada);
        datosConsulta.put("fechaHoraRegistroConsulta", fechaHoraGuardado);
        datosConsulta.put("estado", estadoConsulta);

        consultasReference.child(idConsulta).setValue(datosConsulta)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(Consulta.this, "Consulta guardada exitosamente.", Toast.LENGTH_SHORT).show();
                        textViewEstado.setText("Estado: " + estadoConsulta + " (Guardada)");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Consulta.this, "Error al guardar consulta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        textViewEstado.setText("Estado: Error al guardar");
                    }
                });
    }

    private void limpiarCampos() {
        editTextSintomas.setText("");
        editTextDiagnostico.setText("");
        editTextTratamiento.setText("");
        textViewFechaSeleccionada.setText(getString(R.string.Fecha));
        fechaConsultaSeleccionada = "";
        editTextSintomas.requestFocus();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}