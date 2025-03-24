package mx.utng.finer_back_end.Instructor.Dao;

import mx.utng.finer_back_end.Instructor.Documentos.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EvaluacionDao extends JpaRepository<Evaluacion, Integer> {

    /**
     * Consulta nativa para insertar una evaluación y retornar el ID generado.
     * Este método asume que la base de datos soporta la cláusula RETURNING (ej. PostgreSQL).
     * 
     * @param idCurso el ID del curso
     * @param tituloEvaluacion el título de la evaluación
     * @return El ID de la evaluación creada.
     */
    @Query(value = "INSERT INTO Evaluacion (id_curso, titulo_evaluacion, fecha_creacion) " +
                   "VALUES (:idCurso, :tituloEvaluacion, CURRENT_DATE) RETURNING id_evaluacion", 
           nativeQuery = true)
    Integer generarEvaluacion(@Param("idCurso") Integer idCurso, 
                              @Param("tituloEvaluacion") String tituloEvaluacion);
}
