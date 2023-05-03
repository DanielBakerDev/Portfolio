package com.info5059.casestudy.purchase;

import com.info5059.casestudy.products.ProductRepository;
import com.info5059.casestudy.vendor.Vendor;
import com.info5059.casestudy.vendor.VendorRepository;
import com.info5059.casestudy.products.Product;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
public abstract class PurchaseOrderReportPDFGenerator extends AbstractPdfView {
    public static ByteArrayInputStream generateReport(String repid,
                                                      PurchaseOrderDAO repDAO,
                                                      VendorRepository vendorRepository,
                                                      ProductRepository productRepository) throws IOException {
        URL imageUrl = PurchaseOrderReportPDFGenerator.class.getResource("/static/assets/logo.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(baos);
        // Initialize PDF document to be written to a stream not a file
        PdfDocument pdf = new PdfDocument(writer);
        // Document is the main object
        Document document = new Document(pdf);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        // add the image to the document
        Image img = new Image(ImageDataFactory.create(imageUrl))
                .scaleAbsolute(120, 40)
                .setFixedPosition(80, 710);
        document.add(img);
        // now let's add a big heading
        document.add(new Paragraph("\n\n"));
        Locale locale = new Locale("en", "US");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);

        try {

            PurchaseOrder po = repDAO.findOne(Long.parseLong(repid));
            //HEADING
            document.add(new Paragraph(String.format("Purchase Order"))
                    .setFont(font)
                    .setFontSize(24)
                    .setMarginRight(75)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBold());
            document.add(new Paragraph("#" + repid)
                    .setFont(font)
                    .setFontSize(16)
                    .setBold()
                    .setMarginRight(90)
                    .setMarginTop(-10)
                    .setTextAlignment(TextAlignment.RIGHT));

            //EMPLOYEE INFORMATION
            Optional<Vendor> opt = vendorRepository.findById(po.getVendorid());
            if (opt.isPresent()) {
                Vendor vendor = opt.get();
                Table table2 = new Table(2);
                table2.setWidth(new UnitValue(UnitValue.PERCENT, 30));
                table2.setBorder(Border.NO_BORDER);

                Cell cell = new Cell().add(new Paragraph("Vendor:"))
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT.LEFT);
                table2.addCell(cell);
                cell = new Cell().add(new Paragraph(vendor.getName()))
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT.LEFT);
                table2.addCell(cell);
                cell = new Cell().add(new Paragraph(""))
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT.LEFT);
                table2.addCell(cell);
                cell = new Cell().add(new Paragraph(vendor.getAddress1()))
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT.LEFT);
                table2.addCell(cell);
                cell = new Cell().add(new Paragraph(""))
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT.LEFT);
                table2.addCell(cell);
                cell = new Cell().add(new Paragraph(vendor.getCity()))
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT.LEFT);
                table2.addCell(cell);
                cell = new Cell().add(new Paragraph(""))
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT.LEFT);
                table2.addCell(cell);
                cell = new Cell().add(new Paragraph(vendor.getProvince()))
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT.LEFT);
                table2.addCell(cell);
                cell = new Cell().add(new Paragraph(""))
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT.LEFT);
                table2.addCell(cell);
                cell = new Cell().add(new Paragraph(vendor.getEmail()))
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT.LEFT);
                table2.addCell(cell);
                document.add(table2);

            }
            document.add(new Paragraph("\n\n"));


            //TABLE
            // now a 3 column table
            Table table = new Table(5);
            table.setWidth(new UnitValue(UnitValue.PERCENT, 100));
            // Unfortunately we must format each cell individually :(
            // table headings
            Cell cell = new Cell().add(new Paragraph("Product Code")
                    .setFont(font)
                    .setFontSize(12)
                    .setBold())
                    .setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);
            cell = new Cell().add(new Paragraph("Description")
                    .setFont(font)
                    .setFontSize(12)
                    .setBold())
                    .setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);
            cell = new Cell().add(new Paragraph("Qty Sold")
                    .setFont(font)
                    .setFontSize(12)
                    .setBold())
                    .setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);
            cell = new Cell().add(new Paragraph("Price")
                    .setFont(font)
                    .setFontSize(12)
                    .setBold())
                    .setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);
            cell = new Cell().add(new Paragraph("EXT. Price")
                    .setFont(font)
                    .setFontSize(12)
                    .setBold())
                    .setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);

            //FOREACH EACH ITEM
            BigDecimal sub_total = new BigDecimal(0.0);
            BigDecimal tax = new BigDecimal(0.0);
            BigDecimal total = new BigDecimal(0.0);

            for (PurchaseOrderLineitem line : po.getItems()) {

                Optional<Product> optx = productRepository.findById(line.getProductid());

                if (optx.isPresent()) {
                    Product product = optx.get();

                    //Additions etc
                    BigDecimal ext = line.getPrice().multiply(new BigDecimal(line.getQty()));
                    sub_total = sub_total.add(ext, new MathContext(8, RoundingMode.UP));
                    tax   = tax.add(ext.multiply(BigDecimal.valueOf(0.13)));
                    total = sub_total.add(tax);

                    // table details
                    cell = new Cell().add(new Paragraph(String.valueOf(product.getId()))
                            .setFont(font)
                            .setFontSize(12)
                            .setTextAlignment(TextAlignment.LEFT));
                    table.addCell(cell);
                    cell = new Cell().add(new Paragraph(product.getName())
                            .setFont(font)
                            .setFontSize(12)
                            .setTextAlignment(TextAlignment.LEFT));
                    table.addCell(cell);
                    cell = new Cell().add(new Paragraph(String.valueOf(line.getQty()))
                            .setFont(font)
                            .setFontSize(12)
                            .setTextAlignment(TextAlignment.LEFT));
                    table.addCell(cell);
                    cell = new Cell().add(new Paragraph(String.valueOf(formatter.format(line.getPrice())))
                            .setFont(font)
                            .setFontSize(12)
                            .setTextAlignment(TextAlignment.RIGHT.RIGHT));
                    table.addCell(cell);
                    cell = new Cell().add(new Paragraph(String.valueOf(formatter.format(line.getPrice().multiply(new BigDecimal(line.getQty())))))
                            .setFont(font)
                            .setFontSize(12)
                            .setTextAlignment(TextAlignment.RIGHT.RIGHT));
                    table.addCell(cell);


                }
            }


            // table total
            // report total
            cell = new Cell(1, 4).add(new Paragraph("Sub Total:"))
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.RIGHT.RIGHT);
            table.addCell(cell);
            cell = new Cell().add(new Paragraph(formatter.format(sub_total)))
                    .setTextAlignment(TextAlignment.RIGHT.RIGHT)
                    .setBackgroundColor(ColorConstants.YELLOW);
            table.addCell(cell);
            cell = new Cell(1, 4).add(new Paragraph("Tax:"))
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.RIGHT.RIGHT);
            table.addCell(cell);
            cell = new Cell().add(new Paragraph(formatter.format(tax)))
                    .setTextAlignment(TextAlignment.RIGHT.RIGHT)
                    .setBackgroundColor(ColorConstants.YELLOW);
            table.addCell(cell);
            cell = new Cell(1, 4).add(new Paragraph("Po Total:"))
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.RIGHT.RIGHT);
            table.addCell(cell);
            cell = new Cell().add(new Paragraph(formatter.format(total)))
                    .setTextAlignment(TextAlignment.RIGHT.RIGHT)
                    .setBackgroundColor(ColorConstants.YELLOW);
            table.addCell(cell);

            document.add(table);

            //DATE
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph(String.valueOf(new Date()))
                    .setTextAlignment(TextAlignment.CENTER));

            //CLOSE
            document.close();
        }catch (Exception ex) {
            Logger.getLogger(PurchaseOrderReportPDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }
}
