package mx.utng.finer_back_end.Alumnos.Controller;

import mx.utng.finer_back_end.Alumnos.Documentos.EvaluacionAlumnoDTO;
import mx.utng.finer_back_end.Alumnos.Services.EvaluacionAlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/evaluacion/alumno")
public class EvaluacionAlumnoController {

    @Autowired
    private EvaluacionAlumnoService evaluacionAlumnoService;

    @GetMapping("verEvaluacion/{id}")
    public List<EvaluacionAlumnoDTO> obtenerEvaluacion(@PathVariable Integer id) {
        return evaluacionAlumnoService.obtenerEvaluacion(id);
    }
}
