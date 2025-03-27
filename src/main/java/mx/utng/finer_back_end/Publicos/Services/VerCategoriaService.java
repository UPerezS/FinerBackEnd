package mx.utng.finer_back_end.Publicos.Services;

import java.util.List;

import mx.utng.finer_back_end.Publicos.Documentos.VerCategoriasDTO;

public interface VerCategoriaService {

    List<VerCategoriasDTO> obtenerCategorias();
}
