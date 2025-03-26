package mx.utng.finer_back_end.Instructor.Documentos;

public class OpcionEvaluacionInstructorDTO {

    private String textoOpcion;
    private Boolean verificar;  // Cambiar "correcta" por "verificar"

    // Getters y Setters
    public String getTextoOpcion() {
        return textoOpcion;
    }

    public void setTextoOpcion(String textoOpcion) {
        this.textoOpcion = textoOpcion;
    }

    public Boolean getVerificar() {
        return verificar;  // Cambiar "correcta" por "verificar"
    }

    public void setVerificar(Boolean verificar) {
        this.verificar = verificar;  // Cambiar "correcta" por "verificar"
    }
}
