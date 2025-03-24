package mx.utng.finer_back_end.Instructor.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.Instructor.Dao.CursoEliminarDaoInstructor;
import mx.utng.finer_back_end.Instructor.Services.CursoEliminarServiceInstructor;

@Service
public class CursoEliminarImplementInstructor implements CursoEliminarServiceInstructor {

    @Autowired
    private CursoEliminarDaoInstructor cursoEliminarDao;

    @Override
    public String eliminarCurso(Integer idUsuario, Integer idCurso) {
        // Llamamos al método de DAO que ejecutará la función en la base de datos.
        return cursoEliminarDao.eliminarCurso(idUsuario, idCurso);
    }
}
