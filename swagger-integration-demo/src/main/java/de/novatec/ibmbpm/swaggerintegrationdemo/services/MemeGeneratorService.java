package de.novatec.ibmbpm.swaggerintegrationdemo.services;

import de.novatec.ibmbpm.swaggerintegrationdemo.clients.ImgflipClient;
import de.novatec.ibmbpm.swaggerintegrationdemo.schema.chucknorris.ChuckNorrisMeme;
import de.novatec.ibmbpm.swaggerintegrationdemo.schema.memegenerator.MemeGeneratorRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Generates memes. Template is chosen by resource url.
 * @author HKR
 */
@Slf4j
@RestController
@RequestMapping("/meme")
public class MemeGeneratorService {

    private final static String CHUCKNORRIS_TEMPLATE_ID = "38579871";

    @Autowired
    private ImgflipClient client;

    /**
     * Creates a "Chuck Norris Guns" meme
     * @param request {@link MemeGeneratorRequest} containing top and bottom text and font.
     * @return meme image url
     */
    @RequestMapping(value = "/chucknorris", method = RequestMethod.POST)
    public ChuckNorrisMeme generateChuckNorrisMeme(
            @RequestBody MemeGeneratorRequest request
    ) {
        String memeUrl = client.generateMeme(
                request.getTopText(),
                request.getBottomText(),
                CHUCKNORRIS_TEMPLATE_ID,
                request.getFont().value
        );
        return new ChuckNorrisMeme(memeUrl);
    }

}
