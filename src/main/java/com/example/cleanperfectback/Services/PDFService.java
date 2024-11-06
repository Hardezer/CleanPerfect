package com.example.cleanperfectback.Services;

import com.example.cleanperfectback.Entities.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class PDFService {

    @Autowired
    private ProductoService productoService;

    public ByteArrayInputStream generateSalidaPDF(Salida salida, List<DetalleSalida> detalles) throws IOException, DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // Add logo
        ClassPathResource imgFile = new ClassPathResource("static/css/Logo_cleanperfect.png");
        Image img = Image.getInstance(imgFile.getURL());
        img.scaleToFit(100, 100);
        img.setAlignment(Element.ALIGN_CENTER);
        document.add(img);

        // Title
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
        Paragraph title = new Paragraph("Registro de Salida", font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); 

        // Client details
        Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        document.add(new Paragraph("Cliente: " + salida.getEmpresa().getNombre(), smallFont));
        document.add(new Paragraph("Fecha de Salida: " + salida.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), smallFont));

        document.add(new Paragraph(" "));

        // Table
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 2, 2});

        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("Producto", smallFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Cantidad", smallFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Fecha Reingreso", smallFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        hcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(hcell);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "CL"));

        for (DetalleSalida detalle : detalles) {
            PdfPCell cell;

            // Obtener el nombre del producto basado en el tipo de ItemSalida
            ItemSalida item = detalle.getItemSalida();
            String itemNombre = item.getNombre();
            String fechaReingreso = "-";

            if (item instanceof Producto) {
                Producto producto = (Producto) item;
                itemNombre = productoService.getNombreProductoCompleto(producto.getId());
            } else if (item instanceof Ropa) {
                Ropa ropa = (Ropa) item;
                itemNombre = ropa.getNombre() + " (Talla: " + ropa.getTalla() + ")";
            } else if (item instanceof Maquinaria) {
                Maquinaria maquinaria = (Maquinaria) item;
                itemNombre = maquinaria.getNombre();
                if (maquinaria.getFechaReingreso() != null) {
                    fechaReingreso = maquinaria.getFechaReingreso().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
            }

            cell = new PdfPCell(new Phrase(itemNombre, smallFont));
            cell.setPaddingLeft(5);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(detalle.getCantidad()), smallFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(fechaReingreso, smallFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

        }

        document.add(table);


        document.add(new Paragraph(" ", smallFont));

        for (int i = 0; i < 10; i++) {
            document.add(new Paragraph(" "));
        }

        PdfPTable supervisorTable = new PdfPTable(2);
        supervisorTable.setWidthPercentage(100);
        supervisorTable.setWidths(new int[]{1, 1});

        PdfPCell cell = new PdfPCell(new Phrase("_________________________", smallFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        supervisorTable.addCell(cell);

        cell = new PdfPCell(new Phrase("_________________________", smallFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        supervisorTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Nombre y Firma de Colaborador", smallFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        supervisorTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Firma del Supervisor", smallFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        supervisorTable.addCell(cell);

        document.add(supervisorTable);

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}