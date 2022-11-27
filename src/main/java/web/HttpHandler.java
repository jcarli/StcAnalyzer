package web;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;

public class HttpHandler implements DataHandler {

    private final HttpClient httpClient;

    public HttpHandler(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String getJson(URI uri) {
        try {
        HttpGet request = new HttpGet(uri);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    } catch (IOException e) {
        throw new RuntimeException(String.format("HttpGet on %s failed", uri.toString()), e);
    }
}
}
