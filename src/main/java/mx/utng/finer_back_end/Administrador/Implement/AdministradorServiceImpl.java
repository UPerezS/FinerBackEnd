package mx.utng.finer_back_end.Administrador.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.utng.finer_back_end.Administrador.Services.AdministradorService;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String eliminarAlumnoCurso(String matricula, Integer idCurso) {
        try {
            // Llamar a la función de PostgreSQL para eliminar al alumno del curso
            String resultado = jdbcTemplate.queryForObject(
                "SELECT eliminar_alumno_curso(?, ?)", 
                String.class, 
                matricula, 
                idCurso
            );
            
            return resultado;
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir
            return "Error al eliminar al alumno del curso: " + e.getMessage();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String rechazarCurso(Long idSolicitudCurso, String correoInstructor, String motivoRechazo, String tituloCurso) {
        try {
            // Primero verificamos si existe el registro
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM solicitudcurso WHERE id_solicitud_curso = ?", 
                Integer.class, 
                idSolicitudCurso
            );
            
            // Log para depuración
            System.out.println("Buscando solicitud con ID: " + idSolicitudCurso);
            System.out.println("Registros encontrados: " + count);
            
            if (count != null && count > 0) {
                // Verificar el estado actual
                String estadoActual = jdbcTemplate.queryForObject(
                    "SELECT estatus FROM solicitudcurso WHERE id_solicitud_curso = ?",
                    String.class,
                    idSolicitudCurso
                );
                
                System.out.println("Estado actual de la solicitud: " + estadoActual);
                
                if ("rechazado".equals(estadoActual)) {
                    return "La solicitud ya ha sido rechazada anteriormente";
                }
                
                if ("aprobado".equals(estadoActual)) {
                    return "No se puede rechazar una solicitud que ya ha sido aprobada";
                }
                
                // Enviar el correo antes de actualizar el estado, ya que el trigger eliminará la solicitud
                enviarCorreoRechazo(correoInstructor, motivoRechazo, tituloCurso);
                
                // El registro existe y está en estado válido para rechazar, procedemos a actualizarlo
                int filasAfectadas = jdbcTemplate.update(
                    "UPDATE solicitudcurso SET estatus = 'rechazado' WHERE id_solicitud_curso = ?", 
                    idSolicitudCurso
                );
                
                if (filasAfectadas > 0) {
                    return "Rechazado";
                } else {
                    return "Error al actualizar el registro";
                }
            } else {
                return "No se encontró la solicitud de curso con el ID proporcionado";
            }
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir
            e.printStackTrace(); // Para ver el error completo en los logs
            return "Error al rechazar el curso: " + e.getMessage();
        }
    }
    
    /**
     * Envía un correo electrónico al instructor notificando el rechazo de su solicitud de curso.
     * 
     * @param correoInstructor Correo del instructor al que se enviará la notificación
     * @param motivoRechazo Motivo por el cual se rechazó el curso
     * @param tituloCurso Título del curso rechazado
     */
    private void enviarCorreoRechazo(String correoInstructor, String motivoRechazo, String tituloCurso) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom("finner.oficial.2025@gmail.com");
            mensaje.setTo(correoInstructor);
            mensaje.setSubject("Solicitud de curso rechazada - Finner");
            
            String cuerpoMensaje = "Estimado instructor,\n\n" +
                    "Le informamos que su solicitud para el curso \"" + tituloCurso + "\" ha sido rechazada.\n\n" +
                    "Motivo del rechazo: " + motivoRechazo + "\n\n" +
                    "Si tiene alguna duda o desea más información, por favor contacte al equipo administrativo.\n\n" +
                    "Atentamente,\n" +
                    "El equipo de Finner";
            
            mensaje.setText(cuerpoMensaje);
            
            javaMailSender.send(mensaje);
        } catch (Exception e) {
            // Solo registramos la excepción pero no interrumpimos el flujo
            System.err.println("Error al enviar correo de rechazo: " + e.getMessage());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String crearCategoria(Integer idUsuarioInstructor, Integer idUsuarioAdmin, String nombreCategoria, String descripcion) {
        try {
            // Llamar a la función de PostgreSQL para crear la solicitud de categoría
            String resultado = jdbcTemplate.queryForObject(
                "SELECT solicitar_creacion_categoria(?, ?, ?, ?)", 
                String.class, 
                idUsuarioInstructor, 
                idUsuarioAdmin, 
                nombreCategoria, 
                descripcion
            );
            
            return resultado;
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir
            e.printStackTrace(); // Para ver el error completo en los logs
            return "Error al crear la solicitud de categoría: " + e.getMessage();
        }
    }
}  // Make sure this closing brace is correct
