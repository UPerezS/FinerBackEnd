package mx.utng.finer_back_end.Instructor.Services;

import java.util.List;

import mx.utng.finer_back_end.Instructor.Documentos.AlumnoDetalleNombreDTO;

public interface AlumnoNombreService {
 /**
     * Método para filtrar los alumnos por nombre y orden.
     * @param nombre El nombre a buscar.
     * @param orden El tipo de orden (ascendente/descendente).
     * @return Lista de alumnos que coinciden con el nombre filtrado, ordenados según el parámetro de orden.
     */
    List<AlumnoDetalleNombreDTO> filtrarAlumnoNombre(String nombre, String orden);
}