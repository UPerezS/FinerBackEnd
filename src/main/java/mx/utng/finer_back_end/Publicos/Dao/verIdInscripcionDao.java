package mx.utng.finer_back_end.Publicos.Dao;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import mx.utng.finer_back_end.Documentos.InscripcionDocumento;

public interface verIdInscripcionDao extends CrudRepository<InscripcionDocumento, Integer>{
    @Query(value = "SELECT obtener_idinscripcion(:idUsuario, :idCurso) AS id_inscripcion", nativeQuery = true)
    Integer obtenerIdInscripcion(@Param("idUsuario") Integer idUsuario, @Param("idCurso") Integer idCurso);
    
}

