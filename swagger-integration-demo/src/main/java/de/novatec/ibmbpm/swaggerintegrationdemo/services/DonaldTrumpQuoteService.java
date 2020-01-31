package de.novatec.ibmbpm.swaggerintegrationdemo.services;

import de.novatec.ibmbpm.swaggerintegrationdemo.clients.TronaldDumpClient;
import de.novatec.ibmbpm.swaggerintegrationdemo.schema.donaldtrump.DonaldTrumpQuote;
import de.novatec.ibmbpm.swaggerintegrationdemo.schema.donaldtrump.DonaldTrumpQuoteRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Provides the caller with Donald Trump quotes
 * @author HKR
 */
@RestController
@RequestMapping(value = "/donaldtrump", produces = "application/json")
@Slf4j
public class DonaldTrumpQuoteService {

    @Autowired
    private TronaldDumpClient client;

    /**
     * Provides the caller with a random Donald Trump Quote
     * @param subject (optional) The subject, the random quote will be about
     * @return a random Donald Trump quote
     */
    @RequestMapping(method = RequestMethod.GET)
    public DonaldTrumpQuote getRandomDonaldTrumpQuote(
            @RequestParam(value = "subject", required = false) String subject
    ) {
        log.debug("GET");
        return client.callTronaldDump(subject);
    }

    /**
     * Same as {@link #getRandomDonaldTrumpQuote(String subject)} but with HTTP POST
     * @param body Request Body containing a subject
     * @return a random Donald Trump quote
     */
    @RequestMapping(method = RequestMethod.POST)
    public DonaldTrumpQuote postRandomDonaldTrumpQuote(
            @RequestBody DonaldTrumpQuoteRequest body
    ) {
        log.debug("POST");
        return client.callTronaldDump(body.getSubject());
    }
}
