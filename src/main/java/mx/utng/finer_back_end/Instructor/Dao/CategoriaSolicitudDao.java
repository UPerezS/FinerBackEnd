package mx.utng.finer_back_end.Instructor.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.utng.finer_back_end.Documentos.CategoriaDocumento;

@Repository // Asegúrate de que esta anotación esté presente
public interface CategoriaSolicitudDao extends JpaRepository<CategoriaDocumento, Integer>{

    /**
     * Registrar la solicitud de nueva categoría en la base de datos.
     *
     * @param nombreCategoria el nombre de la categoría
     * @param motivoSolicitud el motivo de la solicitud
     * @param idInstructor el ID del instructor solicitante
     * @return Mensaje de éxito o error.
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO solicitudcategoria (nombre_categoria, motivo_solicitud, id_usuario_instructor, id_usuario_admin, estatus) " +
                   "VALUES (:nombreCategoria, :motivoSolicitud, :idInstructor, :idUsuarioAdmin, 'en revision')", nativeQuery = true)
    void registrarSolicitudCategoria(@Param("nombreCategoria") String nombreCategoria, 
                                       @Param("motivoSolicitud") String motivoSolicitud, 
                                       @Param("idInstructor") Integer idInstructor,
                                       @Param("idUsuarioAdmin") Integer idUsuarioAdmin);
}
