package de.novatec.ibmbpm.swaggerintegrationdemo.schema.donaldtrump;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor
public class DonaldTrumpQuote {

    @Getter @Setter
    private String quote;

    @Getter @Setter
    private LocalDateTime dateOfIssuance;
}
