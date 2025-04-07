package mx.utng.finer_back_end.Instructor.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.Instructor.DTO.SolicitudTemaEditarDTO;
import mx.utng.finer_back_end.Instructor.Services.SolicitudTemaEditarService;

@Service
public class SolicitudTemaEditarServiceImpl implements SolicitudTemaEditarService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Boolean editarSolicitudTema(SolicitudTemaEditarDTO solicitudTemaDTO) {
        try {
            // Call the database function directly using JDBC
            String sql = "SELECT public.editar_solicitud_tema(?, ?, ?, ?)";
            Boolean result = jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                solicitudTemaDTO.getIdSolicitudTema(),
                solicitudTemaDTO.getIdSolicitudCurso(),
                solicitudTemaDTO.getNombreTema(),
                solicitudTemaDTO.getContenido()
            );
            
            return result != null && result;
        } catch (Exception e) {
            e.printStackTrace();
            
            // Fallback: try direct SQL update/insert
            try {
                // Check if the topic exists
                Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM solicitudtema WHERE id_solicitud_tema = ?",
                    Integer.class,
                    solicitudTemaDTO.getIdSolicitudTema()
                );
                
                int updated;
                if (count != null && count > 0) {
                    // Update existing topic
                    updated = jdbcTemplate.update(
                        "UPDATE solicitudtema SET " +
                        "id_solicitud_curso = ?, " +
                        "nombre_tema = ?, " +
                        "contenido = ? " +
                        "WHERE id_solicitud_tema = ?",
                        solicitudTemaDTO.getIdSolicitudCurso(),
                        solicitudTemaDTO.getNombreTema(),
                        solicitudTemaDTO.getContenido(),
                        solicitudTemaDTO.getIdSolicitudTema()
                    );
                } else {
                    // Insert new topic
                    updated = jdbcTemplate.update(
                        "INSERT INTO solicitudtema (id_solicitud_tema, id_solicitud_curso, nombre_tema, contenido) " +
                        "VALUES (?, ?, ?, ?)",
                        solicitudTemaDTO.getIdSolicitudTema(),
                        solicitudTemaDTO.getIdSolicitudCurso(),
                        solicitudTemaDTO.getNombreTema(),
                        solicitudTemaDTO.getContenido()
                    );
                }
                
                return updated > 0;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }
}