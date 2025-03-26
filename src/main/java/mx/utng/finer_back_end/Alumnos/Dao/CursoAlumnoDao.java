package mx.utng.finer_back_end.Alumnos.Dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.CursoDocumento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface CursoAlumnoDao extends JpaRepository<CursoDocumento, Long> {
        @Query(value = "SELECT * FROM obtener_detalles_curso_alumno(:p_id_curso)", nativeQuery = true)
        List<Object[]> verCursoDetalles(@Param("p_id_curso") Integer p_id_curso);

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

        @Query(value = "SELECT " +
                        "ins.id_inscripcion, " +
                        "CONCAT(u.nombre, ' ', u.apellido_paterno, ' ', u.apellido_materno) AS NombreCompletoAlumno, " +
                        "cu.titulo_curso, " +
                        "ca.nombre_categoria, " +
                        "(SELECT CONCAT(nombre, ' ', apellido_paterno, ' ', apellido_materno) " +
                        "FROM usuario usin WHERE cu.id_usuario_instructor = usin.id_usuario LIMIT 1) AS nombreInstructor, "
                        +
                        "ins.matricula, " +
                        "ins.fecha_inscripcion, " +
                        "CURRENT_DATE AS fechaGeneracion " +
                        "FROM categoria ca " +
                        "INNER JOIN curso cu ON ca.id_categoria = cu.id_categoria " +
                        "INNER JOIN inscripcion ins ON ins.id_curso = cu.id_curso " +
                        "INNER JOIN usuario u ON ins.id_usuario_alumno = u.id_usuario " +
                        "WHERE ins.estatus = 'finalizado' AND ins.id_inscripcion = :id_inscripcion " +
                        "LIMIT 1", nativeQuery = true)
        List<Object[]> obtenerDetallesCertificado(@Param("id_inscripcion") Integer id_inscripcion);

        @Query(value = "SELECT * FROM obtener_temas_curso(:p_id_curso)", nativeQuery = true)
        List<Object[]> getTemas(@Param("p_id_curso") Integer p_id_curso);

}
