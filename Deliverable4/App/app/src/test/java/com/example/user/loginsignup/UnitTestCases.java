package com.example.user.loginsignup;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class UnitTestCases {
    @Test//test 1 for deliverable 4
    public void checkServiceName() {
        ServiceInformation service = new ServiceInformation("Cleaning","50","1a2b3c");
        assertEquals("Check the Name of the Service", "Cleaning", service.getServiceName());
    }

    @Test//test 2 for deliverable 4
    public void checkServiceCost() {
        ServiceInformation service = new ServiceInformation("Cleaning","50","1a2b3c");
        assertEquals("Check the Cost of the Service", "50", service.getCost());
    }

    @Test//test 3 for deliverable 4
    public void checkServiceId() {
        ServiceInformation service = new ServiceInformation("Cleaning","50","1a2b3c");
        assertEquals("Check the Id of the Service Provider", "1a2b3c", service.getId());
    }
    @Test//test 4 for deliverable 4
    public void checkRating1() {
        ServiceInformation service = new ServiceInformation("Cleaning","50","1a2b3c");
        assertEquals("Check the Rating of the Service Provider Before updating the Rating", "unrated", service.getRating());
    }

    @Test//test 5 for deliverable 4
    public void checkRating2() {
        ServiceInformation service = new ServiceInformation("Cleaning","50","1a2b3c");
        service.setRating("4");
        assertEquals("Check the Rating of the Service Provider After updating the Rating", "4", service.getRating());
    }
    @Test//test 6 for deliverable 4
    public void checkEmail() {
        ServiceInformation service = new ServiceInformation("Cleaning","50","1a2b3c");
        service.setEmail("ServiceProvider@gmail.com");
        assertEquals("Check the Email of the Service Provider", "ServiceProvider@gmail.com", service.getEmail());
    }

    @Test//test 7 for deliverable 4
    public void checkStartTime() {
        ServiceInformation service = new ServiceInformation("Cleaning","50","1a2b3c");
        service.setStartTime("Start Time: 3:14");
        assertEquals("Check the Start Time", "Start Time: 3:14", service.getStartTime());
    }
    @Test//test 8 for deliverable 4
    public void checkEndTime() {
        ServiceInformation service = new ServiceInformation("Cleaning","50","1a2b3c");
        service.setEndTime("End Time: 4:14");
        assertEquals("Check the End Time", "End Time: 4:14", service.getEndTime());
    }
    @Test//test 9 for deliverable 4
    public void checkBookingId() {
        Avalibility booking = new Avalibility("1a2b3c","Date","Start Time: 3:14","End Time: 4:14");
        assertEquals("Check the Booking Id", "1a2b3c",booking.getId());
    }
    @Test//test 10 for deliverable 4
    public void checkBookingDate() {
        Avalibility booking = new Avalibility("1a2b3c","Date","Start Time: 3:14","End Time: 4:14");
        assertEquals("Check the date of the Booking", "Date",booking.getDate());
    }

}
