package mx.utng.finer_back_end.Instructor.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import mx.utng.finer_back_end.Instructor.Dao.VerAlumnosDaoInstructor;
import mx.utng.finer_back_end.Instructor.Documentos.VerAlumnosDTOInstructor;
import mx.utng.finer_back_end.Instructor.Documentos.VerAlumnosRequestDTO;
import mx.utng.finer_back_end.Instructor.Services.VerAlumnosServiceInstructor;

@Service
public class VerAlumnosImplementInstructor implements VerAlumnosServiceInstructor{

    @Autowired
    private VerAlumnosDaoInstructor verAlumnosDao;

    // Utilidad para convertir el objeto a JSON
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public VerAlumnosDTOInstructor verAlumnos(VerAlumnosRequestDTO requestDTO) {
    try {
        String jsonRequest = objectMapper.writeValueAsString(requestDTO);
        List<Object[]> resultados = verAlumnosDao.verAlumnos(jsonRequest);


        if (resultados.isEmpty()) {
            return new VerAlumnosDTOInstructor(500, "Error en la consulta", null);
        }

        Object[] row = resultados.get(0);
        Integer statusCode = (Integer) row[0];
        String message = (String) row[1];
        String data = row[2] != null ? row[2].toString() : null;

        return new VerAlumnosDTOInstructor(statusCode, message, data);

    } catch (JsonProcessingException e) {
        return new VerAlumnosDTOInstructor(500, "Error al procesar el JSON: " + e.getMessage(), null);
    } catch (Exception e) {
        e.printStackTrace(); // Esto permitirá ver detalles más claros en el terminal
        return new VerAlumnosDTOInstructor(500, "Error al obtener alumnos: " + e.getMessage(), null);
    }
}

}
