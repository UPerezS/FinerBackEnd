package mx.utng.finer_back_end.Instructor.Implement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import mx.utng.finer_back_end.Documentos.SolicitudTemaDocumento;
import mx.utng.finer_back_end.Instructor.Dao.SolicitudCursoDaoInstructor;
import mx.utng.finer_back_end.Instructor.Documentos.CursoSolicitadoDTOInstructor;
import mx.utng.finer_back_end.Instructor.Documentos.SolicitudCursoRequest;
import mx.utng.finer_back_end.Instructor.Services.SolicitudCursoServiceInstructor;

@Service
public class SolicitudCursoServiceImplementInstructor implements SolicitudCursoServiceInstructor {

    @Autowired
    private SolicitudCursoDaoInstructor solicitudCursoDao;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<CursoSolicitadoDTOInstructor> verCursosSolicitados(String estatus, Integer idInstructor) {
        List<Object[]> resultados = solicitudCursoDao.verCursosSolicitados(estatus, idInstructor);
        List<CursoSolicitadoDTOInstructor> cursosSolicitados = new ArrayList<>();

        for (Object[] row : resultados) {
            CursoSolicitadoDTOInstructor dto = new CursoSolicitadoDTOInstructor(
                    (String) row[0], // titulo_curso_solicitado
                    (String) row[1], // descripcion
                    (String) row[2], // nombre_instructor
                    (String) row[3], // nombre_categoria
                    (String) row[4] // estatus
            );
            cursosSolicitados.add(dto);
        }
        return cursosSolicitados;
    }

    @Override
    public Map<String, Object> editarSolicitudCurso(SolicitudCursoRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Convertir los temas a JSON para pasarlos correctamente
            String temasJson = objectMapper.writeValueAsString(request.getTemas());

            // Llamar al método del DAO con los parámetros correctos
            boolean success = solicitudCursoDao.editarSolicitudCurso(
                    request.getIdSolicitudCurso(),
                    request.getIdUsuarioInstructor(),
                    request.getTituloCursoSolicitado(),
                    request.getDescripcion(),
                    request.getIdCategoria(),
                    request.getIdCurso(),
                    request.getImagen(),
                    temasJson); // Pasar los temas como String (JSON)

            if (success) {
                response.put("mensaje", "Edición realizada con éxito.");
                response.put("estatus", "success");
            } else {
                response.put("mensaje", "Error al editar la solicitud.");
                response.put("estatus", "error");
            }
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud: " + e.getMessage());
            response.put("estatus", "error");
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Map<String, Object> enviarARevision(Integer idSolicitudCurso) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = solicitudCursoDao.enviarARevision(idSolicitudCurso);
            if (success) {
                response.put("mensaje", "Solicitud enviada a revisión.");
                response.put("estatus", "success");
            } else {
                response.put("mensaje", "Error al enviar la solicitud a revisión.");
                response.put("estatus", "error");
            }
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud: " + e.getMessage());
            response.put("estatus", "error");
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public  List<SolicitudTemaDocumento> verSolicitudTemaByIDCurso( Integer idSolicitudCurso ){
        List<Object[]> resultados = solicitudCursoDao.verTemasPorCursoSolicitado(idSolicitudCurso);
        List<SolicitudTemaDocumento> cursosSolicitados = new ArrayList<>();

        for (Object[] row : resultados) {
            SolicitudTemaDocumento dto = new SolicitudTemaDocumento(
                    (Integer) row[0], 
                    (Integer) row[1],
                    (String) row[2],
                    (String) row[3]
            );
            cursosSolicitados.add(dto);
        }
        return cursosSolicitados;
    }

}
