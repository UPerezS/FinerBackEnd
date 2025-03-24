package mx.utng.finer_back_end.Instructor.Documentos;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import mx.utng.finer_back_end.Documentos.CursoDocumento;

@Entity
@Table(name = "usuario")
public class UsuarioNotificacionEvaluacionDTO {

    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "correo")
    private String correo;

@Column(name = "id_rol")
    private Integer idRol;

    // Relación ManyToMany con Curso a través de Inscripcion
    @ManyToMany
    @JoinTable(
        name = "inscripcion",
        joinColumns = @JoinColumn(name = "id_usuario_alumno"),
        inverseJoinColumns = @JoinColumn(name = "id_curso")
    )
    private List<CursoDocumento> cursos;


    // Getters and Setters

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
