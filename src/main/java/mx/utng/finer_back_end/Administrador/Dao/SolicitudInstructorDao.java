package mx.utng.finer_back_end.Administrador.Dao;

import mx.utng.finer_back_end.Documentos.SolicitudInstructorDocumento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SolicitudInstructorDao extends JpaRepository<SolicitudInstructorDocumento, Long> {

    @Query(value = "SELECT * FROM rechazar_instructor(:p_id_solicitud)", nativeQuery = true)
    List<Object[]> rechazarInstructor(@Param("p_id_solicitud") Integer p_id_solicitud);
}
