package mx.utng.finer_back_end.Instructor.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.Instructor.Dao.EvaluacionDao;
import mx.utng.finer_back_end.Instructor.Dao.OpcionDao;
import mx.utng.finer_back_end.Instructor.Dao.PreguntaDao;
import mx.utng.finer_back_end.Instructor.Dao.UsuarioNotificacionEvaluacionDao;
import mx.utng.finer_back_end.Instructor.Documentos.Evaluacion;
import mx.utng.finer_back_end.Instructor.Documentos.EvaluacionInstructorDTO;
import mx.utng.finer_back_end.Instructor.Documentos.OpcionEvaluacionInstructorDTO;
import mx.utng.finer_back_end.Instructor.Documentos.PreguntaEvaluacionInstructorDTO;

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionDao evaluacionDao;

    @Autowired
    private PreguntaDao preguntaDao;

    @Autowired
    private OpcionDao opcionDao;

     @Autowired
    private UsuarioNotificacionEvaluacionDao usuarioNotificacionEvaluacionDao;

     @Autowired
    private JavaMailSender mailSender;

    /**
     * Crea una nueva evaluación utilizando el repositorio y envía correos a los estudiantes.
     *
     * @param evaluacionDTO objeto con los datos de la evaluación.
     * @return ID de la evaluación creada.
     */
    public Integer crearEvaluacion(EvaluacionInstructorDTO evaluacionDTO) {
        // Convertir el DTO a la entidad Evaluacion para guardarlo en la base de datos
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setIdCurso(evaluacionDTO.getIdCurso());
        evaluacion.setTituloEvaluacion(evaluacionDTO.getTituloEvaluacion());
        evaluacion.setFechaCreacion(new java.util.Date()); // Asignar la fecha actual
        evaluacionDao.save(evaluacion); // Guardar la evaluación

        // Obtener el ID de la evaluación recién creada
        Integer idEvaluacion = evaluacion.getIdEvaluacion();

 // Asignamos el ID de la evaluación a evaluacionDTO
 evaluacionDTO.setIdEvaluacion(evaluacion.getIdEvaluacion());   

        // Obtener los correos electrónicos de los estudiantes inscritos en el curso
        List<String> correosEstudiantes = usuarioNotificacionEvaluacionDao.obtenerCorreosEstudiantes(evaluacionDTO.getIdCurso());

        System.out.println("Número de correos de estudiantes obtenidos: " + correosEstudiantes.size());

        // Enviar correos a los estudiantes
        for (String correo : correosEstudiantes) {
            // Convertir el DTO a Evaluacion y luego enviamos el correo
            enviarCorreo(correo, evaluacion); // Pasamos el objeto Evaluacion
        }

        return idEvaluacion;
    }

    /**
     * Método para enviar el correo de notificación de evaluación a los estudiantes.
     *
     * @param correo destinatario del correo.
     * @param evaluacion la evaluación a notificar.
     */
    private void enviarCorreo(String correo, Evaluacion evaluacion) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(correo);
        message.setSubject("Nueva Evaluación: " + evaluacion.getTituloEvaluacion());
        message.setText("Estimado estudiante,\n\nSe ha creado una nueva evaluación titulada: " 
                        + evaluacion.getTituloEvaluacion() 
                        + "\n\nPor favor, revisa la plataforma para más detalles.");
        
        try {
            mailSender.send(message);
            System.out.println("Correo enviado a: " + correo);
        } catch (Exception e) {
            System.out.println("Error al enviar correo a: " + correo);
            e.printStackTrace();
        }
    }

    /**
     * Agrega preguntas y opciones a la evaluación recién creada.
     *
     * @param evaluacionDTO objeto con los datos de las preguntas y opciones.
     */
    public void agregarPreguntasYOpciones(EvaluacionInstructorDTO evaluacionDTO) {
        // Verificamos que la evaluación tiene un ID asignado
        if (evaluacionDTO.getIdEvaluacion() != null) {
            // Agregar preguntas y opciones para cada pregunta
            for (PreguntaEvaluacionInstructorDTO pregunta : evaluacionDTO.getPreguntas()) {
                // Agregar la pregunta con el ID de la evaluación
                Integer idPregunta = preguntaDao.agregarPregunta(evaluacionDTO.getIdEvaluacion(), pregunta.getTextoPregunta());
                pregunta.setIdPregunta(idPregunta);

                // Agregar opciones para cada pregunta
                for (OpcionEvaluacionInstructorDTO opcion : pregunta.getOpciones()) {
                    // Ahora utilizamos "verificar" en lugar de "correcta"
                    opcionDao.agregarOpcion(idPregunta, opcion.getTextoOpcion(), opcion.getVerificar());
                }
            }
        } else {
            throw new RuntimeException("El ID de la evaluación no es válido.");
        }
    }
}


