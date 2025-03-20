package mx.utng.finer_back_end.Instructor.Services;

import mx.utng.finer_back_end.Instructor.Documentos.VerAlumnosDTOInstructor;
import mx.utng.finer_back_end.Instructor.Documentos.VerAlumnosRequestDTO;

public interface VerAlumnosServiceInstructor {
    VerAlumnosDTOInstructor verAlumnos(VerAlumnosRequestDTO requestDTO);

}
