package mx.utng.finer_back_end.Instructor.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.utng.finer_back_end.Documentos.CursoDocumento;

public interface CursoVerDaoInstructor extends JpaRepository<CursoDocumento, Integer> {
    @Query(value = "SELECT * FROM verCursos(:p_id_instructor)", nativeQuery = true)
    List<Object[]> verCursos(@Param("p_id_instructor") Integer idInstructor);
}