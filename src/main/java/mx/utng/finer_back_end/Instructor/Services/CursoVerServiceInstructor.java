package mx.utng.finer_back_end.Instructor.Services;

import java.util.List;

import mx.utng.finer_back_end.Instructor.Documentos.CursoVerDTO;

public interface CursoVerServiceInstructor {
     List<CursoVerDTO> verCursos(Integer idInstructor);

}
