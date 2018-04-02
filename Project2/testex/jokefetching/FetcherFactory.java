package testex.jokefetching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FetcherFactory implements IFetcherFactory{

    private final List<String> availableTypes = Arrays.asList("EduJoke", "ChuckNorris", "Moma", "Tambal");

    @Override
    public List<String> getAvailableTypes() {
        return availableTypes;
    }

    @Override
    public List<IJokeFetcher> getJokeFetchers(String jokesToFetch) {
        List<IJokeFetcher> result = new ArrayList<>();
        String[] fetchers = jokesToFetch.split(",");
        for (String type : fetchers) {
            switch (type) {
                case "EduJoke":
                    result.add(new EduJoke());
                    break;
                case "ChuckNorris":
                    result.add(new ChuckNorrisJoke());
                    break;
                case "Moma":
                    result.add(new MomaJoke());
                    break;
                case "Tambal":
                    result.add(new TambalJoke());
                    break;
            }
        }
        return result;
    }
}
