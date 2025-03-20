package mx.utng.finer_back_end.AlumnosInstructor.Dao;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.utng.finer_back_end.Documentos.CursoDocumento;

public interface CursoProgresoDao extends JpaRepository<CursoDocumento,Long> {

    @Query(value = "SELECT obtener_progreso_curso(:idEstudiante, :idCurso)", nativeQuery = true)
    Float verProgresoAlumno(@Param("idEstudiante") Integer idEstudiante, @Param("idCurso") Integer idCurso);

}