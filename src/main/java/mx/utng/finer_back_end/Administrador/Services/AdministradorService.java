package mx.utng.finer_back_end.Administrador.Services;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import mx.utng.finer_back_end.Documentos.UsuarioDocumento;

public interface AdministradorService {
    
    /**
     * Elimina un alumno de un curso específico.
     * 
     * @param matricula Matrícula del alumno a eliminar
     * @param idCurso ID del curso del que se eliminará al alumno
     * @return Mensaje indicando el resultado de la operación
     */
    String eliminarAlumnoCurso(String matricula, Integer idCurso);
    
    /**
     * Rechaza una solicitud de curso y envía un correo al instructor.
     * 
     * @param idSolicitudCurso ID de la solicitud de curso a rechazar
     * @param motivoRechazo Motivo por el cual se rechaza el curso
     * @param tituloCurso Título del curso que se está rechazando
     * @return Mensaje indicando el resultado de la operación
     */
    String rechazarCurso(Long idSolicitudCurso, String motivoRechazo, String tituloCurso);
    
    /**
     * Aprueba una solicitud de curso.
     * 
     * @param idSolicitudCurso ID de la solicitud de curso a aprobar
     * @return Mensaje con el resultado de la operación
     */
    String aprobarCurso(Integer idSolicitudCurso);    
    
    /**
     * Acepta una solicitud de instructor y crea un nuevo usuario con rol de instructor
     * 
     * @param idSolicitudInstructor ID de la solicitud de instructor a aceptar
     * @return Mensaje con el resultado de la operación
     */
    String aceptarInstructor(Integer idSolicitudInstructor);
    
    /**
     * Modifica la descripción de una categoría existente.
     * 
     * @param idCategoria ID de la categoría a modificar
     * @param nuevaDescripcion Nueva descripción para la categoría
     * @return Mensaje indicando el resultado de la operación
     */
    String modificarCategoriaDescripcion(Integer idCategoria, String nuevaDescripcion);
    
    /**
     * Crea una nueva categoría en el sistema.
     * 
     * @param idUsuarioInstructor ID del instructor que solicitó la categoría
     * @param nombreCategoria Nombre de la nueva categoría
     * @param descripcion Descripción de la categoría
     * @return Mensaje indicando el resultado de la operación
     */
    String crearCategoria(Integer idUsuarioInstructor, String nombreCategoria, String descripcion);
    
    /**
     * Elimina una categoría existente.
     * 
     * @param idCategoria ID de la categoría a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    Boolean eliminarCategoria(Integer idCategoria);

    /**
     * Bloquea a un usuario en el sistema cambiando su rol a 'bloqueado'.
     * 
     * @param nombreUsuario Nombre del usuario a bloquear
     * @return Mensaje indicando el resultado de la operación
     */
    String bloquearUsuario(String nombreUsuario);

    /**
     * Obtiene los datos completos de un usuario incluyendo la validación de su cédula profesional.
     * 
     * @param nombreUsuario Nombre de usuario a consultar
     * @return Map con la información completa del usuario y el estado de su cédula profesional
     */
    Map<String, Object> getUsuario(String nombreUsuario);
    
    /**
     * Busca usuarios por coincidencia en nombre, apellido paterno o apellido materno.
     * 
     * @param busqueda Término de búsqueda para filtrar usuarios
     * @return Lista de usuarios que coinciden con el criterio de búsqueda
     */
    List<Map<String, Object>> buscarUsuarioNombre(String busqueda);

    /**
     * Obtiene la lista de todos los alumnos registrados en el sistema.
     * 
     * @return Lista de documentos de usuario correspondientes a alumnos
     */
    List<UsuarioDocumento> getAlumnos(); 

    /**
     * Obtiene la lista de todos los instructores registrados en el sistema.
     * 
     * @return Lista de documentos de usuario correspondientes a instructores
     */
    List<UsuarioDocumento> getInstructores(); 

    /**
     * Obtiene todas las solicitudes de usuarios que quieren ser instructores.
     * 
     * @return Lista de solicitudes ordenadas de la más antigua a la más reciente
     */
    List<Map<String, Object>> verSolicitudInstructor();
    
    /**
     * Obtiene todas las solicitudes de categoría.
     * 
     * @return Lista de solicitudes de categoría
     */
    List<Map<String, Object>> verSolicitudesCategoria();
    
    /**
     * Aprueba una solicitud de categoría y crea la categoría en el sistema.
     * 
     * @param idSolicitudCategoria ID de la solicitud de categoría a aprobar
     * @return Mensaje con el resultado de la operación
     */
    String aprobarCategoria(Integer idSolicitudCategoria);
}