package mx.utng.finer_back_end.Instructor.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.SolicitudCursoDocumento;

@Repository
public interface SolicitudCursoEditarDao extends JpaRepository<SolicitudCursoDocumento, Integer> {
    // Remove the problematic method that was causing issues
}