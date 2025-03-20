package mx.utng.finer_back_end.Instructor.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.CursoDocumento;

@Repository
public interface VerAlumnosDaoInstructor extends JpaRepository<CursoDocumento, Integer> {
    /**
     * Se invoca la función verAlumnos pasando el parámetro JSON.
     * Se castea el parámetro a JSON en la consulta nativa.
     */
    @Query(value = "SELECT * FROM verAlumnos(CAST(?1 AS json))", nativeQuery = true)
    List<Object[]> verAlumnos(String pRequest);





}
