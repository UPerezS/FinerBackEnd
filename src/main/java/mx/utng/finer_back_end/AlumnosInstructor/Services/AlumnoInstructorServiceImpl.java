package mx.utng.finer_back_end.AlumnosInstructor.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AlumnoInstructorServiceImpl implements AlumnoInstructorService {

    @Value("${hunter.io.api.key}")
    private String apiKey;

    private final String apiUrl = "https://api.hunter.io/v2/email-verifier";

    @Override
    public boolean verificarCorreo(String correo) {
        String url = apiUrl + "?email=" + correo + "&api_key=" + apiKey;

        // Usamos RestTemplate para hacer la solicitud HTTP a la API de Hunter.io
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        try {
            // Usamos Jackson para parsear la respuesta JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            String status = root.path("data").path("status").asText();

            return "valid".equals(status); // Comprobamos si el correo es válido
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
