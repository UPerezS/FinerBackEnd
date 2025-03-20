package mx.utng.finer_back_end.Administrador.Dao;
import mx.utng.finer_back_end.Documentos.SolicitudCategoriaDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SolicitudCategoriaDao extends JpaRepository<SolicitudCategoriaDocumento, Integer> {
     // Método para obtener todas las solicitudes de un instructor específico
    List<SolicitudCategoriaDocumento> findByIdUsuarioInstructor(Integer idUsuarioInstructor);
     // Método para obtener todas las solicitudes rechazadas con más de 30 días
    List<SolicitudCategoriaDocumento> findByEstatusAndFechaSolicitudBefore(String estatus, LocalDate fecha);
}

