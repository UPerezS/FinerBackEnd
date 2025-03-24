package mx.utng.finer_back_end.Instructor.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.CursoDocumento;

@Repository
public interface CursoEditarDao extends JpaRepository<CursoDocumento, Integer> {

    /**
     * Consulta nativa para modificar los detalles de un curso.
     *
     * @param idCurso el ID del curso
     * @param idInstructor el ID del instructor
     * @param descripcion la nueva descripción del curso
     * @param idCategoria el nuevo ID de la categoría del curso
     * @return Mensaje con el resultado de la operación.
     */
    @Query(value = "SELECT modificar_curso(:p_id_curso, :p_id_instructor, :p_descripcion, :p_id_categoria)", nativeQuery = true)
    String modificarCurso(@Param("p_id_curso") Integer idCurso, 
                          @Param("p_id_instructor") Integer idInstructor, 
                          @Param("p_descripcion") String descripcion,
                          @Param("p_id_categoria") Integer idCategoria);
}
