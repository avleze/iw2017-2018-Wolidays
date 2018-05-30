package es.uca.wolidays.backend.invoice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import es.uca.wolidays.backend.entities.Reserva;
import es.uca.wolidays.backend.entities.Usuario;
import java.io.OutputStream;

@Service
public class PDFService {

	public void generatePDF(OutputStream outputStream, Reserva res, Usuario anfitrion) throws DocumentException, MalformedURLException, IOException {
    	final String IMG = "src/main/resources/PDFLogo/WolidaysLogo.png";
    	
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();
        
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 38, Font.BOLD, new BaseColor(4, 116, 156));
        Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
        
    	LineSeparator lineFullWidht = new LineSeparator();
    	lineFullWidht.setLineColor(new BaseColor(245, 91, 11));
    	lineFullWidht.setOffset(-8);
        
        Chunk header = new Chunk("Factura", titleFont);  
        
        Chapter chapter = new Chapter(new Paragraph(header), 1);
        chapter.setNumberDepth(0);
        
        Image wolidaysLogo = Image.getInstance(IMG);
        wolidaysLogo.setAbsolutePosition(
        		(PageSize.A4.getWidth() - wolidaysLogo.getScaledWidth()) - 70f,
        		(PageSize.A4.getHeight() - wolidaysLogo.getScaledHeight()) - 70f);
        chapter.add(wolidaysLogo);
        
        Paragraph firstParagraph = new Paragraph("Wolidays S.L.", paragraphFont);
        firstParagraph.setLeading(0,2);
        chapter.add(firstParagraph);
        chapter.add(new Paragraph("Adelfa Gua 5", paragraphFont));
        chapter.add(new Paragraph("España, ESP, 1084"));
        
        document.add(chapter);
        
        document.add(createDescriptionTable(anfitrion,res));
        document.add(lineFullWidht);
        document.add(createHeaderTable());
        document.add(lineFullWidht);
        document.add(createBodyTable(res));
        
        document.close();
	}
	
public static PdfPTable createDescriptionTable(Usuario anfitrion, Reserva res) {
    	
    	PdfPTable table = new PdfPTable(3);
    	table.setWidthPercentage(95f);
    	table.setSpacingBefore(10f);
    	table.setSpacingAfter(10f);
    	
    	Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new BaseColor(4, 116, 156));
    	Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
    	
    	Paragraph p;
    	
    	p = new Paragraph("Huésped", titleFont);
    	
    	PdfPCell cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    	cell.setBorder(Rectangle.NO_BORDER);
    	cell.setPaddingTop(13);
    	table.addCell(cell);
    	
    	p = new Paragraph("Anfitrión", titleFont);
    	
    	cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    	cell.setBorder(Rectangle.NO_BORDER);
    	cell.setPaddingTop(13);
    	table.addCell(cell);
    	
    	Paragraph paragraph = new Paragraph();
    	paragraph.add(new Chunk("Nº de Reserva", titleFont));
    	paragraph.add(new Chunk("  #"+res.getId(), paragraphFont));

    	cell = new PdfPCell(paragraph);
    	cell.setBorder(Rectangle.NO_BORDER);
    	cell.setPaddingTop(20);
    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    	table.addCell(cell);
    	cell = new PdfPCell();
    	p = new Paragraph(res.getUsuario().getApellidos() + ", " +res.getUsuario().getNombre(), paragraphFont);
    	cell.addElement(p);
    	cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    	cell.setBorder(Rectangle.NO_BORDER);
    	cell.setPaddingTop(5);
    	table.addCell(cell);
    	
    	cell = new PdfPCell();
    	p = new Paragraph(anfitrion.getNombre() + ", "+ anfitrion.getApellidos(), paragraphFont);
    	cell.addElement(p);
    	cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    	cell.setBorder(Rectangle.NO_BORDER);
    	cell.setPaddingTop(5);
    	table.addCell(cell);
    	
    	Paragraph paragraph2 = new Paragraph();
    	paragraph2.add(new Chunk("Fecha", titleFont));
    	paragraph2.add(new Chunk("  "+res.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), paragraphFont));

    	cell = new PdfPCell(paragraph2);
    	cell.setBorder(Rectangle.NO_BORDER);
    	cell.setPaddingTop(10);
    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    	table.addCell(cell);
    	
    	cell = new PdfPCell();
    	p = new Paragraph(res.getUsuario().getCorreo(), paragraphFont);
    	cell.addElement(p);
    	cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    	cell.setBorder(Rectangle.NO_BORDER);
    	cell.setPaddingTop(5);
    	table.addCell(cell);
    	
    	cell = new PdfPCell();
    	p = new Paragraph(anfitrion.getCorreo(), paragraphFont);
    	cell.addElement(p);
    	cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    	cell.setBorder(Rectangle.NO_BORDER);
    	cell.setPaddingTop(5);
    	table.addCell(cell);
    	
    	Paragraph paragraph3 = new Paragraph();
    	paragraph3.add(new Chunk("Fecha Venc.", titleFont));
    	paragraph3.add(new Chunk("  " + res.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), paragraphFont));

    	cell = new PdfPCell(paragraph3);
    	cell.setBorder(Rectangle.NO_BORDER);
    	cell.setPaddingTop(10);
    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    	table.addCell(cell);
    	
    	return table;
    }
    
    private static PdfPTable createHeaderTable() {
    	
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(95f);
		
    	Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new BaseColor(4, 116, 156));
    	
    	Paragraph p;
    	
    	p = new Paragraph("Noches", titleFont);
    	p.setAlignment(Element.ALIGN_LEFT);
    	
    	PdfPCell cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	p = new Paragraph("Descripción", titleFont);
    	p.setAlignment(Element.ALIGN_LEFT);
    	
    	cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	p = new Paragraph("Precio Und.", titleFont);
    	p.setAlignment(Element.ALIGN_RIGHT);
    	
    	cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	p = new Paragraph("Importe", titleFont);
    	p.setAlignment(Element.ALIGN_RIGHT);
    	
    	cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	return table;
	}
    
    private static PdfPTable createBodyTable(Reserva res) {
    	
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(95f);
		table.setSpacingBefore(10f);
	
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new BaseColor(4, 116, 156));
    	Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
    	
    	Paragraph p;
    	
    	int noches = (int) ChronoUnit.DAYS.between(res.getFechaInicio(), res.getFechaFin());
    	
    	p = new Paragraph(String.valueOf(noches), paragraphFont);
    	p.setAlignment(Element.ALIGN_CENTER);
    	
    	PdfPCell cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	p = new Paragraph("Reserva de apartamento #" + res.getApartamento().getId(), paragraphFont);
    	p.setAlignment(Element.ALIGN_LEFT);
    	
    	cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	p = new Paragraph(res.getPrecioFinal() / noches + " €/noche", paragraphFont);
    	p.setAlignment(Element.ALIGN_RIGHT);
    	
    	cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	p = new Paragraph(res.getPrecioFinal() + " €", paragraphFont);
    	p.setAlignment(Element.ALIGN_RIGHT);
    	
    	cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	p = new Paragraph("Subtotal:", paragraphFont);
    	p.setAlignment(Element.ALIGN_RIGHT);
    	p.setLeading(0,3);
    	
    	cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setColspan(3);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	p = new Paragraph(res.getPrecioFinal() + " €", paragraphFont);
    	p.setAlignment(Element.ALIGN_RIGHT);
    	p.setLeading(0,3);
    	
    	cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	p = new Paragraph("IVA:", paragraphFont);
    	p.setAlignment(Element.ALIGN_RIGHT);
    	
    	cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setColspan(3);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	p = new Paragraph("21% España", paragraphFont);
    	p.setAlignment(Element.ALIGN_RIGHT);
    	
    	cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	p = new Paragraph("TOTAL:", titleFont);
    	p.setAlignment(Element.ALIGN_RIGHT);
    	
    	cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setColspan(3);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	p = new Paragraph(String.valueOf(res.getPrecioFinal() + (0.21 * res.getPrecioFinal())) + " €", paragraphFont);
    	p.setAlignment(Element.ALIGN_RIGHT);
    	
    	cell = new PdfPCell();
    	cell.addElement(p);
    	cell.setBorder(Rectangle.NO_BORDER);
    	table.addCell(cell);
    	
    	return table;
	}
}
