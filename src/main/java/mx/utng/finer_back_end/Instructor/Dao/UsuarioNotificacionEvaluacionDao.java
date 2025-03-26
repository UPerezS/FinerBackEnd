package mx.utng.finer_back_end.Instructor.Dao;

import mx.utng.finer_back_end.Instructor.Documentos.UsuarioNotificacionEvaluacionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioNotificacionEvaluacionDao extends JpaRepository<UsuarioNotificacionEvaluacionDTO, Integer> {

    /**
     * Obtener los correos electrónicos de los estudiantes inscritos en un curso.
     * Suponiendo que hay una relación ManyToMany entre Usuario y Curso.
     *
     * @param idCurso el ID del curso
     * @return lista de correos electrónicos de los estudiantes inscritos
     */
    @Query("SELECT u.correo FROM UsuarioNotificacionEvaluacionDTO u " +
           "JOIN u.cursos c " + // Aquí aseguramos que la relación con cursos está bien mapeada
           "WHERE c.idCurso = :idCurso AND u.idRol = 3")
    List<String> obtenerCorreosEstudiantes(@Param("idCurso") Integer idCurso);
}
