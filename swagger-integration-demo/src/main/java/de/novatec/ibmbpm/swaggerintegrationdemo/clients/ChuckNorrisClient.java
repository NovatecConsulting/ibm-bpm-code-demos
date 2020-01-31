package de.novatec.ibmbpm.swaggerintegrationdemo.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.novatec.ibmbpm.swaggerintegrationdemo.schema.chucknorris.ChuckNorrisFact;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Slf4j
@Component
public class ChuckNorrisClient {

    private static final String resourceUrl = "https://api.chucknorris.io/jokes/random";

    private HttpClient httpClient = HttpClients.createDefault();

    public ChuckNorrisFact callChuckNorrisIo() {
        // Build Request
        HttpGet request = new HttpGet(resourceUrl);
        ObjectMapper mapper = new ObjectMapper();
        String result = null;

        try {
            // Call target service
            HttpResponse response = httpClient.execute(request);

            // Extract data from response
            String json = EntityUtils.toString(response.getEntity());
            log.debug(json);
            result = mapper.readTree(json).get("value").asText();

            return new ChuckNorrisFact(result);
        } catch (IOException ex) {
            // Catch Exceptions from service call
            log.error("Error while calling chucknorris.io: ", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
