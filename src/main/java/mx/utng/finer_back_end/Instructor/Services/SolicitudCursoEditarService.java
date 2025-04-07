package mx.utng.finer_back_end.Instructor.Services;

import mx.utng.finer_back_end.Instructor.DTO.SolicitudCursoEditarDTO;

public interface SolicitudCursoEditarService {
    /**
     * Edita una solicitud de curso existente.
     * 
     * @param solicitudDTO Datos actualizados de la solicitud
     * @return true si la edición fue exitosa, false en caso contrario
     */
    Boolean editarSolicitudCurso(SolicitudCursoEditarDTO solicitudDTO);
}