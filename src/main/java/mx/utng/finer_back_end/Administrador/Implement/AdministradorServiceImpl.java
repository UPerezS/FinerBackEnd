package mx.utng.finer_back_end.Administrador.Implement;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.List;

import mx.utng.finer_back_end.Administrador.Dao.AdministradorDao;
import mx.utng.finer_back_end.Administrador.Services.AdministradorService;
import mx.utng.finer_back_end.Documentos.UsuarioDocumento;
import mx.utng.finer_back_end.Instructor.Documentos.AlumnoDetalleNombreDTO;

import org.springframework.http.HttpHeaders;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AdministradorDao administradorDao;

    private final String API_KEY = "fb9a6eb9-05c4-4c7f-8b5b-9900053358cb";
    private final String API_URL = "https://api.apis.net.mx/v1/cedulaprofesional/";

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
                    idCurso);

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
    public String rechazarCurso(Long idSolicitudCurso, String correoInstructor, String motivoRechazo,
            String tituloCurso) {
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

                if ("rechazado".equals(estadoActual)) {
                    return "La solicitud ya ha sido rechazada anteriormente";
                }

                if ("aprobado".equals(estadoActual)) {
                    return "No se puede rechazar una solicitud que ya ha sido aprobada";
                }

                // Enviar el correo antes de actualizar el estado, ya que el trigger eliminará
                // la solicitud
                enviarCorreoRechazo(correoInstructor, motivoRechazo, tituloCurso);

                // El registro existe y está en estado válido para rechazar, procedemos a
                // actualizarlo
                int filasAfectadas = jdbcTemplate.update(
                        "UPDATE solicitudcurso SET estatus = 'rechazado' WHERE id_solicitud_curso = ?",
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
    public String crearCategoria(Integer idUsuarioInstructor, Integer idUsuarioAdmin, String nombreCategoria,
            String descripcion) {
        try {
            // Llamar a la función de PostgreSQL para crear la solicitud de categoría
            String resultado = jdbcTemplate.queryForObject(
                    "SELECT solicitar_creacion_categoria(?, ?, ?, ?)",
                    String.class,
                    idUsuarioInstructor,
                    idUsuarioAdmin,
                    nombreCategoria,
                    descripcion);

            return resultado;
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir
            e.printStackTrace(); // Para ver el error completo en los logs
            return "Error al crear la solicitud de categoría: " + e.getMessage();
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

            // Check the current status
            String estadoActual = jdbcTemplate.queryForObject(
                    "SELECT estatus FROM solicitudcurso WHERE id_solicitud_curso = ?",
                    String.class,
                    idSolicitudCurso);

            // Log for debugging
            System.out.println("Estado actual de la solicitud: " + estadoActual);

            if ("aprobado".equals(estadoActual)) {
                return "La solicitud ya ha sido aprobada anteriormente";
            }

            if ("rechazado".equals(estadoActual)) {
                return "No se puede aprobar una solicitud que ya ha sido rechazada";
            }

            // Update the status to 'aprobado'
            int filasAfectadas = jdbcTemplate.update(
                    "UPDATE solicitudcurso SET estatus = 'aprobado' WHERE id_solicitud_curso = ?",
                    idSolicitudCurso);

            if (filasAfectadas > 0) {
                return "El curso ha sido aprobado exitosamente.";
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
            // Llamar a la función de PostgreSQL para bloquear al usuario
            String resultado = jdbcTemplate.queryForObject(
                    "SELECT bloquear_usuario(?)",
                    String.class,
                    nombreUsuario);

            return resultado;
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir
            e.printStackTrace(); // Para ver el error completo en los logs
            return "Error al bloquear al usuario: " + e.getMessage();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUsuario(String nombreUsuario) {
        try {
            // Consultar los datos del usuario
            Map<String, Object> usuario = jdbcTemplate.queryForMap(
                    "SELECT u.*, r.nombre_rol " +
                            "FROM Usuario u " +
                            "JOIN Rol r ON u.id_rol = r.id_rol " +
                            "WHERE u.nombre_usuario = ?",
                    nombreUsuario);

            // Si el usuario tiene número de cédula, validarla
            if (usuario.containsKey("numero_cedula") && usuario.get("numero_cedula") != null) {
                String numeroCedula = usuario.get("numero_cedula").toString();
                Map<String, Object> datosCedula = validarCedulaProfesional(numeroCedula);
                usuario.put("datos_cedula", datosCedula);
            }

            return usuario;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al obtener los datos del usuario: " + e.getMessage());
            return error;
        }
    }

    /**
     * Valida una cédula profesional utilizando la API externa.
     * 
     * @param numeroCedula Número de cédula a validar
     * @return Map con la información de la cédula profesional
     */
    private Map<String, Object> validarCedulaProfesional(String numeroCedula) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Api-Key", API_KEY);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL + numeroCedula,
                    HttpMethod.GET,
                    entity,
                    Map.class);

            return response.getBody();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al validar la cédula profesional: " + e.getMessage());
            return error;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> buscarUsuarioNombre(String busqueda) {
        try {
            // Preparar el término de búsqueda para SQL LIKE
            String termino = "%" + busqueda.toLowerCase() + "%";

            // Realizar la búsqueda en la base de datos
            return jdbcTemplate.queryForList(
                    "SELECT u.*, r.nombre_rol " +
                            "FROM Usuario u " +
                            "JOIN Rol r ON u.id_rol = r.id_rol " +
                            "WHERE LOWER(u.nombre) LIKE ? " +
                            "   OR LOWER(u.apellido_paterno) LIKE ? " +
                            "   OR LOWER(u.apellido_materno) LIKE ?",
                    termino, termino, termino);
        } catch (Exception e) {
            e.printStackTrace();
            // Retornar lista vacía en caso de error
            return new ArrayList<>();
        }
    }

    @Override
    public List<UsuarioDocumento> getUsuarios() {
        // Llamada al DAO para obtener los resultados
        List<Object[]> resultados = administradorDao.getUsuarios();
    
        List<UsuarioDocumento> usuarios = new ArrayList<>();
    
        // Iterar sobre los resultados y mapearlos a UsuarioDocumento
        for (Object[] row : resultados) {
            // Crear un nuevo UsuarioDocumento con todos los campos, incluyendo idUsuario
            UsuarioDocumento usuario = new UsuarioDocumento(
                    (String) row[2],  // nombre (String)
                    (Integer) row[1], // idRol (Integer)
                    (String) row[3],  // apellidoPaterno (String)
                    (String) row[4],  // apellidoMaterno (String)
                    (String) row[5],  // correo (String)
                    null,              // contrasenia no está en la consulta
                    (String) row[6],  // nombreUsuario (String)
                    (String) row[7],  // telefono (String)
                    (String) row[8],  // direccion (String)
                    (String) row[9]   // estatus (String)
            );
            
            // Asignar el idUsuario desde el primer valor de la fila (row[0])
            usuario.setId((Integer) row[0]);
    
            // Agregar el usuario a la lista
            usuarios.add(usuario);
        }
    
        return usuarios;
    }
    
} // Closing brace for the class
