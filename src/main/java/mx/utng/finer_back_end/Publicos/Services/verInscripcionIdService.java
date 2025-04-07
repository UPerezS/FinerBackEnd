package mx.utng.finer_back_end.Publicos.Services;

import java.util.List;


import mx.utng.finer_back_end.Publicos.Documentos.verIdInscripcionDTO;

public interface verInscripcionIdService {
    List<verIdInscripcionDTO> obtenerIdInscripcion(Integer idUsuario, Integer idCurso);
}
