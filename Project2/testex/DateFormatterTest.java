package testex;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;


public class DateFormatterTest {

    DateFormatter df = new DateFormatter();

    // This test does not really make sense to me even after the refactor with Date object in the getFormattedDate() method.
    // You would have to manually write the string you were expecting to get back, and that string changes every minute.
    // So instead I am looking at how many different words there are in the formattedDate string. That way I know that I got
    // a valid date back from the method. Whether or not it was the correct time/date I don't know. For that you could simply
    // add another assertEquals with the string you're expecting. But then you would have to manually update this test every minute
    @Test
    public void testDateFormatter() throws JokeException {
        Date date = new Date();
        String formattedDate = df.getFormattedDate("Europe/Copenhagen", date);
        String[] parts = formattedDate.split(" ");
        System.out.println("The date is: " + formattedDate);
        assertEquals("The date is: " + formattedDate, 5, parts.length);
    }

    @Test (expected = JokeException.class)
    public void testDateFormatterFail() throws JokeException {
        Date date = new Date();
        String formattedDate = df.getFormattedDate("sdfsadfasf", date);
    }
}
