package mx.utng.finer_back_end.Instructor.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.Instructor.Dao.CursoSeleccionadoDaoInstructor;
import mx.utng.finer_back_end.Instructor.Documentos.CursoSeleccionadoDTO;
import mx.utng.finer_back_end.Instructor.Services.CursoSeleccionadoServiceInstructor;


@Service
public class CursoSeleccionadoImplementInstructor implements CursoSeleccionadoServiceInstructor{

    @Autowired
    private CursoSeleccionadoDaoInstructor cursoSeleccionadoDao;

    @Override
    public CursoSeleccionadoDTO seleccionarCurso(Integer idCurso) {
        List<Object[]> resultados = cursoSeleccionadoDao.seleccionarCurso(idCurso);

        // Suponemos que la función siempre retorna una fila
        if (resultados.isEmpty()) {
            return new CursoSeleccionadoDTO(500, "Error en la consulta", null);
        }

        Object[] row = resultados.get(0);
        // Se asume el siguiente orden:
        // 0: status_code (Integer)
        // 1: message (String)
        // 2: data (String) -> JSON en formato texto
        Integer statusCode = (Integer) row[0];
        String message = (String) row[1];
        String data = row[2] != null ? row[2].toString() : null;

        return new CursoSeleccionadoDTO(statusCode, message, data);
    }
}
