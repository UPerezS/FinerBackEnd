package mx.utng.finer_back_end.Alumnos.Implement;

import mx.utng.finer_back_end.Alumnos.Dao.EvaluacionAlumnoDao;
import mx.utng.finer_back_end.Alumnos.Documentos.EvaluacionAlumnoDTO;
import mx.utng.finer_back_end.Alumnos.Services.EvaluacionAlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluacionAlumnoImplement implements EvaluacionAlumnoService {

    @Autowired
    private EvaluacionAlumnoDao evaluacionAlumnoDao;

    @Override
    public List<EvaluacionAlumnoDTO> obtenerEvaluacion(Integer idEvaluacion) {
        List<Map<String, Object>> tablaEvaluaciones =  evaluacionAlumnoDao.obtenerEvaluacion(idEvaluacion);

        Map<Integer, EvaluacionAlumnoDTO> preguntasExamenMap = new LinkedHashMap<>();

        for(Map<String, Object> tablaEvaluacion :tablaEvaluaciones ){
            Integer id_pregunta = (Integer) tablaEvaluacion.get("id_pregunta");

            if(!preguntasExamenMap.containsKey(id_pregunta)){
                EvaluacionAlumnoDTO evaluacionAlumnoDTO = new EvaluacionAlumnoDTO();
                evaluacionAlumnoDTO.setTexto_pregunta((String) tablaEvaluacion.get("texto_pregunta"));
                evaluacionAlumnoDTO.setId_pregunta(id_pregunta);
                evaluacionAlumnoDTO.setOpciones(new ArrayList<>());
                preguntasExamenMap.put(id_pregunta, evaluacionAlumnoDTO);
            }

            EvaluacionAlumnoDTO.OpcionDTO opcionDTO = new EvaluacionAlumnoDTO.OpcionDTO();
            opcionDTO.setId_opcion((Integer) tablaEvaluacion.get("id_opcion"));
            opcionDTO.setTexto_opcion((String) tablaEvaluacion.get("texto_opcion"));

            preguntasExamenMap.get(id_pregunta).getOpciones().add(opcionDTO);


        }
        return new ArrayList<>(preguntasExamenMap.values());
    }

    @Override
    public Number guardarRespuestas(Integer idEstudiante, Integer idCurso, Integer[] idPreguntas, Integer[] idOpciones) {
        return evaluacionAlumnoDao.guardarRespuestas(idEstudiante, idCurso, idPreguntas, idOpciones);
    }
}
