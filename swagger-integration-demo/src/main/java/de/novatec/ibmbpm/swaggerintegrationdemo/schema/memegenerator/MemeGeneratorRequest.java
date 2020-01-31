package de.novatec.ibmbpm.swaggerintegrationdemo.schema.memegenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class MemeGeneratorRequest {

    @Getter @Setter
    private String topText;
    @Getter @Setter
    private String bottomText;
    @Getter @Setter
    private MemeFont font;

    @AllArgsConstructor
    public enum MemeFont {
        IMPACT("impact"),
        ARIAL("arial");
        public final String value;
    }
}
