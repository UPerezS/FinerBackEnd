package mx.utng.finer_back_end.Instructor.Services;

import java.util.List;
import java.util.Map;

import mx.utng.finer_back_end.Documentos.SolicitudTemaDocumento;
import mx.utng.finer_back_end.Instructor.Documentos.CursoSolicitadoDTOInstructor;
import mx.utng.finer_back_end.Instructor.Documentos.SolicitudCursoRequest;

public interface SolicitudCursoServiceInstructor {
    List<CursoSolicitadoDTOInstructor> verCursosSolicitados(String estatus, Integer idInstructor);
    List<SolicitudTemaDocumento> verSolicitudTemaByIDCurso( Integer idSolicitudCurso);

    Map<String, Object> enviarARevision(Integer id_solicitud_curso );
    Map<String, Object> editarSolicitudCurso(SolicitudCursoRequest request);
}
