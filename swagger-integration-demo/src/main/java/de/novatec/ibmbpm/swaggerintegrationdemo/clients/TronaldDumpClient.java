package de.novatec.ibmbpm.swaggerintegrationdemo.clients;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.novatec.ibmbpm.swaggerintegrationdemo.schema.donaldtrump.DonaldTrumpQuote;
import de.novatec.ibmbpm.swaggerintegrationdemo.util.LocalDateConversionUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Component
public class TronaldDumpClient {

    private static final String RESOURCE_URL = "https://api.tronalddump.io/random/quote";
    private static final String NOT_FOUND_ERROR_MSG = "Couldn't find Trump Quote with the given subject!";
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    @PostConstruct
    public void setUp() {
        restTemplate = new RestTemplate();

        objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public DonaldTrumpQuote callTronaldDump(String subject) {
        try {
            // Build request URI
            URI uri = new URIBuilder(RESOURCE_URL).build();

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setAcceptCharset(Arrays.asList(Charsets.UTF_8));

            // add user agent to prevent "403 FORBIDDEN" from server
            headers.add("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                            "(KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            // Create Body
            String requestJson = objectMapper.writeValueAsString(new TronalddumpRequest(subject));

            // Build request entity
            HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

            // Send Request to Server
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
                    requestEntity, String.class);

            // Handle response
            int statusCode = responseEntity.getStatusCodeValue();
            if (statusCode == 404) {
                log.error(NOT_FOUND_ERROR_MSG);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_ERROR_MSG);
            }
            else if (statusCode != 200) {
                log.error("Error while calling tronalddump.io. Status code: "
                        + statusCode + ". Reason: " + responseEntity.getStatusCode().getReasonPhrase());
                throw new ResponseStatusException(
                        HttpStatus.valueOf(statusCode),
                        responseEntity.getStatusCode().getReasonPhrase()
                );
            }

            // Read data from response
            TronalddumpResponse response = objectMapper.readValue(responseEntity.getBody(), TronalddumpResponse.class);
            return new DonaldTrumpQuote(response.value, LocalDateConversionUtil.convertDateToLocalDateTime(response.appeared_at));
        }
        catch (URISyntaxException ex) {
            // Catch Exception from setting parameters
            log.error("Error building URI for tronalddump.io: ", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (IOException ex) {
            // Catch Exception from calling the target service
            log.error("Error calling tronalddump.io: ", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @AllArgsConstructor @ToString
    private static class TronalddumpRequest {
        String tag;
    }

    @NoArgsConstructor @ToString
    private static class TronalddumpResponse {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
        Date appeared_at;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
        Date created_at;

        String value;
        String[] tags;
    }
}
