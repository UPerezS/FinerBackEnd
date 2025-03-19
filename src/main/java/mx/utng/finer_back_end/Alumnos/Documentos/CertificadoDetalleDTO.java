package mx.utng.finer_back_end.Alumnos.Documentos;

import java.time.LocalDate;

public class CertificadoDetalleDTO {

    private Integer idInscripcion;
    private String nombreCompletoAlumno;
    private String tituloCurso;
    private String nombreCategoria;
    private String nombreInstructor;
    private String matricula;
    private LocalDate fechaInscripcion;
    private LocalDate fechaGeneracion;


    public CertificadoDetalleDTO() {
    }

    public CertificadoDetalleDTO(Integer idInscripcion, String nombreCompletoAlumno, String tituloCurso,
            String nombreCategoria, String nombreInstructor, String matricula, LocalDate fechaInscripcion,
            LocalDate fechaGeneracion) {
        this.idInscripcion = idInscripcion;
        this.nombreCompletoAlumno = nombreCompletoAlumno;
        this.tituloCurso = tituloCurso;
        this.nombreCategoria = nombreCategoria;
        this.nombreInstructor = nombreInstructor;
        this.matricula = matricula;
        this.fechaInscripcion = fechaInscripcion;
        this.fechaGeneracion = fechaGeneracion;
    }


    public Integer getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(Integer idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public String getNombreCompletoAlumno() {
        return nombreCompletoAlumno;
    }

    public void setNombreCompletoAlumno(String nombreCompletoAlumno) {
        this.nombreCompletoAlumno = nombreCompletoAlumno;
    }

    public String getTituloCurso() {
        return tituloCurso;
    }

    public void setTituloCurso(String tituloCurso) {
        this.tituloCurso = tituloCurso;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreInstructor() {
        return nombreInstructor;
    }

    public void setNombreInstructor(String nombreInstructor) {
        this.nombreInstructor = nombreInstructor;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
}
