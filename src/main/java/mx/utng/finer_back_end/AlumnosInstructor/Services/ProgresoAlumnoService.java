package mx.utng.finer_back_end.AlumnosInstructor.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.AlumnosInstructor.Documentos.CursoDetalleProgresoDTO;
@Service
public interface ProgresoAlumnoService {

    List<CursoDetalleProgresoDTO> verProgresoAlumno(Integer idEstudiante, Integer idCurso);
  
}
