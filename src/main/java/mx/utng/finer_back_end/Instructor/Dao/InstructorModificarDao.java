package mx.utng.finer_back_end.Instructor.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.UsuarioDocumento;

@Repository
public interface InstructorModificarDao extends JpaRepository<UsuarioDocumento, Integer>{
@Query(value = "SELECT * FROM actualizar_perfil_instructor (:idusuario,:nombre,:apellidopaterno,:apellidomaterno,:correo,:nombreusuario,:telefono,:direccion)", nativeQuery = true)

List<Object[]>actualizar_perfil_instructor(
    @Param("idusuario") Integer idUsuario,
    @Param("nombre") String nombre,
    @Param("apellidopaterno") String apellidoPaterno,
    @Param("apellidomaterno") String apellidoMaterno,
    @Param("correo") String correo,
    @Param("nombreusuario")String nombreUsuario,
    @Param("direccion") String direccion);


} 

    

    

