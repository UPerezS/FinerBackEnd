package mx.utng.finer_back_end.Alumnos.Documentos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContinuarCursoDTO {
    private Integer idTema;
    private String nombreTema;
    private String contenido;
    private Boolean f;

    // Constructor con todos los parámetros
    public ContinuarCursoDTO(Integer idTema, String nombreTema, String contenido, Boolean f) {
        this.idTema = idTema;
        this.nombreTema = nombreTema;
        this.contenido = contenido;
        this.f = f;
    }

    // Getters
    public Integer getIdTema() {
        return idTema;
    }

    public String getNombreTema() {
        return nombreTema;
    }

    public String getContenido() {
        return contenido;
    }

    public Boolean getF() {
        return f;
    }

    // Setters
    public void setIdTema(Integer idTema) {
        this.idTema = idTema;
    }

    public void setNombreTema(String nombreTema) {
        this.nombreTema = nombreTema;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setF(Boolean f) {
        this.f = f;
    }
}