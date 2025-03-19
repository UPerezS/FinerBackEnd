package mx.utng.finer_back_end.Publicos.Controller;

import mx.utng.finer_back_end.Publicos.Services.EmailService;
import mx.utng.finer_back_end.Publicos.Services.PublicosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

@RestController
@RequestMapping("/api/token")
public class PublicosController {
    
    private static final SecureRandom random = new SecureRandom();
    public static String tokenGenerado = null;
    private final PublicosService usuarioService;
    private final EmailService emailService;

    public PublicosController(PublicosService usuarioService, EmailService emailService) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }

    /**
     * - Genera el token de 6 dígitos numéricos
     * @return regresa el código de 6 dígitos
     */
    private String generarTokenNumerico() {
        return String.valueOf(100000 + random.nextInt(900000));
    }

    /**
     * Obtiene el correo del usuario según su ID y envía el token.
     * @param correoUsuario correo del usuario a donde se enviará el token.
     * @return Mensaje de confirmación o error.
     */
    public ResponseEntity<String> enviarToken(@PathVariable String correoUsuario) {
        tokenGenerado = generarTokenNumerico();

        boolean enviado = emailService.mandarTokenNumerico(correoUsuario, tokenGenerado);

        if (enviado) {
            return ResponseEntity.ok("Token enviado a " + correoUsuario);
        } else {
            return ResponseEntity.status(500).body("Error al enviar el token.");
        }
    }

    /**
     * Comparar los tokens que coincidan y mandar true o false en caso de que sean iguales, 
     * este token sera enviado desde front-end.
     * @param token Token que se envia en parámetros de la ruta
     * @return true si son iguales, si no false
     */
    @GetMapping("/comparar")
    private Boolean compararToken(@RequestParam String token){
        return tokenGenerado != null && token.equals(tokenGenerado);
    }

    /**
     * Método para iniciar sesión en la aplicación
     * después de verfiicar la identidad en la base de datos
     * y a su correo electrónico
     * @param obj - Obj que se recibe desde el frontend
     * @return - Regresa la respuesta al frontend que el usuario se autentico y se envio su token 
     * @apiNote - El token que se genera solo funciona mientras la aplicación esta en su linea de vida (no se reinicio)
     */
    @GetMapping("/iniciar-sesion")
    private ResponseEntity<String> iniciarSesion(@RequestParam String nombreUsuario, @RequestParam String contrasenia) {
        try {
            // Llamamos a la función 'autenticarUsuario' del servicio
            Object[] usuarioDatos = usuarioService.autenticarUsuario(nombreUsuario, contrasenia);

            // Verificamos si el usuario fue autenticado correctamente en la base de datos
            if (usuarioDatos != null && (Boolean) usuarioDatos[2]) {
                String idUsuario = String.valueOf(usuarioDatos[0]);
                String correo = (String) usuarioDatos[1];
                try {
                    this.enviarToken(correo);
                } catch (Exception e) {
                    return ResponseEntity.status(500).body("Ocurrio un error al enviar el correo" + e);
                }
                return ResponseEntity.ok("Identificación en espera del usuario ID: " + idUsuario + " y token enviado a: " + correo);
            } else {
                return ResponseEntity.status(401).body("Usuario o contraseña incorrectos.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al autenticar: " + e.getMessage());
        }
    }
}
