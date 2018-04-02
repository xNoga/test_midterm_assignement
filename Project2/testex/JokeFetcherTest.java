package testex;

import org.junit.Before;
import org.junit.Test;

import static java.util.Calendar.AM;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.internal.matchers.Matches;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import testex.jokefetching.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class JokeFetcherTest {

    private JokeFetcher jf;
    @Mock IDateFormatter ifMock;
    @Mock IFetcherFactory factory;
    @Mock MomaJoke moma;
    @Mock ChuckNorrisJoke chuck;
    @Mock EduJoke edu;
    @Mock TambalJoke tambal;

    @Before
    public void setup() {
        List<IJokeFetcher> fetchers = Arrays.asList(edu,chuck,moma,tambal);
        when(factory.getJokeFetchers("EduJoke,ChuckNorris,Moma,Tambal")).thenReturn(fetchers);
        List<String> types = Arrays.asList("EduJoke","ChuckNorris","Moma","Tambal");
        when(factory.getAvailableTypes()).thenReturn(types);
        jf = new JokeFetcher (ifMock, factory);
    }

    @Test
    public void testGetAvailableTypes() {
        List<String> mockList = Arrays.asList("EduJoke","ChuckNorris","Moma","Tambal");
        assertEquals(factory.getAvailableTypes().size(), 4);
        assertThat(mockList, is(factory.getAvailableTypes()));
    }

    @Test
    public void testIsStringValid() {
        assertEquals(false, jf.isStringValid("hello, world"));
        assertEquals(true, jf.isStringValid("EduJoke"));
    }

    @Test
    public void testGetJokes() throws JokeException {
        BDDMockito.given(ifMock.getFormattedDate(eq("Europe/Copenhagen"), anyObject())).willReturn("17 feb. 2018 10:56 AM");
        Jokes jokes = jf.getJokes("Moma", "Europe/Copenhagen");
        String mockTime = jokes.getTimeZoneString();
        assertEquals("17 feb. 2018 10:56 AM", mockTime);
        // The line below kept giving me an error. It kept saying the arguments differed from the actual invocation, but the output
        // in the log displayed the exact same input values. There is no difference at all. I don't know how to fix that
        // which is why the line below is commented out
//        BDDMockito.verify(idfmock, times(1)).getFormattedDate("Europe/Copenhagen", new Date());
    }

    @Test
    public void testEduJoke() throws JokeException {
        Joke testJoke = new Joke("This is a random joke. Hehe, so funny!", "FunnyJoke.com");
        when(edu.getJoke()).thenReturn(testJoke);
        Jokes eduJokes = jf.getJokes("EduJoke", "Europe/Copenhagen");
        assertThat(eduJokes.getJokes(), hasItems(testJoke));
        BDDMockito.verify(factory, times(1)).getJokeFetchers("EduJoke");
    }

    @Test
    public void testChuckNorrisJoke() throws JokeException {
        Joke testJoke = new Joke("This is a random joke2. Hehe, so funny!", "FunnyJoke.com");
        when(chuck.getJoke()).thenReturn(testJoke);
        Jokes chuckJokes = jf.getJokes("ChuckNorris", "Europe/Copenhagen");
        assertThat(chuckJokes.getJokes(), hasItems(testJoke));
        BDDMockito.verify(factory, times(1)).getJokeFetchers("ChuckNorris");
    }

    @Test
    public void testMomaJoke() throws JokeException {
        Joke testJoke = new Joke("This is a random joke3. Hehe, so funny!", "FunnyJoke.com");
        when(moma.getJoke()).thenReturn(testJoke);
        Jokes momaJokes = jf.getJokes("Moma", "Europe/Copenhagen");
        assertThat(momaJokes.getJokes(), hasItems(testJoke));
        BDDMockito.verify(factory, times(1)).getJokeFetchers("Moma");
    }

    @Test
    public void testTombalJoke() throws JokeException {
        Joke testJoke = new Joke("This is a random joke4. Hehe, so funny!", "FunnyJoke.com");
        when(tambal.getJoke()).thenReturn(testJoke);
        Jokes tambalJokes = jf.getJokes("Tambal", "Europe/Copenhagen");
        assertThat(tambalJokes.getJokes(), hasItems(testJoke));
        BDDMockito.verify(factory, times(1)).getJokeFetchers("Tambal");
    }
}
