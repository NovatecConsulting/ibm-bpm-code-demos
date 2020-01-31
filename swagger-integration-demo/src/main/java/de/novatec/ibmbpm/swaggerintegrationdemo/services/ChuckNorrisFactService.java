package de.novatec.ibmbpm.swaggerintegrationdemo.services;

import de.novatec.ibmbpm.swaggerintegrationdemo.clients.ChuckNorrisClient;
import de.novatec.ibmbpm.swaggerintegrationdemo.schema.chucknorris.ChuckNorrisFact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides the caller with Chuck Norris facts
 * @author HKR
 */
@RestController
@RequestMapping(value = "/chucknorris", produces = "application/json")
public class ChuckNorrisFactService {

    @Autowired
    private ChuckNorrisClient client;

    /**
     * Provides the caller with a random Chuck Norris Fact
     * @return a random Chuck Norris Fact
     */
    @RequestMapping(method = RequestMethod.GET)
    public ChuckNorrisFact getChuckNorrisFact() {
        return client.callChuckNorrisIo();
    }
}
