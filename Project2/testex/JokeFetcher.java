
package testex;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.response.ExtractableResponse;
import testex.jokefetching.FetcherFactory;
import testex.jokefetching.IFetcherFactory;
import testex.jokefetching.IJokeFetcher;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Class used to fetch jokes from a number of external joke API's
 */
public class JokeFetcher {
  
  /**
   * These are the valid types that can be used to indicate required jokes
   * eduprog: Contains joke related to programming and education. API only returns a new value each hour
   * chucknorris: Fetch a chucknorris joke. Not always political correct ;-)
   * moma: Fetch a "MOMA" joke. Defenitely never political correct ;-)
   * tambal: Just random jokes
   */
  private IDateFormatter dateFormatter;
  private IFetcherFactory factory;

  public JokeFetcher(IDateFormatter dateFormatter, IFetcherFactory iFF) {
    this.dateFormatter = dateFormatter;
    this.factory = iFF;
  }
  
  /**
   * Verifies whether a provided value is a valid string (contained in availableTypes)
   * @param jokeTokens. Example (with valid values only): "eduprog,chucknorris,chucknorris,moma,tambal"
   * @return true if the param was a valid value, otherwise false
   */
  boolean isStringValid(String jokeTokens){
    String[] tokens = jokeTokens.split(",");
      for(String token: tokens){
      if(!factory.getAvailableTypes().contains(token)){
        return false;
      }
    }
    return true;
  }
  
  /**
   * Fetch jokes from external API's as given in the input string - jokesToFetch
   * @param jokesToFetch A comma separated string with values (contained in availableTypes) indicating the jokes
   * to fetch. Example: "eduprog,chucknorris,chucknorris,moma,tambal" will return five jokes (two chucknorris)
   * @param timeZone. Must be a valid timeZone string as returned by: TimeZone.getAvailableIDs()  
   * @return A Jokes instance with the requested jokes + time zone adjusted string representing fetch time
   * (the jokes list can contain null values, if a server did not respond correctly)
   * @throws JokeException. Thrown if either of the two input arguments contains illegal values
   */
  public Jokes getJokes(String jokesToFetch, String timeZone) throws JokeException{
    if(!isStringValid(jokesToFetch)){
      throw new JokeException("Inputs (jokesToFetch) contain types not recognized");
    }
    Jokes jokes = new Jokes();
    for (IJokeFetcher fetcher : factory.getJokeFetchers(jokesToFetch)) {
      jokes.addJoke(fetcher.getJoke());
    }
    String tzString = dateFormatter.getFormattedDate(timeZone, new Date());
    jokes.setTimeZoneString(tzString);
    return jokes;

  }
  
  /**
   * DO NOT TEST this function. It's included only to get a quick way of executing the code
   * @param args 
   */
  public static void main(String[] args) throws JokeException {
    JokeFetcher jf = new JokeFetcher(new DateFormatter(), new FetcherFactory());
    Jokes jokes = jf.getJokes("Moma,Tambal","Europe/Copenhagen");
    jokes.getJokes().forEach((joke) -> {
      System.out.println(joke);
    });
//    System.out.println("Is String Valid: "+jf.isStringValid("edu_prog,xxx"));
  }
}
