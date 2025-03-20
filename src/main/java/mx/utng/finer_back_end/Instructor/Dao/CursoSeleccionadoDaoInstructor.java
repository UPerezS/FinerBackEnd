package mx.utng.finer_back_end.Instructor.Dao;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.CursoDocumento;

@Repository
public interface CursoSeleccionadoDaoInstructor extends JpaRepository<CursoDocumento, Integer> {

    @Query(value = "SELECT * FROM seleccionarCurso(:p_id_curso)", nativeQuery = true)
    List<Object[]> seleccionarCurso(@Param("p_id_curso") Integer idCurso);
}