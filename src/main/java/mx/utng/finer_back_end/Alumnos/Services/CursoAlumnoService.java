package mx.utng.finer_back_end.Alumnos.Services;

import java.util.List;

import mx.utng.finer_back_end.Alumnos.Documentos.CertificadoDetalleDTO;
import mx.utng.finer_back_end.Alumnos.Documentos.CursoDetalleAlumnoDTO;
import mx.utng.finer_back_end.Alumnos.Documentos.PuntuacionAlumnoDTO;
import mx.utng.finer_back_end.Documentos.TemaDocumento;

public interface CursoAlumnoService {
    /*
     * Metodo getCurso(idCurso)
     * idCurso Int
     * Return: Obj Curso
     */
    List<CursoDetalleAlumnoDTO> getCurso(Integer idCurso);

    Boolean inscribirseCurso(Integer idUsuario, Integer idCurso);

    List<PuntuacionAlumnoDTO> verPuntuacion(Integer idInscripcion);

    String bajaCursoAlumno(Integer idInscripcion);

    CertificadoDetalleDTO obtenerDetallesCertificado(Integer idInscripcion);

    List<TemaDocumento> getTemas(Integer idCurso);
}
