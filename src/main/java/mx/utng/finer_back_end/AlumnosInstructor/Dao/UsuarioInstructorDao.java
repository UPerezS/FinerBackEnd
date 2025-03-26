package mx.utng.finer_back_end.AlumnosInstructor.Dao;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.UsuarioDocumento;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UsuarioInstructorDao extends JpaRepository<UsuarioDocumento, Integer> {

    // Método para obtener los datos del usuario por su ID
    @Query(value = "SELECT * FROM obtener_datos_usuario(:idUsuario)", nativeQuery = true)
    UsuarioDocumento obtenerDatosUsuario(@Param("idUsuario") Integer idUsuario);
    
}


