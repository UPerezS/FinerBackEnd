package mx.utng.finer_back_end.Instructor.Services;

import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.activation.FileDataSource;
import jakarta.mail.internet.MimeMessage;
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
    try {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setFrom("finner.oficial.2025@gmail.com");
        helper.setTo(correo);
        helper.setSubject("Nueva Evaluación: " + evaluacion.getTituloEvaluacion());

    URL logoUrl = getClass().getClassLoader().getResource       ("finer_logo.png");
        String logoPath = "";
        if (logoUrl != null) {
            // Decodificar la ruta para eliminar caracteres codificados (como %20)
            logoPath = URLDecoder.decode(logoUrl.getPath(), "UTF-8");
            System.out.println("Ruta decodificada del logo: " + logoPath);
        } else {
            System.err.println("No se encontró el recurso finer_logo.png, se enviará sin logo.");
        }

        // Construir el contenido HTML del mensaje
        String cuerpoMensaje = "<html>" +
            "<body style=\"font-family: Arial, sans-serif; background-color: #f5f5f5; color: #333; padding: 20px;\">" +
            "<div style=\"background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">" +
            "<h2 style=\"color: #2d6a4f;\">Nueva Evaluación: " + evaluacion.getTituloEvaluacion() + "</h2>" +
            "<p>Estimado estudiante,</p>" +
            "<p>Se ha creado una nueva evaluación titulada: <strong>" + evaluacion.getTituloEvaluacion() + "</strong>.</p>" +
            "<p>Si quieres saber mas detalles, revisa la plataforma.</p>" +
            "<p>Saludos cordiales,</p>" +
            "<p>El equipo de Finer</p>" +
            (logoUrl != null ? "<p style=\"text-align:center;\"><img src=\"cid:finerLogo\" alt=\"Finer Logo\" style=\"max-width: 200px;\" /></p>" : "") +
            "</div>" +
            "</body>" +
            "</html>";

        helper.setText(cuerpoMensaje, true);

        if (logoUrl != null) {
            FileDataSource dataSource = new FileDataSource(logoPath);
            helper.addInline("finerLogo", dataSource);
        }

        mailSender.send(mensaje);
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


