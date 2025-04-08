package mx.utng.finer_back_end.Alumnos.Dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.CursoDocumento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface CursoAlumnoDao extends JpaRepository<CursoDocumento, Long> {
        @Query(value = "SELECT * FROM ver_detalles_curso(:p_titulo_curso)", nativeQuery = true)
        List<Object[]> verCursoDetalles(@Param("p_titulo_curso") String p_titulo_curso);

        @Query(value = "SELECT validar_reinscripcion(:p_id_curso, :p_id_usuario)", nativeQuery = true)
        Boolean validarReinscripcionAlumno(@Param("p_id_curso") Integer p_id_curso,
                        @Param("p_id_usuario") Integer p_id_usuario);

        @Query(value = "SELECT inscribir_alumno(:p_id_curso, :p_id_usuario)", nativeQuery = true)
        Boolean inscribirseCursoAlumno(@Param("p_id_curso") Integer p_id_curso,
                        @Param("p_id_usuario") Integer p_id_usuario);

        @Query(value = "SELECT * FROM calcular_calificacion(:id_inscripcion_param)", nativeQuery = true)
        List<Object[]> verPuntuacion(@Param("id_inscripcion_param") Integer id_inscripcion_param);

        @Query(value = "SELECT cancelar_inscripcion(:p_id_inscripcion)", nativeQuery = true)
        String bajaCursoAlumno(@Param("p_id_inscripcion") Integer p_id_inscripcion);

        @Query(value = "SELECT * FROM obtener_datos_completos_certificado(:id_inscripcion)", nativeQuery = true)
        List<Object[]> obtenerDetallesCertificado(@Param("id_inscripcion") Integer id_inscripcion);


        @Query(value = "SELECT * FROM obtener_temas_curso(:p_id_curso)", nativeQuery = true)
        List<Object[]> getTemas(@Param("p_id_curso") Integer p_id_curso);

        @Query(value = "SELECT * FROM obtener_cursos_alumno(:p_id_usuario)", nativeQuery = true)
        List<Object[]> obtenerCursosAlumno(@Param("p_id_usuario") Integer idAlumno);


    @Query(value = "SELECT EXISTS(SELECT 1 FROM usuario WHERE id_usuario = :id_usuario AND id_rol = 3)", nativeQuery = true)
    Boolean esAlumno(@Param("id_usuario") Integer idUsuario);

    @Query(value = "SELECT * FROM obtener_cursos_finalizados_por_alumno(:p_id_usuario_alumno)", nativeQuery = true)
    List<Object[]> obtenerCursosFinalizadosPorAlumno(@Param("p_id_usuario_alumno") Integer p_id_usuario_alumno);
    

}
