package mx.utng.finer_back_end.Alumnos.Documentos;


import java.time.LocalDate;

public class CursoFinalizadoDTO {

    // Datos de inscripción
    private Integer idInscripcion;
    private String matricula;
    private LocalDate fechaInscripcion;
    private String estatusInscripcion;
    private Integer idUsuarioAlumno;
    
    // Datos del alumno
    private String nombreCompletoAlumno;
    
    // Datos del curso
    private Integer idCurso;
    private Integer idUsuarioInstructor;
    private Integer idCategoria;
    private String tituloCurso;
    private String descripcionCurso;
    
    // Datos de la categoría
    private String nombreCategoria;
    
    // Datos del instructor
    private String nombreCompletoInstructor;

    public CursoFinalizadoDTO() {
    }

    public CursoFinalizadoDTO(Integer idInscripcion, String matricula, LocalDate fechaInscripcion, String estatusInscripcion,
            Integer idUsuarioAlumno, String nombreCompletoAlumno, Integer idCurso, Integer idUsuarioInstructor,
            Integer idCategoria, String tituloCurso, String descripcionCurso, String nombreCategoria, String nombreCompletoInstructor) {
        this.idInscripcion = idInscripcion;
        this.matricula = matricula;
        this.fechaInscripcion = fechaInscripcion;
        this.estatusInscripcion = estatusInscripcion;
        this.idUsuarioAlumno = idUsuarioAlumno;
        this.nombreCompletoAlumno = nombreCompletoAlumno;
        this.idCurso = idCurso;
        this.idUsuarioInstructor = idUsuarioInstructor;
        this.idCategoria = idCategoria;
        this.tituloCurso = tituloCurso;
        this.descripcionCurso = descripcionCurso;
        this.nombreCategoria = nombreCategoria;
        this.nombreCompletoInstructor = nombreCompletoInstructor;
    }

    // Getters y Setters

    public Integer getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(Integer idInscripcion) {
        this.idInscripcion = idInscripcion;
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

    public String getEstatusInscripcion() {
        return estatusInscripcion;
    }

    public void setEstatusInscripcion(String estatusInscripcion) {
        this.estatusInscripcion = estatusInscripcion;
    }

    public Integer getIdUsuarioAlumno() {
        return idUsuarioAlumno;
    }

    public void setIdUsuarioAlumno(Integer idUsuarioAlumno) {
        this.idUsuarioAlumno = idUsuarioAlumno;
    }

    public String getNombreCompletoAlumno() {
        return nombreCompletoAlumno;
    }

    public void setNombreCompletoAlumno(String nombreCompletoAlumno) {
        this.nombreCompletoAlumno = nombreCompletoAlumno;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public Integer getIdUsuarioInstructor() {
        return idUsuarioInstructor;
    }

    public void setIdUsuarioInstructor(Integer idUsuarioInstructor) {
        this.idUsuarioInstructor = idUsuarioInstructor;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getTituloCurso() {
        return tituloCurso;
    }

    public void setTituloCurso(String tituloCurso) {
        this.tituloCurso = tituloCurso;
    }

    public String getDescripcionCurso() {
        return descripcionCurso;
    }

    public void setDescripcionCurso(String descripcionCurso) {
        this.descripcionCurso = descripcionCurso;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreCompletoInstructor() {
        return nombreCompletoInstructor;
    }

    public void setNombreCompletoInstructor(String nombreCompletoInstructor) {
        this.nombreCompletoInstructor = nombreCompletoInstructor;
    }
}
