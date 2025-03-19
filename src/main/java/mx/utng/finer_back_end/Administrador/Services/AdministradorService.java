package mx.utng.finer_back_end.Administrador.Services;

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
     * @param correoInstructor Correo del instructor al que se notificará
     * @param motivoRechazo Motivo por el cual se rechaza el curso
     * @param tituloCurso Título del curso que se está rechazando
     * @return Mensaje indicando el resultado de la operación
     */
    String rechazarCurso(Long idSolicitudCurso, String correoInstructor, String motivoRechazo, String tituloCurso);
    
    // Add this method to your AdministradorService interface
    String crearCategoria(Integer idUsuarioInstructor, Integer idUsuarioAdmin, String nombreCategoria, String descripcion);
}