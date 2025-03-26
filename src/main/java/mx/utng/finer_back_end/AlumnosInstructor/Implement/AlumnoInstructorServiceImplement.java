package mx.utng.finer_back_end.AlumnosInstructor.Implement;

import mx.utng.finer_back_end.AlumnosInstructor.Dao.UsuarioInstructorDao;
import mx.utng.finer_back_end.AlumnosInstructor.Services.UsuarioInstructorService;
import mx.utng.finer_back_end.Documentos.UsuarioDocumento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlumnoInstructorServiceImplement implements UsuarioInstructorService {

    @Autowired
    private UsuarioInstructorDao usuarioinstructorDao;

    @Override
    public UsuarioDocumento obtenerDatosUsuario(Integer idUsuario) {
        // Utiliza el DAO para obtener los datos del usuario por su ID
        return usuarioinstructorDao.obtenerDatosUsuario(idUsuario);
    }
}
