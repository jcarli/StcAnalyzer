package web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import training.Club;
import training.Workout;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class DataFetcher {
    private final URI baseUri;
    private final Function<Integer, URI> clubUri;
    private final DataHandler handler;

    public static DataFetcher createWithHttp(String baseUri) {
        HttpClient client = HttpClients.createDefault();
        DataHandler httpHandler = new HttpHandler(client);

        URI uri;
        try {
            uri = new URI(baseUri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Function<Integer, URI> clubUri = integer -> {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                LocalDateTime start = LocalDateTime.now();
                LocalDateTime end = start.plusDays(7);

                URIBuilder uriBuilder = new URIBuilder(baseUri);
                List<String> pathSegments = Arrays.stream(String.format("%s/%d/%s", uriBuilder.getPath(), integer, "groupactivities").split("/")).toList();
                uriBuilder.setPathSegments(pathSegments);

                uriBuilder.setParameters(new BasicNameValuePair("period.start", start.format(formatter)), new BasicNameValuePair("period.end", end.format(formatter)));
                return uriBuilder.build();

            } catch (URISyntaxException e) {
                throw new RuntimeException("URI syntax not correct", e);
            }
        };

        return new DataFetcher(httpHandler, uri, clubUri);
    }
    public DataFetcher(DataHandler handler, URI baseUri, Function<Integer, URI> clubUri) {
        this.handler = handler;
        this.baseUri = baseUri;
        this.clubUri = clubUri;
    }

    public static <T> List<T> transform(Class<T[]> type, String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return Arrays.asList(mapper.readValue(json, type));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse json to club", e);
        }
    }

    public List<Club> getClubData() {
        String json = handler.getJson(baseUri);
        List<Club> clubs = transform(Club[].class, json);

        List<CompletableFuture<Void>> futures = clubs.stream().map(club ->
                CompletableFuture.runAsync(() -> club.setWorkouts(transform(Workout[].class, getClubDataAsJson(club.id()))))).toList();

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return clubs;
    }

    public String getClubDataAsJson(int id) {
        return handler.getJson(clubUri.apply(id));

    }
}
