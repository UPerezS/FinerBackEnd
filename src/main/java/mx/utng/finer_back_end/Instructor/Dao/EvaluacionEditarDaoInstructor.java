package mx.utng.finer_back_end.Instructor.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.utng.finer_back_end.Documentos.EvaluacionDocumento;

public interface EvaluacionEditarDaoInstructor extends JpaRepository<EvaluacionDocumento, Integer> {

    @Query(value = "SELECT modificar_evaluacion(:p_id_evaluacion, :p_titulo_evaluacion)", nativeQuery = true)
    boolean editarEvaluacion(@Param("p_id_evaluacion") Integer idEvaluacion,
                             @Param("p_titulo_evaluacion") String tituloEvaluacion);

    @Query(value = "SELECT modificar_pregunta(:p_id_pregunta, :p_texto_pregunta)", nativeQuery = true)
    boolean editarPregunta(@Param("p_id_pregunta") Integer idPregunta,
                           @Param("p_texto_pregunta") String textoPregunta);

    @Query(value = "SELECT modificar_opcion(:p_id_opcion, :p_texto_opcion, :p_verificar)", nativeQuery = true)
    boolean editarOpcion(@Param("p_id_opcion") Integer idOpcion,
                     @Param("p_texto_opcion") String textoOpcion,
                     @Param("p_verificar") Boolean verificar);

}
