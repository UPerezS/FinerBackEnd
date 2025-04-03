package mx.utng.finer_back_end.Administrador.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import java.net.URL;
import java.net.URLDecoder;
import jakarta.activation.FileDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
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
     * 
     * Este método elimina a un alumno de un curso específico utilizando una función
     * almacenada en la base de datos PostgreSQL. El proceso consiste en:
     * 1. Llamar a la función 'eliminar_alumno_curso' de PostgreSQL, pasando la
     * matrícula
     * del alumno y el ID del curso como parámetros
     * 2. La función de base de datos se encarga de verificar si el alumno está
     * inscrito
     * en el curso y realizar la eliminación si corresponde
     * 3. Retornar el resultado de la operación como un mensaje descriptivo
     * 
     * @param matricula Identificador único del alumno (matrícula)
     * @param idCurso   Identificador único del curso
     * @return Mensaje indicando el resultado de la operación: éxito, alumno no
     *         inscrito,
     *         o error en caso de excepción
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
                    idCurso);

            return resultado;
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir
            return "Error al eliminar al alumno del curso: " + e.getMessage();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * Este método rechaza una solicitud de curso y notifica al instructor por
     * correo electrónico.
     * El proceso consiste en:
     * 1. Verificar si la solicitud existe en la base de datos
     * 2. Comprobar que la solicitud no haya sido rechazada o aprobada previamente
     * 3. Obtener la información del instructor y del curso desde la base de datos
     * 4. Enviar un correo electrónico al instructor informándole sobre el rechazo
     * 5. Actualizar el estado de la solicitud a 'rechazado' en la base de datos
     * 
     * @param idSolicitudCurso Identificador único de la solicitud de curso a
     *                         rechazar
     * @param motivoRechazo    Texto que explica la razón por la cual se rechaza el
     *                         curso
     * @param tituloCurso      Título del curso que se está rechazando (opcional, se
     *                         puede obtener de la BD)
     * @return Mensaje indicando el resultado de la operación: "Rechazado" en caso
     *         de éxito,
     *         mensaje de error específico en caso contrario
     */
    @Override
    @Transactional
    public String rechazarCurso(Long idSolicitudCurso, String motivoRechazo, String tituloCurso) {
        try {
            // Primero verificamos si existe el registro
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM solicitudcurso WHERE id_solicitud_curso = ?",
                    Integer.class,
                    idSolicitudCurso);

            // Log para depuración
            System.out.println("Buscando solicitud con ID: " + idSolicitudCurso);
            System.out.println("Registros encontrados: " + count);

            if (count != null && count > 0) {
                // Verificar el estado actual
                String estadoActual = jdbcTemplate.queryForObject(
                        "SELECT estatus FROM solicitudcurso WHERE id_solicitud_curso = ?",
                        String.class,
                        idSolicitudCurso);

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
                        idSolicitudCurso);

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

                // El registro existe y está en estado válido para rechazar, procedemos a
                // actualizarlo
                int filasAfectadas = jdbcTemplate.update(
                        "UPDATE solicitudcurso SET estatus = 'rechazada' WHERE id_solicitud_curso = ?",
                        idSolicitudCurso);

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
     * Envía un correo electrónico al instructor notificando el rechazo de su
     * solicitud de curso.
     * 
     * @param correoInstructor Correo del instructor al que se enviará la
     *                         notificación
     * @param motivoRechazo    Motivo por el cual se rechazó el curso
     * @param tituloCurso      Título del curso rechazado
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
    public String crearCategoria(Integer idUsuarioInstructor, String nombreCategoria, String descripcion) {
        try {
            // Verificar si la categoría ya existe
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM categoria WHERE nombre_categoria = ?",
                    Integer.class,
                    nombreCategoria);

            if (count != null && count > 0) {
                return "Error: Ya existe una categoría con el nombre '" + nombreCategoria + "'";
            }

            // Verificar si existe una solicitud de categoría y su estado
            // Nota: Según la documentación, la solicitud debe estar en la tabla
            // solicitudcategoria
            // y debe tener un estado 'aprobado' para poder crear la categoría
            try {
                String estadoSolicitud = jdbcTemplate.queryForObject(
                        "SELECT estatus FROM solicitudcategoria WHERE nombre_categoria = ?",
                        String.class,
                        nombreCategoria);

                // Log para depuración
                System.out.println(
                        "Estado de la solicitud para la categoría '" + nombreCategoria + "': " + estadoSolicitud);

                // Verificar si el estado es 'aprobado'
                if (estadoSolicitud == null || !"aprobado".equals(estadoSolicitud)) {
                    return "Error: La solicitud de categoría no está aprobada o no existe";
                }
            } catch (Exception e) {
                // Si ocurre un error al buscar la solicitud, asumimos que no existe
                System.err.println("Error al verificar el estado de la solicitud: " + e.getMessage());
                return "Error: No se encontró una solicitud de categoría aprobada";
            }

            // Si llegamos aquí, la solicitud existe y está aprobada, procedemos a crear la
            // categoría
            int filasAfectadas = jdbcTemplate.update(
                    "INSERT INTO categoria (nombre_categoria, descripcion) VALUES (?, ?)",
                    nombreCategoria,
                    descripcion);

            if (filasAfectadas > 0) {
                // Obtener el ID de la categoría recién creada
                Integer idCategoria = jdbcTemplate.queryForObject(
                    "SELECT id_categoria FROM categoria WHERE nombre_categoria = ?", 
                    Integer.class, 
                    nombreCategoria
                );
                
                // Nota: La tabla log_categoria no existe en el esquema actual de la base de datos
                // Por lo tanto, no intentamos registrar en ella y continuamos con el flujo normal
                
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
            // Llamar a la función de PostgreSQL para modificar la descripción de la
            // categoría
            String resultado = jdbcTemplate.queryForObject(
                    "SELECT modificar_desc_categoria(?, ?)",
                    String.class,
                    idCategoria,
                    nuevaDescripcion);

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
                    idCategoria);

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
                    idCategoria);

            int solicitudesActualizadas = jdbcTemplate.update(
                    "UPDATE solicitudcurso SET id_categoria = 0 WHERE id_categoria = ?",
                    idCategoria);

            System.out.println("Cursos reasignados: " + cursosActualizados);
            System.out.println("Solicitudes reasignadas: " + solicitudesActualizadas);

            // Now try to delete the category
            int rowsAffected = jdbcTemplate.update(
                    "DELETE FROM categoria WHERE id_categoria = ?",
                    idCategoria);

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
                    idSolicitudCurso);

            if (count == null || count == 0) {
                return "La solicitud de curso no existe.";
            }

            // Start transaction and acquire exclusive lock on the solicitudcurso row
            jdbcTemplate.execute("BEGIN");

            // Lock the solicitudcurso row first to prevent concurrent processing
            Map<String, Object> solicitudInfo = jdbcTemplate.queryForMap(
                    "SELECT id_solicitud_curso, estatus, id_curso FROM solicitudcurso WHERE id_solicitud_curso = ? FOR UPDATE",
                    idSolicitudCurso);

            // Check if the course has already been created for this request
            Integer idCursoExistente = (Integer) solicitudInfo.get("id_curso");
            if (idCursoExistente != null) {
                jdbcTemplate.execute("COMMIT");
                return "El curso ya ha sido creado anteriormente con ID: " + idCursoExistente;
            }

            // Check the current status
            String estadoActual = (String) solicitudInfo.get("estatus");

            // Log for debugging
            System.out.println("Estado actual de la solicitud: " + estadoActual);
            
            if ("aprobada".equals(estadoActual)) {
                jdbcTemplate.execute("COMMIT");
                return "La solicitud ya ha sido aprobada anteriormente";
            }

            if ("rechazada".equals(estadoActual)) {
                jdbcTemplate.execute("COMMIT");
                return "No se puede aprobar una solicitud que ya ha sido rechazada";
            }

            // Get the course request details
            Map<String, Object> solicitudCurso = jdbcTemplate.queryForMap(
                    "SELECT id_usuario_instructor, id_categoria, titulo_curso_solicitado, descripcion FROM solicitudcurso WHERE id_solicitud_curso = ?",
                    idSolicitudCurso);

            // Check if a course with the same title and instructor already exists
            List<Map<String, Object>> existingCourses = jdbcTemplate.queryForList(
                    "SELECT id_curso FROM curso WHERE titulo_curso = ? AND id_usuario_instructor = ? FOR UPDATE",
                    solicitudCurso.get("titulo_curso_solicitado"),
                    solicitudCurso.get("id_usuario_instructor"));

            Integer idCurso;

            if (!existingCourses.isEmpty()) {
                // Course with same title and instructor already exists, get its ID
                idCurso = (Integer) existingCourses.get(0).get("id_curso");
                System.out.println("Se encontró un curso existente con el mismo título e instructor, ID: " + idCurso);
            } else {
                // Create the course in the curso table
                KeyHolder keyHolder = new GeneratedKeyHolder();

                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO curso (id_usuario_instructor, id_categoria, titulo_curso, descripcion) VALUES (?, ?, ?, ?)",
                            new String[] { "id_curso" });
                    ps.setObject(1, solicitudCurso.get("id_usuario_instructor"));
                    ps.setObject(2, solicitudCurso.get("id_categoria"));
                    ps.setString(3, (String) solicitudCurso.get("titulo_curso_solicitado"));
                    ps.setString(4, (String) solicitudCurso.get("descripcion"));
                    return ps;
                }, keyHolder);

                // Get the ID directly from the key holder
                idCurso = (Integer) keyHolder.getKeys().get("id_curso");
                System.out.println("Nuevo curso creado con ID: " + idCurso);
            }

            // Update the status and course ID in a single operation
            int filasAfectadas = jdbcTemplate.update(
                "UPDATE solicitudcurso SET estatus = 'aprobada', id_curso = ? WHERE id_solicitud_curso = ? AND id_curso IS NULL",
                idCurso,
                idSolicitudCurso
            );
            
            jdbcTemplate.execute("COMMIT");
            
            if (filasAfectadas > 0) {
                return "El curso ha sido aprobado exitosamente y asociado al catálogo con ID: " + idCurso;
            } else {
                // The update didn't affect any rows, which means another transaction might have
                // updated it
                Integer idCursoFinal = jdbcTemplate.queryForObject(
                        "SELECT id_curso FROM solicitudcurso WHERE id_solicitud_curso = ?",
                        Integer.class,
                        idSolicitudCurso);

                if (idCursoFinal != null) {
                    return "El curso ya ha sido asociado a esta solicitud con ID: " + idCursoFinal;
                } else {
                    throw new RuntimeException("Error al actualizar el estado de la solicitud");
                }
            }
        } catch (Exception e) {
            // Rollback in case of error
            try {
                jdbcTemplate.execute("ROLLBACK");
            } catch (Exception rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }

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
            // Get user ID by username
            Integer idUsuario = jdbcTemplate.queryForObject(
                    "SELECT id_usuario FROM usuario WHERE nombre_usuario = ?",
                    Integer.class,
                    nombreUsuario);

            if (idUsuario == null) {
                return "No se encontró el usuario con el nombre de usuario proporcionado";
            }

            // Call database function to block user
            String resultado = jdbcTemplate.queryForObject(
                    "SELECT bloquear_usuario(?)",
                    String.class,
                    idUsuario);

            return resultado;

        } catch (DataAccessException e) {
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
                    nombreUsuario);

            if (count == null || count == 0) {
                return Map.of("error", "No se encontró el usuario con el nombre de usuario proporcionado");
            }

            // Obtener los datos del usuario
            Map<String, Object> usuario = jdbcTemplate.queryForMap(
                    "SELECT u.*, r.rol FROM usuario u JOIN rol r ON u.id_rol = r.id_rol WHERE u.nombre_usuario = ?",
                    nombreUsuario);

            // Verificar si el usuario es instructor y tiene cédula profesional
            if (usuario.get("id_rol") != null && Integer.parseInt(usuario.get("id_rol").toString()) == 2) {
                // Verificar el estado de validación de la cédula
                String estadoValidacion = jdbcTemplate.queryForObject(
                        "SELECT estatus FROM validacioncedula WHERE id_usuario = ?",
                        String.class,
                        usuario.get("id_usuario"));

                usuario.put("estado_cedula", estadoValidacion != null ? estadoValidacion : "pendiente");
            }

            return usuario;
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "Error al obtener los datos del usuario: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> buscarUsuarioNombre(String nombreUsuario) {
        try {
            // Buscar usuarios por coincidencia en nombre
            String sql = "SELECT u.*, r.rol FROM usuario u " +
                    "JOIN rol r ON u.id_rol = r.id_rol " +
                    "WHERE LOWER(u.nombre) LIKE LOWER(?) OR " +
                    "LOWER(u.apellido_paterno) LIKE LOWER(?) OR " +
                    "LOWER(u.apellido_materno) LIKE LOWER(?) OR " +
                    "LOWER(u.nombre_usuario) LIKE LOWER(?) OR " +
                    "LOWER(CONCAT(u.nombre, ' ', u.apellido_paterno)) LIKE LOWER(?) OR " +
                    "LOWER(CONCAT(u.nombre, ' ', u.apellido_materno)) LIKE LOWER(?)";

            String termino = "%" + nombreUsuario + "%";

            return jdbcTemplate.queryForList(sql,
                    termino, termino, termino, termino, termino, termino);
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
                        rs.getString("cedula_pdf"));
                instructor.setId(rs.getInt("id_usuario"));
                return instructor;
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
                            idSolicitudInstructor);
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
     * Envía un correo electrónico al instructor notificando la aprobación de su
     * solicitud de curso.
     * 
     * @param solicitudInfo Información de la solicitud del instructor
     */
    private void enviarCorreoAceptacionInstructor(Map<String, Object> solicitudInfo) {
        try {
            String correoInstructor = (String) solicitudInfo.get("correo");
            String nombreInstructor = (String) solicitudInfo.get("nombre") + " " +
                    (String) solicitudInfo.get("apellido_paterno");
            String nombreUsuario = (String) solicitudInfo.get("nombre_usuario");
            
            // Crear un mensaje MIME para soportar contenido HTML
            MimeMessage mensaje = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            
            helper.setFrom("finner.oficial.2025@gmail.com");
            helper.setTo(correoInstructor);
            helper.setSubject("¡Felicidades! Su solicitud como instructor ha sido aprobada - Finner");
            
            // Intentar cargar el logo
            URL logoUrl = getClass().getClassLoader().getResource("finer_logo.png");
            String logoPath = "";
            if (logoUrl != null) {
                logoPath = URLDecoder.decode(logoUrl.getPath(), "UTF-8");
                System.out.println("Ruta decodificada del logo: " + logoPath);
            } else {
                System.err.println("No se encontró el recurso finer_logo.png, se enviará sin logo.");
            }
            
            // Construir el contenido HTML del mensaje
            String cuerpoMensaje = "<html>" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f5f5f5; color: #333; padding: 20px;\">" +
                "<div style=\"background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">" +
                "<h2 style=\"color: #2d6a4f;\">¡Felicidades! Su solicitud como instructor ha sido aprobada</h2>" +
                "<p>Estimado/a " + nombreInstructor + ",</p>" +
                "<p>Nos complace informarle que su solicitud para convertirse en instructor en la plataforma Finner ha sido <strong>aprobada</strong>.</p>" +
                "<p>Ahora puede acceder a la plataforma con su nombre de usuario: <strong>" + nombreUsuario + "</strong></p>" +
                "<p>Como instructor, podrá crear y gestionar cursos, interactuar con los alumnos y contribuir al crecimiento de nuestra comunidad educativa.</p>" +
                "<p>Si tiene alguna pregunta o necesita asistencia, no dude en contactar a nuestro equipo de soporte.</p>" +
                "<p>¡Le damos la bienvenida al equipo de instructores de Finner!</p>" +
                "<p>Atentamente,</p>" +
                "<p>El equipo de Finner</p>" +
                (logoUrl != null ? "<p style=\"text-align:center;\"><img src=\"cid:finerLogo\" alt=\"Finer Logo\" style=\"max-width: 200px;\" /></p>" : "") +
                "</div>" +
                "</body>" +
                "</html>";
            
            helper.setText(cuerpoMensaje, true);
            
            // Agregar el logo si está disponible
            if (logoUrl != null) {
                FileDataSource dataSource = new FileDataSource(logoPath);
                helper.addInline("finerLogo", dataSource);
            }
            
            javaMailSender.send(mensaje);
            System.out.println("Correo de aceptación enviado a: " + correoInstructor);
        } catch (Exception e) {
            // Solo registramos la excepción pero no interrumpimos el flujo
            System.err.println("Error al enviar correo de aceptación de instructor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Map<String, Object>> verSolicitudInstructor() {
        try {
    
            return jdbcTemplate.queryForList("SELECT * FROM ver_solicitud_instructor()");
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene todas las solicitudes de categoría.
     * 
     * @return Lista de solicitudes de categoría
     */
    @Override
    public List<Map<String, Object>> verSolicitudesCategoria() {
        String sql = "SELECT sc.id_solicitud_categoria, sc.id_usuario_instructor, sc.id_usuario_admin, " +
                "sc.nombre_categoria, sc.descripcion, sc.estatus, sc.fecha_solicitud, " +
                "u.nombre, u.apellido_paterno, u.apellido_materno, u.correo " +
                "FROM solicitudcategoria sc " +
                "JOIN usuario u ON sc.id_usuario_instructor = u.id_usuario " +
                "ORDER BY sc.fecha_solicitud DESC";

        return jdbcTemplate.queryForList(sql);
    }

    /**
     * Aprueba una solicitud de categoría y crea la categoría en el sistema.
     * 
     * @param idSolicitudCategoria ID de la solicitud de categoría a aprobar
     * @return Mensaje con el resultado de la operación
     */
    @Override
    public String aprobarCategoria(Integer idSolicitudCategoria) {
        try {
            // Verificar si la solicitud existe
            String checkSql = "SELECT COUNT(*) FROM solicitudcategoria WHERE id_solicitud_categoria = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, idSolicitudCategoria);
            
            if (count == null || count == 0) {
                return "Error: La solicitud de categoría no existe";
            }
            
            // Obtener los datos de la solicitud
            String getSql = "SELECT nombre_categoria, descripcion, estatus FROM solicitudcategoria WHERE id_solicitud_categoria = ?";
            Map<String, Object> solicitud = jdbcTemplate.queryForMap(getSql, idSolicitudCategoria);
            
            String estatus = (String) solicitud.get("estatus");
            if (!"en revision".equals(estatus)) {
                return "Error: La solicitud de categoría no está en estado de revisión";
            }
            
            String nombreCategoria = (String) solicitud.get("nombre_categoria");
            String descripcion = (String) solicitud.get("descripcion");
            
            // Verificar si ya existe una categoría con ese nombre
            String checkCatSql = "SELECT COUNT(*) FROM categoria WHERE nombre_categoria = ?";
            Integer catCount = jdbcTemplate.queryForObject(checkCatSql, Integer.class, nombreCategoria);
            
            if (catCount != null && catCount > 0) {
                // Actualizar el estatus de la solicitud a aprobada
                String updateSql = "UPDATE solicitudcategoria SET estatus = 'aprobada' WHERE id_solicitud_categoria = ?";
                jdbcTemplate.update(updateSql, idSolicitudCategoria);
                return "Solicitud de categoría aprobada exitosamente. La categoría ya existía en el sistema.";
            }
            
            // Crear la nueva categoría
            String insertSql = "INSERT INTO categoria (nombre_categoria, descripcion) VALUES (?, ?)";
            jdbcTemplate.update(insertSql, nombreCategoria, descripcion);
            
            // Actualizar el estatus de la solicitud a aprobada
            String updateSql = "UPDATE solicitudcategoria SET estatus = 'aprobada' WHERE id_solicitud_categoria = ?";
            jdbcTemplate.update(updateSql, idSolicitudCategoria);
            
            return "Solicitud de categoría aprobada exitosamente. Se ha creado la nueva categoría.";
            
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al aprobar la solicitud de categoría: " + e.getMessage();
        }
    }
}
