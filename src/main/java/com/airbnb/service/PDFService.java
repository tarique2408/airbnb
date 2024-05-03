package com.airbnb.service;
import com.airbnb.dto.BookingDto;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
@Service
public class PDFService {
        public boolean generatePdf(String fileName, BookingDto dto)  {
            try {
                Document document = new Document();
                //getInstance() responsible to writing content to pdf document (1st arg) ,2-location where content should be written
                PdfWriter.getInstance(document, new FileOutputStream(fileName));

                document.open();
                Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
                //create table
                Chunk chunk = new Chunk("Booking Details", font);
                PdfPTable table = new PdfPTable(2); // 2 columns
                //syntax-
//            table.addCell("Field");
//            table.addCell("Value");

                table.addCell("Booking Id");
                table.addCell(String.valueOf(dto.getBookingId()));

                table.addCell("Guest Name");
                table.addCell(dto.getGuestName());

                table.addCell("User Email");
                table.addCell(dto.getEmail());

                table.addCell("Price of One Day");
                table.addCell(String.valueOf(dto.getPrice()));

                table.addCell("Total Price");
                table.addCell(String.valueOf(dto.getTotalPrice()));

                document.add(chunk);
                document.add(new Paragraph("\n"));
                document.add(table);
                document.close();
                return true;
            }catch (Exception e){
                e.printStackTrace();

            }
            //it will run because we handled the exception so remaining code will execute as it is.
            return false;
//          Chunk chunk = new Chunk("Booking Confirmation", font);
//            //add the booking details into pdf
//            Chunk bookingId = new Chunk("Booking Id"+dto.getBookingId(), font);
//            Chunk guestName = new Chunk("Guest Name"+dto.getGuestName(), font);
//            Chunk email = new Chunk("User email"+dto.getEmail(), font);
//            Chunk price = new Chunk("price of one day"+dto.getPrice(), font);
//            Chunk totalPrice = new Chunk("Total price"+dto.getTotalPrice(), font);
//            document.add(chunk);
//            document.add(new Paragraph("\n"));
//            document.add(bookingId);
//            document.add(new Paragraph("\n"));
//            document.add(guestName);
//            document.add(new Paragraph("\n"));
//            document.add(email);
//            document.add(new Paragraph("\n"));
//            document.add(price);
//            document.add(new Paragraph("\n"));
//            document.add(totalPrice);
//            document.close();
        }
}
