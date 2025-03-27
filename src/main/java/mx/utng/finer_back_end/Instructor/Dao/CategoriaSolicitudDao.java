package mx.utng.finer_back_end.Instructor.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.utng.finer_back_end.Documentos.CategoriaDocumento;

@Repository // Asegúrate de que esta anotación esté presente
public interface CategoriaSolicitudDao extends JpaRepository<CategoriaDocumento, Integer>{

    /**
     * Registrar la solicitud de nueva categoría en la base de datos.
     *
     * @param nombreCategoria el nombre de la categoría
     * @param descripcion el motivo de la solicitud
     * @param idInstructor el ID del instructor solicitante
     * @return Mensaje de éxito o error.
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO solicitudcategoria (nombre_categoria, descripcion, id_usuario_instructor, estatus) " +
                   "VALUES (:nombreCategoria, :descripcion, :idInstructor, 'en revision')", nativeQuery = true)
    void registrarSolicitudCategoria(@Param("nombreCategoria") String nombreCategoria, 
                                       @Param("descripcion") String descripcion, 
                                       @Param("idInstructor") Integer idInstructor);
}
