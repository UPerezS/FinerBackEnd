package mx.utng.finer_back_end.Administrador.Dao;

import mx.utng.finer_back_end.Documentos.SolicitudCursoDocumento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudCursoDao extends JpaRepository<SolicitudCursoDocumento, Integer> {
    // Método para obtener las solicitudes de cursos pendientes de aprobación
    List<SolicitudCursoDocumento> findByEstatus(String estatus);
}

