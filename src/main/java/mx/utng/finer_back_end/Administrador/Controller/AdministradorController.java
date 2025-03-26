package mx.utng.finer_back_end.Administrador.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Administrador.DTO.CategoriaDTO;
import mx.utng.finer_back_end.Administrador.Services.AdministradorService;
import mx.utng.finer_back_end.Documentos.UsuarioDocumento;

@RestController
@RequestMapping("/api/admin")  // Changed from "/api/administrador"
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Endpoint para eliminar un alumno de un curso específico.
     * 
     * Este método verifica si el alumno seleccionado está inscrito en algún curso.
     * En caso de que lo esté, permite su eliminación del curso. Para lograrlo, se
     * elimina
     * el registro correspondiente en la tabla de inscripciones, utilizando la
     * matrícula
     * y el id del curso como referencia. Como resultado, el alumno queda
     * desvinculado del curso.
     *
     * @param obj Objeto que contiene la matrícula del alumno (matricula) y el ID
     *            del curso (idCurso)
     * @return ResponseEntity con el mensaje de éxito o error en formato JSON.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Eliminación completada correctamente.
     *         - `404 Not Found`: Si el alumno no está inscrito en el curso.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la
     *         eliminación.
     */
    @PostMapping("/eliminarCursoAlumno")
    public ResponseEntity<Map<String, Object>> eliminarCursoAlumno(@RequestBody Map<String, Object> obj) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Extraer los valores del objeto recibido
            String matricula = (String) obj.get("matricula");
            Integer idCurso = Integer.parseInt(obj.get("idCurso").toString());

            // Validar que los datos necesarios estén presentes
            if (matricula == null || matricula.isEmpty() || idCurso == null) {
                response.put("mensaje", "La matrícula y el ID del curso son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Llamar al servicio para eliminar al alumno del curso
            String resultado = administradorService.eliminarAlumnoCurso(matricula, idCurso);

            // Verificar el resultado
            if (resultado.equals("El alumno no está inscrito en este curso.")) {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (resultado.equals("Alumno eliminado exitosamente del curso.")) {
                response.put("mensaje", resultado);
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar eliminar al alumno del curso");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "El ID del curso debe ser un número entero válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para rechazar una solicitud de curso.
     * 
     * Este método permite al administrador rechazar un curso de los que están
     * listados
     * para revisión. Al rechazar el curso, se cambia su estatus a 'rechazado' en la
     * base
     * de datos y se envía un correo al instructor con el mensaje correspondiente.
     *
     * @param obj Objeto que contiene el ID de la solicitud de curso
     *            (idSolicitudCurso),
     *            el correo del instructor (correoInstructor) y el motivo del
     *            rechazo (motivoRechazo)
     * @return ResponseEntity con el mensaje de éxito o error en formato JSON.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Rechazo completado correctamente.
     *         - `404 Not Found`: Si no se encuentra la solicitud de curso.
     *         - `400 Bad Request`: Si faltan datos requeridos.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar el
     *         rechazo.
     */
    @PostMapping("/rechazarCurso")
    public ResponseEntity<Map<String, Object>> rechazarCurso(@RequestBody Map<String, Object> obj) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Extraer los valores del objeto recibido
            Long idSolicitudCurso = Long.parseLong(obj.get("idSolicitudCurso").toString());
            String motivoRechazo = (String) obj.get("motivoRechazo");
            // El título del curso es opcional, se obtendrá de la base de datos si no se proporciona
            String tituloCurso = obj.containsKey("tituloCurso") ? (String) obj.get("tituloCurso") : "";

            // Validar que los datos necesarios estén presentes
            if (idSolicitudCurso == null || motivoRechazo == null || motivoRechazo.isEmpty()) {
                response.put("mensaje", "El ID de la solicitud y el motivo del rechazo son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Llamar al servicio para rechazar el curso
            String resultado = administradorService.rechazarCurso(idSolicitudCurso, motivoRechazo, tituloCurso);

            // Verificar el resultado
            if (resultado.equals("No se encontró la solicitud de curso con el ID proporcionado")) {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (resultado.equals("Rechazado")) {
                response.put("mensaje", "Solicitud de curso rechazada exitosamente");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (NumberFormatException e) {
            response.put("mensaje", "El ID de la solicitud de curso debe ser un número válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para solicitar la creación de una nueva categoría.
     * 
     * Este método se usa para registrar una nueva solicitud de categoría. Recibe
     * los datos
     * necesarios como el ID del instructor, ID del administrador, nombre de la
     * categoría y
     * descripción. La solicitud es procesada y almacenada en la tabla
     * SolicitudCategoria.
     *
     * @param obj Objeto que contiene los datos de la solicitud
     *            (idUsuarioInstructor,
     *            idUsuarioAdmin, nombreCategoria, descripcion)
     * @return ResponseEntity con el mensaje de éxito o error en formato JSON.
     * 
     *         Posibles respuestas:
     *         - `201 Created`: Solicitud creada correctamente.
     *         - `400 Bad Request`: Si faltan datos requeridos.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la
     *         solicitud.
     */
    @PostMapping("/crearCategoria")
    public ResponseEntity<Map<String, Object>> crearCategoria(@RequestBody Map<String, Object> obj) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Extraer los valores del objeto recibido
            Integer idUsuarioInstructor = Integer.parseInt(obj.get("idUsuarioInstructor").toString());
            Integer idUsuarioAdmin = Integer.parseInt(obj.get("idUsuarioAdmin").toString());
            String nombreCategoria = (String) obj.get("nombreCategoria");
            String descripcion = (String) obj.get("descripcion");

            // Validar que los datos necesarios estén presentes
            if (idUsuarioInstructor == null || idUsuarioAdmin == null ||
                    nombreCategoria == null || nombreCategoria.isEmpty()) {
                response.put("mensaje",
                        "El ID del instructor, ID del administrador y nombre de la categoría son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Llamar al servicio para crear la solicitud de categoría
            String resultado = administradorService.crearCategoria(idUsuarioInstructor, idUsuarioAdmin, nombreCategoria,
                    descripcion);

            // Verificar el resultado - MODIFICADO PARA RECONOCER EL MENSAJE DE ÉXITO
            // CORRECTO
            if (resultado.contains("enviada correctamente")) {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar crear la solicitud de categoría");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "Los IDs deben ser números enteros válidos");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para modificar la descripción de una categoría existente.
     * 
     * Este método permite realizar actualizaciones parciales sin necesidad de
     * modificar
     * atributos específicos. Recibe el identificador de la categoría y un DTO con
     * los
     * datos a actualizar.
     *
     * @param id           ID de la categoría a modificar
     * @param categoriaDTO Objeto DTO que contiene la nueva descripción
     * @return ResponseEntity con el mensaje de éxito o error en formato JSON.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Modificación completada correctamente.
     *         - `404 Not Found`: Si no se encuentra la categoría con el ID
     *         proporcionado.
     *         - `400 Bad Request`: Si faltan datos requeridos.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la
     *         modificación.
     */
    @PutMapping("/modificarCategoria/{id}")
    public ResponseEntity<Map<String, Object>> modificarCategoria(@PathVariable Integer id,
            @RequestBody CategoriaDTO categoriaDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validar que los datos necesarios estén presentes
            if (id == null || categoriaDTO.getDescripcion() == null || categoriaDTO.getDescripcion().isEmpty()) {
                response.put("mensaje", "El ID de la categoría y la descripción son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Llamar al servicio para modificar la descripción de la categoría
            String resultado = administradorService.modificarCategoriaDescripcion(id, categoriaDTO.getDescripcion());

            // Verificar el resultado
            if (resultado.contains("Error: La categoría no existe")) {
                response.put("mensaje", "No se encontró la categoría con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (resultado.contains("actualizada exitosamente")) {
                response.put("mensaje", resultado);
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar modificar la categoría");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "El ID de la categoría debe ser un número entero válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para eliminar una categoría existente.
     * 
     * Este método elimina una categoría de la base de datos. Antes de eliminar,
     * verifica que no sea la categoría predeterminada y reasigna los cursos y
     * solicitudes asociados a la categoría "Sin elegir".
     *
     * @param id ID de la categoría a eliminar
     * @return ResponseEntity con el mensaje de éxito o error en formato JSON.
     */
    @DeleteMapping("/borrarCategoria/{id}")
    public ResponseEntity<Map<String, Object>> borrarCategoria(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validar que el ID no sea nulo
            if (id == null) {
                response.put("mensaje", "El ID de la categoría es obligatorio");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Validar que no se intente eliminar la categoría predeterminada
            if (id == 0) {
                response.put("mensaje", "No se puede eliminar la categoría predeterminada 'Sin elegir'");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Verificar si la categoría existe
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM categoria WHERE id_categoria = ?",
                    Integer.class,
                    id);

            if (count != null && count == 0) {
                response.put("mensaje", "No se encontró la categoría con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Llamar al servicio para eliminar la categoría
            Boolean resultado = administradorService.eliminarCategoria(id);

            // Verificar el resultado
            if (resultado) {
                response.put("mensaje", "Categoría eliminada correctamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje",
                        "No se pudo eliminar la categoría. Puede estar siendo utilizada por otros registros.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar eliminar la categoría");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "El ID de la categoría debe ser un número entero válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para aprobar una solicitud de curso.
     * 
     * Este método permite al administrador aprobar cursos que los instructores
     * hayan creado,
     * asegurando que cumplen con la estructura requerida. Al aprobarlo actualizará
     * el estatus
     * de la tabla SolicitudCurso a "Aprobado" y se creará el curso.
     *
     * @param obj Objeto que contiene el ID de la solicitud de curso
     *            (idSolicitudCurso)
     * @return ResponseEntity con el mensaje de éxito o error en formato JSON.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Aprobación completada correctamente.
     *         - `404 Not Found`: Si no se encuentra la solicitud de curso.
     *         - `400 Bad Request`: Si la solicitud no está en estado de revisión.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la
     *         aprobación.
     */
    @PostMapping("/aprobarCurso")
    public ResponseEntity<Map<String, Object>> aprobarCurso(@RequestBody Map<String, Object> obj) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Extraer los valores del objeto recibido
            Integer idSolicitudCurso = Integer.parseInt(obj.get("idSolicitudCurso").toString());

            // Validar que los datos necesarios estén presentes
            if (idSolicitudCurso == null) {
                response.put("mensaje", "El ID de la solicitud de curso es obligatorio");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Llamar al servicio para aprobar el curso
            String resultado = administradorService.aprobarCurso(idSolicitudCurso);

            // Verificar el resultado
            if (resultado.contains("no existe")) {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (resultado.contains("no está en estado de revisión")) {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } else if (resultado.contains("aprobado exitosamente")) {
                response.put("mensaje", resultado);
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar aprobar el curso");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "El ID de la solicitud de curso debe ser un número entero válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para bloquear a un usuario en el sistema.
     * 
     * Este método cambia el rol de un usuario a 'bloqueado', lo que impide que
     * pueda
     * realizar acciones en el sistema. Si el usuario ya está bloqueado o no existe,
     * se retorna el mensaje correspondiente.
     *
     * @param obj Objeto que contiene el nombre de usuario a bloquear
     * @return ResponseEntity con el mensaje de éxito o error en formato JSON.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Usuario bloqueado correctamente.
     *         - `404 Not Found`: Si no se encuentra el usuario.
     *         - `400 Bad Request`: Si faltan datos requeridos.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar el
     *         bloqueo.
     */
    @PostMapping("/bloquearUsuario")
    public ResponseEntity<Map<String, Object>> bloquearUsuario(@RequestBody Map<String, Object> obj) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Extraer el nombre de usuario del objeto recibido
            String nombreUsuario = (String) obj.get("nombreUsuario");

            // Validar que el nombre de usuario esté presente
            if (nombreUsuario == null || nombreUsuario.isEmpty()) {
                response.put("mensaje", "El nombre de usuario es obligatorio");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Llamar al servicio para bloquear al usuario
            String resultado = administradorService.bloquearUsuario(nombreUsuario);

            // Verificar el resultado
            if (resultado.equals("Usuario no encontrado.")) {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (resultado.equals("Usuario bloqueado exitosamente.")) {
                response.put("mensaje", resultado);
                return ResponseEntity.ok(response);
            } else if (resultado.equals("El usuario ya está bloqueado.")) {
                response.put("mensaje", resultado);
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar bloquear al usuario");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para obtener los datos completos de un usuario.
     * 
     * Este método consulta toda la información relacionada con un usuario,
     * incluyendo
     * sus datos personales, rol y validación de cédula profesional. La información
     * de la cédula se valida a través de una API externa.
     *
     * @param nombreUsuario Nombre del usuario a consultar
     * @return ResponseEntity con la información completa del usuario o mensaje de
     *         error
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Datos del usuario encontrados correctamente.
     *         - `404 Not Found`: Si no se encuentra el usuario.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la
     *         consulta.
     */
    @GetMapping("/getUsuario")
    public ResponseEntity<Map<String, Object>> getUsuario(@RequestParam String nombreUsuario) {
        try {
            Map<String, Object> resultado = administradorService.getUsuario(nombreUsuario);

            if (resultado.containsKey("error")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(resultado);
            }

            if (resultado.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(response);
            }

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al obtener los datos del usuario");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /**
     * Endpoint para obtener la lista de alumnos.
     * 
     * Este método consulta todos los usuarios que son alumnos en el sistema. Si
     * no se encuentran alumnos, se devuelve un mensaje indicando que no hay
     * contenido.
     * 
     * @return ResponseEntity con la lista de alumnos o mensaje de error
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Lista de alumnos obtenida correctamente.
     *         - `204 No Content`: Si no se encuentran alumnos en el sistema.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la
     *         consulta.
     */
    @GetMapping("/getUsuarios/alumnos")
    public ResponseEntity<List<UsuarioDocumento>> getUsuarios() {
        try {
            List<UsuarioDocumento> usuarios = administradorService.getAlumnos();
            if (usuarios.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content
            }
            return ResponseEntity.ok(usuarios); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // 500 Internal Server Error
        }
    }

    /**
     * Endpoint para obtener la lista de instructores.
     * 
     * Este método consulta todos los usuarios que son instructores en el sistema.
     * Si
     * no se encuentran instructores, se devuelve un mensaje indicando que no hay
     * contenido.
     * 
     * @return ResponseEntity con la lista de instructores o mensaje de error
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Lista de instructores obtenida correctamente.
     *         - `204 No Content`: Si no se encuentran instructores en el sistema.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la
     *         consulta.
     */
    @GetMapping("/getUsuarios/instructores")
    public ResponseEntity<List<UsuarioDocumento>> getInstructores() {
        try {
            List<UsuarioDocumento> usuarios = administradorService.getInstructores();
            if (usuarios.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content
            }
            return ResponseEntity.ok(usuarios); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // 500 Internal Server Error
        }
    }

    /**
     * Endpoint para buscar usuarios por coincidencia en nombre o apellidos.
     * 
     * Este método busca usuarios cuyo nombre, apellido paterno o apellido materno
     * coincida parcialmente con el término de búsqueda proporcionado. La búsqueda
     * no distingue entre mayúsculas y minúsculas.
     *
     * @param obj Objeto que cont
     * @return ResponseEntity con la lista de usuarios encontrados o mensaje de
     *         error
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Búsqueda realizada correctamente (con o sin resultados).
     *         - `400 Bad Request`: Si el término de búsqueda está vacío.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la
     *         búsqueda.
     */
    @PostMapping("/buscarUsuarioNombre")
    public ResponseEntity<Map<String, Object>> buscarUsuarioNombre(@RequestBody Map<String, Object> obj) {
        Map<String, Object> response = new HashMap<>();

        try {
            String busqueda = (String) obj.get("busqueda");

            // Validar que el término de búsqueda esté presente
            if (busqueda == null || busqueda.trim().isEmpty()) {
                response.put("mensaje", "El término de búsqueda es obligatorio");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Realizar la búsqueda
            List<Map<String, Object>> usuarios = administradorService.buscarUsuarioNombre(busqueda.trim());

            // Preparar la respuesta
            if (usuarios.isEmpty()) {
                response.put("mensaje", "No se encontraron usuarios que coincidan con la búsqueda");
                response.put("usuarios", usuarios);
            } else {
                response.put("mensaje", "Usuarios encontrados");
                response.put("usuarios", usuarios);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("mensaje", "Error al buscar usuarios");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Endpoint para obtener todas las solicitudes de usuarios que quieren ser instructores.
     * 
     * Este método retorna una lista de todas las solicitudes pendientes para ser instructor,
     * ordenadas desde la más antigua hasta la más reciente. Incluye información detallada
     * como nombre, correo, teléfono y estado de la solicitud.
     *
     * @return ResponseEntity con la lista de solicitudes o mensaje de error
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Lista de solicitudes recuperada correctamente (incluso si está vacía).
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la consulta.
     */
    @GetMapping("/solicitudes-instructor")
    public ResponseEntity<?> verSolicitudInstructor() {
        try {
            List<Map<String, Object>> solicitudes = administradorService.verSolicitudInstructor();
            Map<String, Object> response = new HashMap<>();
            
            if (solicitudes.isEmpty()) {
                response.put("mensaje", "No hay solicitudes de instructor");
                response.put("solicitudes", solicitudes); // Incluimos la lista vacía
            } else {
                response.put("mensaje", "Solicitudes recuperadas exitosamente");
                response.put("solicitudes", solicitudes);
            }
            
            return ResponseEntity.ok(response); // Siempre retornamos 200 OK
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al obtener las solicitudes de instructor");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    /**
     * Endpoint para aceptar una solicitud de instructor.
     * 
     * Este método permite al administrador aprobar una solicitud de instructor.
     * Al aprobarla, se cambia el estatus de la solicitud a "aprobada" y se crea
     * un nuevo usuario con rol de instructor en el sistema.
     *
     * @param payload Objeto que contiene el ID de la solicitud de instructor
     * @return ResponseEntity con el mensaje de éxito o error
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Solicitud aprobada correctamente.
     *         - `400 Bad Request`: Si falta el ID de la solicitud.
     *         - `404 Not Found`: Si no se encuentra la solicitud.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la aprobación.
     */
    @PostMapping("/aceptar-instructor")
    public ResponseEntity<?> aceptarInstructor(@RequestBody Map<String, Integer> payload) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Integer idSolicitudInstructor = payload.get("idSolicitudInstructor");
            if (idSolicitudInstructor == null) {
                response.put("mensaje", "El ID de solicitud de instructor es requerido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Llamar al servicio para aceptar al instructor
            String resultado = administradorService.aceptarInstructor(idSolicitudInstructor);
            
            // Verificar el resultado
            if (resultado.contains("no existe") || resultado.contains("no encontrada")) {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (resultado.contains("aceptado exitosamente") || resultado.contains("aprobado exitosamente")) {
                response.put("mensaje", resultado);
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar aceptar al instructor");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
