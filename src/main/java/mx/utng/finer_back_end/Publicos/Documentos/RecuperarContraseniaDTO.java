package mx.utng.finer_back_end.Publicos.Documentos;

public class RecuperarContraseniaDTO {
    private String correoUsuario;
    private String token;
    private String nuevaContrasenia;

    public RecuperarContraseniaDTO() {
    }

    public String getCorreo() {
        return correoUsuario;
    }
    public void setCorreo(String correo) {
        this.correoUsuario = correo;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getNuevaContrasenia() {
        return nuevaContrasenia;
    }
    public void setNuevaContrasenia(String nuevaContrasenia) {
        this.nuevaContrasenia = nuevaContrasenia;
    }
}
