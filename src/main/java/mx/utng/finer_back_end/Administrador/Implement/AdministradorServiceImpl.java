package mx.utng.finer_back_end.Administrador.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;  // Add this import
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mx.utng.finer_back_end.Administrador.Services.AdministradorService;
import mx.utng.finer_back_end.Documentos.UsuarioDocumento;
    
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
    public String rechazarCurso(Long idSolicitudCurso, String motivoRechazo, String tituloCurso) {
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

                if ("rechazada".equals(estadoActual)) {
                    return "La solicitud ya ha sido rechazada anteriormente";
                }

                if ("aprobada".equals(estadoActual)) {
                    return "No se puede rechazar una solicitud que ya ha sido aprobada";
                }
                
                // Obtener el correo del instructor y el título del curso desde la base de datos
                Map<String, Object> solicitudInfo = jdbcTemplate.queryForMap(
                    "SELECT u.correo, sc.titulo_curso_solicitado " +
                    "FROM solicitudcurso sc " +
                    "JOIN usuario u ON sc.id_usuario_instructor = u.id_usuario " +
                    "WHERE sc.id_solicitud_curso = ?",
                    idSolicitudCurso
                );
                
                String correoInstructor = (String) solicitudInfo.get("correo");
                // Si el título no se proporciona, usamos el de la base de datos
                if (tituloCurso == null || tituloCurso.isEmpty()) {
                    tituloCurso = (String) solicitudInfo.get("titulo_curso_solicitado");
                }
                
                System.out.println("Correo del instructor: " + correoInstructor);
                System.out.println("Título del curso: " + tituloCurso);
                
                if (correoInstructor == null || correoInstructor.isEmpty()) {
                    return "No se pudo obtener el correo del instructor";
                }
                
                // Enviar el correo antes de actualizar el estado
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
            // Verificar si la categoría ya existe
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM categoria WHERE nombre_categoria = ?", 
                Integer.class, 
                nombreCategoria
            );
            
            if (count != null && count > 0) {
                return "Error: Ya existe una categoría con el nombre '" + nombreCategoria + "'";
            }
            
            // Verificar si existe una solicitud de categoría y su estado
            // Nota: Según la documentación, la solicitud debe estar en la tabla solicitudcategoria
            // y debe tener un estado 'aprobado' para poder crear la categoría
            try {
                String estadoSolicitud = jdbcTemplate.queryForObject(
                    "SELECT estatus FROM solicitudcategoria WHERE nombre_categoria = ?",
                    String.class,
                    nombreCategoria
                );
                
                // Log para depuración
                System.out.println("Estado de la solicitud para la categoría '" + nombreCategoria + "': " + estadoSolicitud);
                
                // Verificar si el estado es 'aprobado'
                if (estadoSolicitud == null || !"aprobado".equals(estadoSolicitud)) {
                    return "Error: La solicitud de categoría no está aprobada o no existe";
                }
            } catch (Exception e) {
                // Si ocurre un error al buscar la solicitud, asumimos que no existe
                System.err.println("Error al verificar el estado de la solicitud: " + e.getMessage());
                return "Error: No se encontró una solicitud de categoría aprobada";
            }
            
            // Si llegamos aquí, la solicitud existe y está aprobada, procedemos a crear la categoría
            int filasAfectadas = jdbcTemplate.update(
                "INSERT INTO categoria (nombre_categoria, descripcion) VALUES (?, ?)", 
                nombreCategoria, 
                descripcion
            );
            
            if (filasAfectadas > 0) {
                // Obtener el ID de la categoría recién creada
                Integer idCategoria = jdbcTemplate.queryForObject(
                    "SELECT id_categoria FROM categoria WHERE nombre_categoria = ?", 
                    Integer.class, 
                    nombreCategoria
                );
                
                // Nota: La tabla log_categoria no existe en el esquema actual de la base de datos
                // Por lo tanto, no intentamos registrar en ella y continuamos con el flujo normal
                // Si en el futuro se implementa esta tabla, se puede descomentar el código siguiente:
                /*
                try {
                    jdbcTemplate.update(
                        "INSERT INTO log_categoria (id_categoria, id_usuario_instructor, id_usuario_admin, fecha_creacion) VALUES (?, ?, ?, CURRENT_TIMESTAMP)", 
                        idCategoria, 
                        idUsuarioInstructor, 
                        idUsuarioAdmin
                    );
                } catch (Exception logError) {
                    System.err.println("Error al registrar en log_categoria: " + logError.getMessage());
                }
                */
                
                return "Categoría '" + nombreCategoria + "' creada exitosamente con ID: " + idCategoria;
            } else {
                return "Error: No se pudo crear la categoría";
            }
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir
            e.printStackTrace(); // Para ver el error completo en los logs
            return "Error al crear la categoría: " + e.getMessage();
        }
    }
    
 
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String modificarCategoriaDescripcion(Integer idCategoria, String nuevaDescripcion) {
        try {
            // Llamar a la función de PostgreSQL para modificar la descripción de la categoría
            String resultado = jdbcTemplate.queryForObject(
                "SELECT modificar_desc_categoria(?, ?)", 
                String.class, 
                idCategoria, 
                nuevaDescripcion
            );
            
            return resultado;
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir
            e.printStackTrace(); // Para ver el error completo en los logs
            return "Error al modificar la descripción de la categoría: " + e.getMessage();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Boolean eliminarCategoria(Integer idCategoria) {
        try {
            // First check if the category exists
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM categoria WHERE id_categoria = ?", 
                Integer.class, 
                idCategoria
            );
            
            if (count == null || count == 0) {
                System.err.println("La categoría con ID " + idCategoria + " no existe.");
                return false;
            }
            
            // Check if it's the default category
            if (idCategoria == 0) {
                System.err.println("No se puede eliminar la categoría predeterminada (ID 0).");
                return false;
            }
            
            // Manually update references before deletion
            int cursosActualizados = jdbcTemplate.update(
                "UPDATE curso SET id_categoria = 0 WHERE id_categoria = ?", 
                idCategoria
            );
            
            int solicitudesActualizadas = jdbcTemplate.update(
                "UPDATE solicitudcurso SET id_categoria = 0 WHERE id_categoria = ?", 
                idCategoria
            );
            
            System.out.println("Cursos reasignados: " + cursosActualizados);
            System.out.println("Solicitudes reasignadas: " + solicitudesActualizadas);
            
            // Now try to delete the category
            int rowsAffected = jdbcTemplate.update(
                "DELETE FROM categoria WHERE id_categoria = ?", 
                idCategoria
            );
            
            System.out.println("Filas afectadas al eliminar categoría: " + rowsAffected);
            
            return rowsAffected > 0;
        } catch (Exception e) {
            // Log the full error for debugging
            System.err.println("Error al eliminar categoría: " + idCategoria);
            e.printStackTrace();
            
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
  
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String aprobarCurso(Integer idSolicitudCurso) {
        try {
            // First check if the course request exists
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM solicitudcurso WHERE id_solicitud_curso = ?",
                Integer.class,
                idSolicitudCurso
            );
            
            if (count == null || count == 0) {
                return "La solicitud de curso no existe.";
            }
            
            // Check the current status
            String estadoActual = jdbcTemplate.queryForObject(
                "SELECT estatus FROM solicitudcurso WHERE id_solicitud_curso = ?",
                String.class,
                idSolicitudCurso
            );
            
            // Log for debugging
            System.out.println("Estado actual de la solicitud: " + estadoActual);

            if ("aprobada".equals(estadoActual)) {
                return "La solicitud ya ha sido aprobada anteriormente";
            }

            if ("rechazada".equals(estadoActual)) {
                return "No se puede aprobar una solicitud que ya ha sido rechazada";
            }
            
            // Get the course request details before updating status
            Map<String, Object> solicitudCurso = jdbcTemplate.queryForMap(
                "SELECT id_usuario_instructor, id_categoria, titulo_curso_solicitado, descripcion FROM solicitudcurso WHERE id_solicitud_curso = ?",
                idSolicitudCurso
            );
            
            // Update the status to 'aprobado' instead of 'aprobada'
            int filasAfectadas = jdbcTemplate.update(

                    "UPDATE solicitudcurso SET estatus = 'aprobada' WHERE id_solicitud_curso = ?",
                    idSolicitudCurso);

            if (filasAfectadas > 0) {
                // Create the course in the curso table
                int cursoCreado = jdbcTemplate.update(
                    "INSERT INTO curso (id_usuario_instructor, id_categoria, titulo_curso, descripcion) " +
                    "VALUES (?, ?, ?, ?)",
                    solicitudCurso.get("id_usuario_instructor"),
                    solicitudCurso.get("id_categoria"),
                    solicitudCurso.get("titulo_curso_solicitado"),
                    solicitudCurso.get("descripcion")
                );
                
                if (cursoCreado > 0) {
                    // Get the ID of the newly created course
                    Integer idCurso = jdbcTemplate.queryForObject(
                        "SELECT id_curso FROM curso WHERE titulo_curso = ? AND id_usuario_instructor = ? ORDER BY id_curso DESC LIMIT 1",
                        Integer.class,
                        solicitudCurso.get("titulo_curso_solicitado"),
                        solicitudCurso.get("id_usuario_instructor")
                    );
                    
                    // Update the solicitudcurso with the course ID
                    jdbcTemplate.update(
                        "UPDATE solicitudcurso SET id_curso = ? WHERE id_solicitud_curso = ?",
                        idCurso,
                        idSolicitudCurso
                    );
                    
                    return "El curso ha sido aprobado exitosamente y creado en el catálogo con ID: " + idCurso;
                } else {
                    // Rollback the transaction if the course creation fails
                    throw new RuntimeException("Error al crear el curso en el catálogo");
                }
            } else {
                return "Error al actualizar el registro";
            }
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir
            e.printStackTrace(); // Para ver el error completo en los logs
            return "Error al aprobar el curso: " + e.getMessage();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String bloquearUsuario(String nombreUsuario) {
        try {
            // Verificar si el usuario existe
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM usuario WHERE nombre_usuario = ?", 
                Integer.class, 
                nombreUsuario
            );
            
            if (count == null || count == 0) {
                return "No se encontró el usuario con el nombre de usuario proporcionado";
            }
            
            // Verificar el rol actual del usuario
            Integer idRolActual = jdbcTemplate.queryForObject(
                "SELECT id_rol FROM usuario WHERE nombre_usuario = ?",
                Integer.class,
                nombreUsuario
            );
            
            // Verificar si ya está bloqueado (asumiendo que el id_rol para 'bloqueado' es 4)
            if (idRolActual != null && idRolActual == 4) {
                return "El usuario ya se encuentra bloqueado";
            }
            
            // Actualizar el rol del usuario a 'bloqueado'
            int filasAfectadas = jdbcTemplate.update(
                "UPDATE usuario SET id_rol = 4 WHERE nombre_usuario = ?", 
                nombreUsuario
            );
            
            if (filasAfectadas > 0) {
                return "Usuario bloqueado exitosamente";
            } else {
                return "Error al bloquear el usuario";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al bloquear el usuario: " + e.getMessage();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getUsuario(String nombreUsuario) {
        try {
            // Verificar si el usuario existe
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM usuario WHERE nombre_usuario = ?", 
                Integer.class, 
                nombreUsuario
            );
            
            if (count == null || count == 0) {
                return Map.of("error", "No se encontró el usuario con el nombre de usuario proporcionado");
            }
            
            // Obtener los datos del usuario
            Map<String, Object> usuario = jdbcTemplate.queryForMap(
                "SELECT u.*, r.nombre_rol FROM usuario u JOIN rol r ON u.id_rol = r.id_rol WHERE u.nombre_usuario = ?",
                nombreUsuario
            );
            
            // Verificar si el usuario es instructor y tiene cédula profesional
            if (usuario.get("id_rol") != null && Integer.parseInt(usuario.get("id_rol").toString()) == 2) {
                // Verificar el estado de validación de la cédula
                String estadoValidacion = jdbcTemplate.queryForObject(
                    "SELECT estatus FROM validacioncedula WHERE id_usuario = ?",
                    String.class,
                    usuario.get("id_usuario")
                );
                
                usuario.put("estado_cedula", estadoValidacion != null ? estadoValidacion : "pendiente");
            }
            
            return usuario;
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "Error al obtener los datos del usuario: " + e.getMessage());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, Object>> buscarUsuarioNombre(String busqueda) {
        try {
            // Buscar usuarios por coincidencia en nombre, apellido paterno o apellido materno
            String sql = "SELECT u.*, r.nombre_rol FROM usuario u " +
                         "JOIN rol r ON u.id_rol = r.id_rol " +
                         "WHERE LOWER(u.nombre) LIKE LOWER(?) OR " +
                         "LOWER(u.apellido_paterno) LIKE LOWER(?) OR " +
                         "LOWER(u.apellido_materno) LIKE LOWER(?)";
            
            String termino = "%" + busqueda + "%";
            
            return jdbcTemplate.queryForList(sql, termino, termino, termino);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(Map.of("error", "Error al buscar usuarios: " + e.getMessage()));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<UsuarioDocumento> getAlumnos() {
        try {
            // Obtener todos los usuarios con rol de alumno (id_rol = 3)
            String sql = "SELECT * FROM usuario WHERE id_rol = 3";
            
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                UsuarioDocumento alumno = new UsuarioDocumento(
                    rs.getString("nombre"),
                    rs.getInt("id_rol"),
                    rs.getString("apellido_paterno"),
                    rs.getString("apellido_materno"),
                    rs.getString("correo"),
                    rs.getString("contrasenia"),
                    rs.getString("nombre_usuario"),
                    rs.getString("telefono"),
                    rs.getString("direccion"),
                    rs.getString("estatus"),
                    null // No necesitamos la cédula para alumnos
                );
                alumno.setId(rs.getInt("id_usuario"));
                return alumno;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<UsuarioDocumento> getInstructores() {
        try {
            // Obtener todos los usuarios con rol de instructor (id_rol = 2)
            String sql = "SELECT * FROM usuario WHERE id_rol = 2";
            
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                UsuarioDocumento instructor = new UsuarioDocumento(
                    rs.getString("nombre"),
                    rs.getInt("id_rol"),
                    rs.getString("apellido_paterno"),
                    rs.getString("apellido_materno"),
                    rs.getString("correo"),
                    rs.getString("contrasenia"),
                    rs.getString("nombre_usuario"),
                    rs.getString("telefono"),
                    rs.getString("direccion"),
                    rs.getString("estatus"),
                    rs.getBytes("cedula_pdf")
                );
                instructor.setId(rs.getInt("id_usuario"));
                return instructor;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    @Transactional
    public String aceptarInstructor(Integer idSolicitudInstructor) {
        try {
            // Verificar si la solicitud existe
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM solicitudinstructor WHERE id_solicitud_instructor = ?",
                Integer.class,
                idSolicitudInstructor);
                
            if (count == null || count == 0) {
                return "La solicitud de instructor no existe";
            }
            
            // Verificar el estatus actual
            String estatus = jdbcTemplate.queryForObject(
                "SELECT estatus_solicitud FROM solicitudinstructor WHERE id_solicitud_instructor = ?",
                String.class,
                idSolicitudInstructor);
                
            if (!"pendiente".equals(estatus)) {
                return "La solicitud ya ha sido procesada anteriormente";
            }
            
            // Obtener datos del instructor antes de actualizar el estatus
            Map<String, Object> instructor = jdbcTemplate.queryForMap(
                "SELECT * FROM solicitudinstructor WHERE id_solicitud_instructor = ?",
                idSolicitudInstructor);
                
            // Crear un nuevo usuario con rol de instructor
            jdbcTemplate.update(
                "INSERT INTO usuario (id_rol, nombre, apellido_paterno, apellido_materno, correo, contrasenia, nombre_usuario, telefono, estatus) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'activo')",
                2, // Rol de instructor (id_rol = 2)
                instructor.get("nombre"),
                instructor.get("apellido_paterno"),
                instructor.get("apellido_materno"),
                instructor.get("correo"),
                instructor.get("contrasenia"),
                instructor.get("nombre_usuario"),
                instructor.get("telefono")
            );
            
            // Llamar a la función de PostgreSQL para actualizar el estado de la solicitud
            try {
                jdbcTemplate.update("SELECT aceptar_instructor(?)", idSolicitudInstructor);
                System.out.println("Estado de solicitud actualizado a 'aprobada' mediante función de base de datos");
            } catch (Exception e1) {
                System.err.println("Error al llamar a la función aceptar_instructor: " + e1.getMessage());
                
                // Si falla la función, intentamos actualizar manualmente
                try {
                    jdbcTemplate.update(
                        "UPDATE solicitudinstructor SET estatus_solicitud = 'aprobada' WHERE id_solicitud_instructor = ?",
                        idSolicitudInstructor
                    );
                    System.out.println("Estado actualizado manualmente a 'aprobada'");
                } catch (Exception e2) {
                    System.err.println("Error al actualizar manualmente: " + e2.getMessage());
                    // No interrumpimos el flujo ya que el usuario ha sido creado
                }
            }
            
            // Enviar correo de aceptación
            enviarCorreoAceptacionInstructor(instructor);
            
            return "Instructor aceptado exitosamente";
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir la excepción completa para depuración
            return "Error al aceptar al instructor: " + e.getMessage();
        }
    }
    
    /**
     * Envía un correo electrónico al instructor notificando la aprobación de su solicitud.
     * 
     * @param solicitudInfo Información de la solicitud del instructor
     */
    private void enviarCorreoAceptacionInstructor(Map<String, Object> solicitudInfo) {
        try {
            String correoInstructor = (String) solicitudInfo.get("correo");
            String nombreInstructor = (String) solicitudInfo.get("nombre") + " " + 
                                     (String) solicitudInfo.get("apellido_paterno");
            String nombreUsuario = (String) solicitudInfo.get("nombre_usuario");
            
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom("finner.oficial.2025@gmail.com");
            mensaje.setTo(correoInstructor);
            mensaje.setSubject("¡Felicidades! Su solicitud como instructor ha sido aprobada - Finner");
            
            String cuerpoMensaje = "Estimado/a " + nombreInstructor + ",\n\n" +
                    "Nos complace informarle que su solicitud para convertirse en instructor en la plataforma Finner ha sido aprobada.\n\n" +
                    "Ahora puede acceder a la plataforma con su nombre de usuario: " + nombreUsuario + "\n\n" +
                    "Como instructor, podrá crear y gestionar cursos, interactuar con los alumnos y contribuir al crecimiento de nuestra comunidad educativa.\n\n" +
                    "Si tiene alguna pregunta o necesita asistencia, no dude en contactar a nuestro equipo de soporte.\n\n" +
                    "¡Le damos la bienvenida al equipo de instructores de Finner!\n\n" +
                    "Atentamente,\n" +
                    "El equipo de Finner";
            
            mensaje.setText(cuerpoMensaje);
            
            javaMailSender.send(mensaje);
        } catch (Exception e) {
            // Solo registramos la excepción pero no interrumpimos el flujo
            System.err.println("Error al enviar correo de aceptación de instructor: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> verSolicitudInstructor() {
        try {
            return jdbcTemplate.queryForList(
                "SELECT id_solicitud_instructor, id_rol, nombre, " +
                "apellido_paterno, apellido_materno, correo, " +
                "nombre_usuario, telefono, direccion, " +
                "fecha_solicitud, estatus_solicitud " +
                "FROM SolicitudInstructor " +
                "ORDER BY fecha_solicitud ASC"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}