

package mx.utng.finer_back_end.Instructor.Services;
import java.util.List;

import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.Instructor.Documentos.AlumnoDetalleMatriculaDTO;
@Service
public interface AlumnoMatriculaService {

    /**
     * Método para filtrar los alumnos por nombre y orden.
     * @param matricula El nombre a buscar.
     * @param orden El tipo de orden (ascendente/descendente).
     * @return Lista de alumnos que coinciden con el nombre filtrado, ordenados según el parámetro de orden.
     */
    List<AlumnoDetalleMatriculaDTO> filtrarAlumnosMatricula(String matricula, String orden);
}

