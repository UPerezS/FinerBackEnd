package mx.utng.finer_back_end.Documentos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "respuestaalumno") 
public class RespuestaAlumnoDocumento {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta_alumno")
    private Long idRespuestaAlumno;

    @NotNull
    @Column(name = "id_opcion")
    private Integer idOpcion;


    @NotNull
    @Column(name = "id_pregunta")
    private Integer idPregunta;

    @NotNull
    @Column(name = "id_inscripcion")
    private Integer idInscripcion;
   

    public Long getIdRespuestaAlumno() {
        return idRespuestaAlumno;
    }

    public void setIdRespuestaAlumno(Long idRespuestaAlumno) {
        this.idRespuestaAlumno = idRespuestaAlumno;
    }

   
    public Integer getIdOpcion(){
        return idOpcion;

    }
    public void setIdOpcion( Integer idOpcion){
        this.idOpcion = idOpcion;
    }
    public Integer getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Integer idPregunta) {
        this.idPregunta = idPregunta;
    }
    public Integer getidInscripcion(){
        return idInscripcion;
    }
    public void setPregunta(Integer idInscripcion){
        this.idInscripcion = idInscripcion;
    }

  
   
    }

  