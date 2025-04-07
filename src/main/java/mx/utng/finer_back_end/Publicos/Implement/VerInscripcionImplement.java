package mx.utng.finer_back_end.Publicos.Implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mx.utng.finer_back_end.Publicos.Dao.verIdInscripcionDao;
import mx.utng.finer_back_end.Publicos.Documentos.verIdInscripcionDTO;

import mx.utng.finer_back_end.Publicos.Services.verInscripcionIdService;

@Service
public class VerInscripcionImplement implements verInscripcionIdService {
    @Autowired
    private verIdInscripcionDao verIdInscripcionDao;
    
    @Override
    @Transactional
    public List<verIdInscripcionDTO> obtenerIdInscripcion(Integer idUsuario, Integer idCurso) {
        Integer idInscripcion = verIdInscripcionDao.obtenerIdInscripcion(idUsuario, idCurso);
        List<verIdInscripcionDTO> detalle = new ArrayList<>();
    
        if (idInscripcion != null) {
            detalle.add(new verIdInscripcionDTO(idInscripcion));
        }
    
        return detalle;
    }
}

