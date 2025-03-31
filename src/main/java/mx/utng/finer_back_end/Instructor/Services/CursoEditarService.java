package mx.utng.finer_back_end.Instructor.Services;

import mx.utng.finer_back_end.Instructor.Dao.CursoEditarDao;
import mx.utng.finer_back_end.Instructor.Documentos.CursoEditarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CursoEditarService {

    @Autowired
    private CursoEditarDao cursoEditarDao;

    /**
     * Edita los detalles de un curso.
     *
     * @param cursoEditarDTO objeto con los datos del curso editado.
     * @return Mensaje de éxito o error.
     */
    public String editarCurso(CursoEditarDTO cursoEditarDTO) {
        // Llamar a la función de la base de datos para modificar el curso
        return cursoEditarDao.modificarCurso(cursoEditarDTO.getIdCurso(), cursoEditarDTO.getIdInstructor(), cursoEditarDTO.getDescripcion(), cursoEditarDTO.getIdCategoria(), cursoEditarDTO.getImagen());
    }
}
