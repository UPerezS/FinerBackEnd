package mx.utng.finer_back_end.Instructor.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.utng.finer_back_end.Documentos.CursoDocumento;

public interface CursoNombreDao extends JpaRepository<CursoDocumento, Integer>{
   @Query(value = "SELECT * FROM filtrar_cursos_nombre(:busqueda)", nativeQuery = true)
List<Object[]> filtrarCursosPorNombre(@Param("busqueda") String busqueda);

}
