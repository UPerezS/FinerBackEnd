package mx.utng.finer_back_end.Publicos.Implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.Publicos.Dao.VerCursosDao;
import mx.utng.finer_back_end.Publicos.Documentos.CursoDetalleDTO;
import mx.utng.finer_back_end.Publicos.Services.VerCursoService;
@Service
public class VerCursoImplement implements VerCursoService{

    @Autowired
    private VerCursosDao cursoDao;

    @Override
    public List<CursoDetalleDTO> verCursos() {
        List<Object[]> resultados = cursoDao.verCursos();
        List<CursoDetalleDTO> detalles = new ArrayList<>();

        for (Object[] row : resultados) {
            CursoDetalleDTO dto = new CursoDetalleDTO(
                (String) row[0], // titulo_curso
                (String) row[1], // descripcion
                (String) row[2], // nombre del instructor
                (String) row[3], // apellido paterno
                (String) row[4], // apellido materno
                (String) row[5]  // nombre de la categoría
            );
            detalles.add(dto);
        }
        return detalles;
    }

}
