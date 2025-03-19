package mx.utng.finer_back_end.Instructor.Services;

import java.util.List;

import mx.utng.finer_back_end.Instructor.Documentos.CursoDetalleCategoriaDTO;

public interface CursoCategoriaService {
    List<CursoDetalleCategoriaDTO> filtrarCursoCategoria(String busqueda);
}
