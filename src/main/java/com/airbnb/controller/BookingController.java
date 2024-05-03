package com.airbnb.controller;
import com.airbnb.dto.BookingDto;
import com.airbnb.entity.Booking;
import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.service.BucketService;
import com.airbnb.service.PDFService;
import com.airbnb.service.SmsService;
import com.itextpdf.text.DocumentException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    private BookingRepository bookingRepository;
    private PropertyRepository propertyRepository;
    private PDFService pdfService;
    private BucketService bucketService;

    private SmsService service;

    public BookingController(BookingRepository bookingRepository, PropertyRepository propertyRepository, PropertyRepository propertyRepository1, PDFService pdfService, BucketService bucketService, SmsService service) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository1;
        this.pdfService = pdfService;
        this.bucketService = bucketService;
        this.service = service;
    }

    //create booking for a user of property
    //and after booking is done we create pdf for booking details and save it into local device and then store the pdf at
    //aws s3 bucket and get the url of pdf and save the url in database and send the url to user to download the pdf.
    //url-http://localhost:8080/api/v1/booking/createBooking/{propertyId}
    @PostMapping("/createBooking/{propertyId}")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking,
                                           @PathVariable long propertyId,
                                           @AuthenticationPrincipal PropertyUser user) throws DocumentException, IOException {
        //set the user details in booking
        booking.setPropertyUser(user);
        //get the per night price of property so first get object of that property based on id and get the price
        Property property = propertyRepository.findById(propertyId).get();
        int nightlyPrice=property.getNightlyPrice();
        int totalPrice=nightlyPrice*booking.getTotalNight();
        //set totalPrice
        booking.setTotalPrice(totalPrice);
        //set property
        booking.setProperty(property);

        //1-property booked
        Booking completeBookingInfo = bookingRepository.save(booking);


        //create Object BookingDto and set the value
        BookingDto dto=new BookingDto();
        dto.setBookingId(completeBookingInfo.getId());
        dto.setGuestName(completeBookingInfo.getGuestName());
        dto.setEmail(user.getEmail());
        dto.setPrice(property.getNightlyPrice());
        dto.setTotalPrice(completeBookingInfo.getTotalPrice());

        //now send dto to generatePdf for add the booking details in pdf

        //2- generate pdf that consist booking details
        //dynamically set the name of pdf for every booking why id because id is unique for every booking
        boolean b = pdfService.generatePdf("D://pdfCreation//" + "booking-confirmation-id" + completeBookingInfo.getId() + ".pdf", dto);
        //if pdf generated and store in file then upload the file on aws s3
        if (b){
            MultipartFile multiPartFile = convert("D://pdfCreation//" + "booking-confirmation-id" + completeBookingInfo.getId() + ".pdf");
            //3-upload file in bucket s3
            String url = bucketService.uploadFile(multiPartFile, "airbnb5");
            //4-send this url to user's phone number to download booking pdf
            String smsId = service.sendSms("+919868692786", "Booking successful click for see booking details" + url);
            System.out.println(smsId);
            return new ResponseEntity<>(url,HttpStatus.OK);

        }else{
            return new ResponseEntity<>("something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static MultipartFile convert(String filePath) throws IOException {
        //load the file from specified path
        File file=new File(filePath);

        //read the file content into byte array
        byte[] fileContent= Files.readAllBytes(file.toPath());

        //convert byte array to a resource (ByteArrayResource)
        Resource resource = new ByteArrayResource(fileContent);

        //create Multipartfile from resource
        MultipartFile multipartFile =new MultipartFile() {
            @Override
            public String getName() {
                return file.getName();
            }

            @Override
            public String getOriginalFilename() {
                return file.getName();
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return fileContent.length == 0;
            }

            @Override
            public long getSize() {
                return fileContent.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return fileContent;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return resource.getInputStream();
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                Files.write(dest.toPath(), fileContent);
            }
        };
        return multipartFile;

    }

}
