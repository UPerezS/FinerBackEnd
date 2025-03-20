package mx.utng.finer_back_end.Instructor.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.UsuarioDocumento;

@Repository
public interface AlumnoMatriculaDao extends JpaRepository<UsuarioDocumento, Integer> {
     @Query(value = "SELECT * FROM filtrar_alumno_matricula(:matricula, :orden)", nativeQuery = true)
    List<Object[]> filtrarAlumnosMatricula(@Param("matricula") String matricula, @Param("orden") String orden);
}


