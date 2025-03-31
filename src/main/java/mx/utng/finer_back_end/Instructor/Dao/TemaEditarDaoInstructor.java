package mx.utng.finer_back_end.Instructor.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.utng.finer_back_end.Documentos.TemaDocumento;

public interface TemaEditarDaoInstructor extends JpaRepository<TemaDocumento, Integer> {

    @Query(value = "SELECT modificar_tema(:p_id_usuario, :p_id_tema, :p_nombre_tema, :p_contenido)", nativeQuery = true)
    String editarTema(@Param("p_id_usuario") Integer idUsuario,
                      @Param("p_id_tema") Integer idTema,
                      @Param("p_nombre_tema") String nombreTema,
                      @Param("p_contenido") String contenido
                     );
}
