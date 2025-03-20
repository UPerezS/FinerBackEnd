package mx.utng.finer_back_end.AlumnosInstructor.Implement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mx.utng.finer_back_end.AlumnosInstructor.Dao.CursoProgresoDao;
import mx.utng.finer_back_end.AlumnosInstructor.Documentos.CursoDetalleProgresoDTO;
import mx.utng.finer_back_end.AlumnosInstructor.Services.ProgresoAlumnoService;

@Service
public class CursoProgresoImplement implements ProgresoAlumnoService {
    @Autowired
    private CursoProgresoDao cursoProgresoDao;

    @Override
    @Transactional
    public List<CursoDetalleProgresoDTO> verProgresoAlumno(Integer idEstudiante, Integer idCurso) {
        Float porcentaje = cursoProgresoDao.verProgresoAlumno(idEstudiante, idCurso);

        if (porcentaje == null) {
            return Collections.emptyList();
        }

        CursoDetalleProgresoDTO progresoDTO = new CursoDetalleProgresoDTO();
        progresoDTO.setVPorcentaje(porcentaje);

        return List.of(progresoDTO);
    }
}