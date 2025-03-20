package mx.utng.finer_back_end.Instructor.Implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.Instructor.Dao.AlumnoMatriculaDao;
import mx.utng.finer_back_end.Instructor.Documentos.AlumnoDetalleMatriculaDTO;
import mx.utng.finer_back_end.Instructor.Services.AlumnoMatriculaService;

@Service
public class AlumnoMatriculaImplement implements AlumnoMatriculaService {
    @Autowired
    private AlumnoMatriculaDao alumnoMatriculaDao;

    @Override
    public List<AlumnoDetalleMatriculaDTO> filtrarAlumnosMatricula(String matricula, String orden){ 

        List<Object[]> resultados = alumnoMatriculaDao.filtrarAlumnosMatricula(matricula, orden);
        List<AlumnoDetalleMatriculaDTO> detalles = new ArrayList<>();

        for(Object[] row:resultados){
            AlumnoDetalleMatriculaDTO dto = new AlumnoDetalleMatriculaDTO(
                (String) row[0], // nombre
                (String) row[1], // apellidoPaterno
                (String) row[2], // apellidoMaterno
                (String) row[3],  // correo
                (String)row[4] //matricula

            );
            detalles.add(dto);

        }
        return detalles;



    }

}
