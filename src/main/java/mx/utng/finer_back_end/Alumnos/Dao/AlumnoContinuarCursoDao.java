package mx.utng.finer_back_end.Alumnos.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.utng.finer_back_end.Documentos.CursoDocumento;

public interface AlumnoContinuarCursoDao extends JpaRepository <CursoDocumento, Integer> {
    @Query(value = "SELECT * FROM continuar_curso(:idCurso,:idUsuarioAlumno)", nativeQuery =  true)

    List<Object[]> continuar_curso(
        @Param("idCurso") Integer idCurso,
        @Param("idUsuarioAlumno") Integer idUsuarioAlumno
    );

    
}
