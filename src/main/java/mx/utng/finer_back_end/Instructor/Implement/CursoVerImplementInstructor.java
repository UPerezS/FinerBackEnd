package mx.utng.finer_back_end.Instructor.Implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.Instructor.Dao.CursoVerDaoInstructor;
import mx.utng.finer_back_end.Instructor.Documentos.CursoVerDTO;
import mx.utng.finer_back_end.Instructor.Services.CursoVerServiceInstructor;

@Service
public class CursoVerImplementInstructor implements CursoVerServiceInstructor{
    @Autowired
    private CursoVerDaoInstructor cursoVerDao;

     @Override
    public List<CursoVerDTO> verCursos(Integer idInstructor) {
        List<Object[]> resultados = cursoVerDao.verCursos(idInstructor);
        List<CursoVerDTO> cursos = new ArrayList<>();

        for (Object[] row : resultados) {
            // Se asume el siguiente orden:
            // 0: id_curso (Integer)
            // 1: titulo (String)
            // 2: descripcion (String)
            // 3: estatus (String)
            // 4: origen (String)
            CursoVerDTO dto = new CursoVerDTO(
                    (Integer) row[0],
                    (String) row[1],
                    (String) row[2],
                    (String) row[3],
                    (String) row[4]
            );
            cursos.add(dto);
        }
        return cursos;
    }
}
