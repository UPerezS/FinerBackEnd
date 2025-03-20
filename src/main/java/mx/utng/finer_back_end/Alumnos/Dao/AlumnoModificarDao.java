package mx.utng.finer_back_end.Alumnos.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.UsuarioDocumento;

@Repository
public interface AlumnoModificarDao extends JpaRepository<UsuarioDocumento, Integer> {
    @Query(value = "SELECT * FROM actualizar_perfil_alumno(:idusuario,:nombre,:apellidoPaterno,:apellidoMaterno,:nombreUsuario,:correo,:contrasenia)", nativeQuery = true)

    List<Object[]> actualizar_perfil_alumno(
            @Param("idusuario") Integer idUsuario,
            @Param("nombre") String nombre,
            @Param("apellidoPaterno") String apellidoPaterno, 
            @Param("apellidoMaterno") String apellidoMaterno,
            @Param("nombreUsuario") String nombreUsuario, 
            @Param("correo") String correo,
            @Param("contrasenia") String contrasenia);
}
