package mx.utng.finer_back_end.Documentos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "opcion") //Nombre de la tabla en la BD
public class OpcionDocumento {
    //Campos de la tabla
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion")
    private Long idOpcion;

    @NotNull
    @Column(name = "id_pregunta")
    private Integer idPregunta;

    @NotBlank
    @Column(name = "opcion")
    private String opcion;

    @Column(name = "verificar", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean verificar;

    // Getters and Setters de cada uno de los atributos de la tabla

    public Long getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(Long idOpcion) {
        this.idOpcion = idOpcion;
    }

    public Integer getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Integer idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public Boolean getVerificar() {
        return verificar;
    }

    public void setVerificar(Boolean verificar) {
        this.verificar = verificar;
    }
}