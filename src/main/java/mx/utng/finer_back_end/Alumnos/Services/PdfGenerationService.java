package mx.utng.finer_back_end.Alumnos.Services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.element.Table;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;

import mx.utng.finer_back_end.Alumnos.Documentos.CertificadoDetalleDTO;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfGenerationService {

    public byte[] generarCertificado(CertificadoDetalleDTO certificadoDetalleDTO) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, PageSize.A4.rotate()); 
        DeviceRgb customGreen = new DeviceRgb(50, 205, 32); 


        Paragraph titulo = new Paragraph("Certificado de Curso")
                .setFontSize(28)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(customGreen) 
                .setMarginBottom(20);
        document.add(titulo);


        String logoPath = getClass().getClassLoader().getResource("finer_logo.png").getPath();
        Image logo = new Image(ImageDataFactory.create(logoPath));
        logo.setWidth(100).setHeight(100);
        document.add(logo.setFixedPosition(10, 520)); 

        Paragraph appName = new Paragraph("Finer")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT)
                .setFontColor(customGreen) 
                .setFixedPosition(120, 520, 300); 
        document.add(appName);


        Paragraph detalleAlumno = new Paragraph("Este certificado se otorga a:")
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(detalleAlumno);


        Paragraph nombreAlumno = new Paragraph(certificadoDetalleDTO.getNombreCompletoAlumno())
                .setFontSize(22)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(customGreen)
                .setMarginBottom(20);
        document.add(nombreAlumno);


        Paragraph curso = new Paragraph("por completar con éxito")
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(curso);


        Paragraph nombreCurso = new Paragraph(certificadoDetalleDTO.getTituloCurso())
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(customGreen)
                .setMarginBottom(40);
        document.add(nombreCurso);

        Paragraph detallesAdicionales = new Paragraph(
                "Curso ofrecido por Universidad Tecnológica del Norte de Guanajuato")
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(detallesAdicionales);

        Paragraph firma = new Paragraph("Firma del Instructor")
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10);
        document.add(firma);

        document.add(new Paragraph("_____________________")
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph(certificadoDetalleDTO.getNombreInstructor())
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(30));


        float[] columnWidths = { 1, 2, 1, 2 }; 
        Table infoTable = new Table(columnWidths);

        infoTable.setBackgroundColor(DeviceRgb.WHITE);
        infoTable.setBorder(null);

        infoTable.addCell("Instructor:");
        infoTable.addCell(certificadoDetalleDTO.getNombreInstructor());

        infoTable.addCell("Matrícula:");
        infoTable.addCell(certificadoDetalleDTO.getMatricula());

        infoTable.addCell("Fecha de Inscripción:");
        infoTable.addCell(certificadoDetalleDTO.getFechaInscripcion().toString());

        infoTable.addCell("Fecha de Generación:");
        infoTable.addCell(certificadoDetalleDTO.getFechaGeneracion().toString());


        document.add(infoTable.setFixedPosition(50, 50, 750));
                                                             

        document.close();

        return byteArrayOutputStream.toByteArray();
    }
}
