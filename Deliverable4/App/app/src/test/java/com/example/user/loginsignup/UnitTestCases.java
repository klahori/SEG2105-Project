package com.example.user.loginsignup;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class UnitTestCases {
    @Test//test 1 for deliverable 3
    public void checkDate() {
        Avalibility aAvailable = new Avalibility("1","11-11-2018","5:00","6:00");
        assertEquals("Check the date of availability", "11-11-2018", aAvailable.getDate());
    }

    @Test//test 2 for deliverable 3
    public void checkStartTime() {
        Avalibility aAvailable = new Avalibility("1","11-11-2018","5:00","6:00");
        assertEquals("Check the start time of availability", "5:00", aAvailable.getStartTime());
    }

    @Test
    public void checkServiceName() {
        Service aService = new Service("1","Cleaning",12);
        assertEquals("Check the name of the service", "Cleaning", aService.getServiceName());
    }
    @Test
    public void checkServiceId() {
        Service aService = new Service("1","Cleaning",12);
        assertEquals("Check the id of the Service", "1", aService.getId());
    }

    @Test
    public void checkServiceCost() {
        Service aService = new Service("1","Cleaning",12);
        double expected = 12.0;
        assertEquals("Check the price of the service", expected, aService.getCost(),0);
    }
    @Test
    public void checkUserName() {
        User aUser = new User("something","Jonny","Boy","Jonny@gmail.com","1231231234","somwhere land","Home Owner","12","12","1996");
        assertEquals("Check the first name of the User","Jonny", aUser.getFirstName());
    }

    @Test
    public void checkUserEmail() {
        User aUser = new User("something","Jonny","Boy","Jonny@gmail.com","1231231234","somwhere land","Home Owner","12","12","1996");
        assertEquals("Check the email address of the user", "Jonny@gmail.com", aUser.getEmail());
    }

}
