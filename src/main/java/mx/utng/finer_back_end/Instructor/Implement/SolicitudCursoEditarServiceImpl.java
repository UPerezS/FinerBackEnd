package mx.utng.finer_back_end.Instructor.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.Instructor.DTO.SolicitudCursoEditarDTO;
import mx.utng.finer_back_end.Instructor.Dao.SolicitudCursoEditarDao;
import mx.utng.finer_back_end.Instructor.Services.SolicitudCursoEditarService;

@Service
public class SolicitudCursoEditarServiceImpl implements SolicitudCursoEditarService {
    
    // Keep the DAO reference to avoid dependency issues
    @Autowired
    private SolicitudCursoEditarDao solicitudCursoEditarDao;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Boolean editarSolicitudCurso(SolicitudCursoEditarDTO solicitudDTO) {
        try {
            // Try direct SQL update approach
            // Get current status
            String currentStatus = jdbcTemplate.queryForObject(
                "SELECT estatus FROM solicitudcurso WHERE id_solicitud_curso = ?",
                String.class,
                solicitudDTO.getIdSolicitudCurso()
            );
            
            // Determine new status
            String newStatus = "rechazada".equals(currentStatus) ? "pendiente" : currentStatus;
            
            // Perform update
            int updated = jdbcTemplate.update(
                "UPDATE solicitudcurso SET " +
                "id_usuario_instructor = ?, " +
                "titulo_curso_solicitado = ?, " +
                "descripcion = ?, " +
                "estatus = ?, " +
                "id_categoria = ?, " +
                "id_curso = NULL, " +
                "fecha_solicitud = CURRENT_TIMESTAMP " +
                "WHERE id_solicitud_curso = ?",
                solicitudDTO.getIdUsuarioInstructor(),
                solicitudDTO.getTituloCursoSolicitado(),
                solicitudDTO.getDescripcion(),
                newStatus,
                solicitudDTO.getIdCategoria(),
                solicitudDTO.getIdSolicitudCurso()
            );
            
            return updated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}