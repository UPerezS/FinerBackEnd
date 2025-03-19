package mx.utng.finer_back_end.Documentos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "certificado") // Nombre de la tabla en la BD
public class CertificadoDocumento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_certificado")
    private Integer idCertificado;
    
    @NotNull
    @Column(name = "id_inscripcion")
    private Integer idInscripcion;
    
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_emision")
    private Date fechaEmision;
    
    @Lob
    @Column(name = "certificado_pdf")
    private byte[] certificadoPdf;
    
    // Getters y Setters
    
    public Integer getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(Integer idCertificado) {
        this.idCertificado = idCertificado;
    }

    public Integer getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(Integer idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public byte[] getCertificadoPdf() {
        return certificadoPdf;
    }

    public void setCertificadoPdf(byte[] certificadoPdf) {
        this.certificadoPdf = certificadoPdf;
    }
}

