package mx.utng.finer_back_end.Documentos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pregunta") //Nombre De la tabla en la BD
public class PreguntaDocumento {
    //Campos de la tabla
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Long idPregunta;

    @NotNull
    @Column(name = "id_evaluacion")
    private Integer idEvaluacion;

  
    @NotBlank
    @Column(name = "pregunta")
    private String pregunta;


   

    public Long getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Long idPregunta) {
        this.idPregunta = idPregunta;
    }
    public Integer getIdEvaluacion(){
        return idEvaluacion;

    }
    public void setIdEvaluacion( Integer idEvaluacion){
        this.idEvaluacion = idEvaluacion;
    }

    public String getPregunta(){
        return pregunta;
    }
    public void setPregunta(String pregunta){
        this.pregunta = pregunta;
    }

  
   
    }

  