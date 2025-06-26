
package com.example.hospital;

public class ConsultaModel {
    private String idConsulta;
    private String uidPaciente;
    private String nombrePaciente;
    private String correoPaciente;
    private String sintomas;
    private String diagnostico;
    private String tratamiento;
    private String fechaConsultaProgramada;
    private String fechaHoraRegistroConsulta;
    private String estado;
    public ConsultaModel() {
    }

    public ConsultaModel(String idConsulta, String uidPaciente, String nombrePaciente, String correoPaciente,
                         String sintomas, String diagnostico, String tratamiento,
                         String fechaConsultaProgramada, String fechaHoraRegistroConsulta, String estado) {
        this.idConsulta = idConsulta;
        this.uidPaciente = uidPaciente;
        this.nombrePaciente = nombrePaciente;
        this.correoPaciente = correoPaciente;
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.fechaConsultaProgramada = fechaConsultaProgramada;
        this.fechaHoraRegistroConsulta = fechaHoraRegistroConsulta;
        this.estado = estado;
    }

    public String getIdConsulta() { return idConsulta; }
    public void setIdConsulta(String idConsulta) { this.idConsulta = idConsulta; }

    public String getUidPaciente() { return uidPaciente; }
    public void setUidPaciente(String uidPaciente) { this.uidPaciente = uidPaciente; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getCorreoPaciente() { return correoPaciente; }
    public void setCorreoPaciente(String correoPaciente) { this.correoPaciente = correoPaciente; }

    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public String getFechaConsultaProgramada() { return fechaConsultaProgramada; }
    public void setFechaConsultaProgramada(String fechaConsultaProgramada) { this.fechaConsultaProgramada = fechaConsultaProgramada; }

    public String getFechaHoraRegistroConsulta() { return fechaHoraRegistroConsulta; }
    public void setFechaHoraRegistroConsulta(String fechaHoraRegistroConsulta) { this.fechaHoraRegistroConsulta = fechaHoraRegistroConsulta; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}