package mx.utng.finer_back_end.Instructor.Documentos;

public class CursoEditarDTO {

    private Integer idCurso;      // ID del curso
    private Integer idInstructor; // ID del instructor
    private String descripcion;   // Nueva descripción del curso
    private Integer idCategoria; // Nueva categoría del curso

    // Getters y Setters
    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public Integer getIdInstructor() {
        return idInstructor;
    }

    public void setIdInstructor(Integer idInstructor) {
        this.idInstructor = idInstructor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }
}
