package mx.utng.finer_back_end.Instructor.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.utng.finer_back_end.Documentos.PreguntaDocumento;

public interface PreguntaDao extends JpaRepository<PreguntaDocumento, Integer> {

    @Query(value = "SELECT agregar_pregunta(:p_id_evaluacion, :p_texto_pregunta)", nativeQuery = true)
    Integer agregarPregunta(@Param("p_id_evaluacion") Integer idEvaluacion, 
                            @Param("p_texto_pregunta") String textoPregunta);
}
