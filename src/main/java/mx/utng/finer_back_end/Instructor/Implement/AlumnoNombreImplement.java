package mx.utng.finer_back_end.Instructor.Implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import mx.utng.finer_back_end.Instructor.Dao.AlumnoNombreDao;
import mx.utng.finer_back_end.Instructor.Documentos.AlumnoDetalleNombreDTO;
import mx.utng.finer_back_end.Instructor.Services.AlumnoNombreService;
@Service
public class AlumnoNombreImplement implements AlumnoNombreService {
    @Autowired
    private AlumnoNombreDao alumnoNombreDao;

    @Override
    public List<AlumnoDetalleNombreDTO> filtrarAlumnoNombre(String nombre, String orden) { 
        // Llamada al DAO pasando ambos parámetros (nombre y orden)
        List<Object[]> resultados = alumnoNombreDao.filtrarAlumnosNombre(nombre, orden);
        List<AlumnoDetalleNombreDTO> detalles = new ArrayList<>();

        // Iterar sobre los resultados y mapearlos a DTO
        for(Object[] row: resultados){
            AlumnoDetalleNombreDTO dto = new AlumnoDetalleNombreDTO(
                (String) row[0], // nombre
                (String) row[1], // apellidoPaterno
                (String) row[2], // apellidoMaterno
                (String) row[3]  // correo
            );
            detalles.add(dto);
        }
        return detalles;
    }
}