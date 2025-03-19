package mx.utng.finer_back_end.Instructor.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.CursoDocumento;

@Repository
public interface CursoCategoriaDao extends JpaRepository<CursoDocumento, Integer>{
    @Query(value = "SELECT * FROM filtrar_cursos_categoria(:busqueda)", nativeQuery = true)
    List<Object[]> filtrarCursosCategoria(@Param("busqueda") String busqueda);
}
