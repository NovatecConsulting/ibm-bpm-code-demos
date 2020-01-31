package de.novatec.ibmbpm.swaggerintegrationdemo.clients;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

@Component
@PropertySource("classpath:auth.properties")
@Slf4j
public class ImgflipClient {

    private final String resourceUrl = "https://api.imgflip.com/caption_image";
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private URIBuilder uriBuilder;

    // TODO on first Run: IMPORTANT: Create file 'src/main/resources/auth.properties'
    //  and specify user credentials for imgflip.com
    @Value("${imgflip.username}")
    private String user;
    @Value("${imgflip.password}")
    private String pass;

    @PostConstruct
    public void postConstruct() {
        restTemplate = new RestTemplate();

        objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String generateMeme(String topText, String bottomText, String templateId, String font) {
        try {
            // build request
            uriBuilder = new URIBuilder(resourceUrl)
                .addParameter("template_id", templateId)
                .addParameter("text0", topText)
                .addParameter("text1", bottomText)
                .addParameter("font", font)
                .addParameter("username", user)
                .addParameter("password", pass);

            // set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            // add user agent to prevent "403 FORBIDDEN" from server
            headers.add("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            // execute
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            URI uri = uriBuilder.build();
            ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);

            // process response
            ImgflipResonse responseBody = objectMapper.readValue(response.getBody(), ImgflipResonse.class);
            if (!responseBody.success)
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

            return responseBody.data.url;
        }
        catch (URISyntaxException ex) {
            log.warn("Error building URI: ", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (HttpClientErrorException ex) {
            log.error(String.format("Error calling Imgflip: %s.",ex.getStatusText()), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        catch (IOException ex) {
            log.error("Error resolving data on Imgflip call: ", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @NoArgsConstructor
    private static class ImgflipResonse {
        @Getter @Setter
        private boolean success;
        @Getter @Setter
        private Data data;
        @Getter @Setter
        private String error_message;

        @NoArgsConstructor
        class Data {
            @Getter @Setter
            private String url;
            @Getter @Setter
            private String page_url;
        }
    }
}
