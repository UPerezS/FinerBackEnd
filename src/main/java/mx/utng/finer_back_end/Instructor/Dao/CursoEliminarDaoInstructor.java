package mx.utng.finer_back_end.Instructor.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.utng.finer_back_end.Documentos.CursoDocumento;

public interface CursoEliminarDaoInstructor extends JpaRepository<CursoDocumento, Integer> {
    
    @Query(value = "SELECT eliminar_curso(:p_id_usuario, :p_id_curso)", nativeQuery = true)
    String eliminarCurso(@Param("p_id_usuario") Integer idUsuario, @Param("p_id_curso") Integer idCurso);
}
