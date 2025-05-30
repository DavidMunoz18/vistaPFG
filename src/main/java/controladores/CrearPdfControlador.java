package controladores;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dtos.UsuarioDto;
import servicios.AutentificacionServicio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet que genera un PDF con el listado de usuarios obtenidos de la API.
 */
@WebServlet("/exportarUsuariosPdf")
public class CrearPdfControlador extends HttpServlet {

    private AutentificacionServicio authService = new AutentificacionServicio();

    /**
     * Procesa peticiones GET y escribe en la respuesta un PDF con la tabla de usuarios.
     *
     * @param request  objeto HttpServletRequest
     * @param response objeto HttpServletResponse con content-type application/pdf
     * @throws ServletException si ocurre un error de servlet
     * @throws IOException      si ocurre un error de IO
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<UsuarioDto> usuarios = authService.obtenerUsuarios();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.pdf");

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font fontHeader = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            String[] headers = {"ID", "Nombre", "Teléfono", "Email", "Rol"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, fontHeader));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            for (UsuarioDto u : usuarios) {
                table.addCell(String.valueOf(u.getIdUsuario()));
                table.addCell(u.getNombreUsuario());
                table.addCell(u.getTelefonoUsuario());
                table.addCell(u.getEmailUsuario());
                table.addCell(u.getRol());
            }

            document.add(table);
        } catch (DocumentException de) {
            throw new IOException(de);
        } finally {
            document.close();
        }
    }
}