package mx.utng.finer_back_end.Instructor.Services;

import java.util.List;

import mx.utng.finer_back_end.Instructor.Documentos.CursoDetalleNombreDTO;

public interface CursoNombreService {
    List<CursoDetalleNombreDTO> filtrarCursoNombre(String busqueda);
}
