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
        assertEquals("Check the Rating of the Service Provider After updating the Rating", "ServiceProvider@gmail.com", service.getEmail());
    }

    @Test
    public void checkUserEmail() {
        User aUser = new User("something","Jonny","Boy","Jonny@gmail.com","1231231234","somwhere land","Home Owner","12","12","1996");
        assertEquals("Check the email address of the user", "Jonny@gmail.com", aUser.getEmail());
    }

}
