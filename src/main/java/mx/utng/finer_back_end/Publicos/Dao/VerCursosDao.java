package mx.utng.finer_back_end.Publicos.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mx.utng.finer_back_end.Documentos.CursoDocumento;

public interface VerCursosDao extends JpaRepository<CursoDocumento, Integer>{
    @Query(value = "SELECT c.titulo_curso, c.descripcion, u.nombre, u.apellido_paterno, u.apellido_materno, cat.nombre_categoria " +
                   "FROM Curso c " +
                   "JOIN Usuario u ON c.id_usuario_instructor = u.id_usuario " +
                   "JOIN Categoria cat ON c.id_categoria = cat.id_categoria", 
                   nativeQuery = true)
    List<Object[]> verCursos();
}
