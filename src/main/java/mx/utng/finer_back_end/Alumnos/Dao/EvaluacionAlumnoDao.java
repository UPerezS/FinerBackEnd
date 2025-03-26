package mx.utng.finer_back_end.Alumnos.Dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.EvaluacionDocumento;

import java.util.List;
import java.util.Map;

@Repository
public interface EvaluacionAlumnoDao extends CrudRepository<EvaluacionDocumento, Integer> {

    @Query(value = "SELECT * FROM ver_evaluacion(:idEvaluacion)", nativeQuery = true)
    List<Map<String, Object>> obtenerEvaluacion(@Param("idEvaluacion") Integer idEvaluacion);

    // @Query(value = "SELECT realizar_evaluacion()", nativeQuery = true)
    // Number guardarRespuestas();

}
