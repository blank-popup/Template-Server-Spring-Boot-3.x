package org.duckdns.ahamike.rollbook.util.mime;

import org.springframework.http.MediaType;

public class ParserMimeType {
    public static InfoMimeType parse(String mimeTypeString) {
        if (mimeTypeString == null || mimeTypeString.isBlank()) {
            throw new IllegalArgumentException("MIME type string cannot be null or blank");
        }

        MediaType mediaType = MediaType.parseMediaType(mimeTypeString.trim());

        return new InfoMimeType(
                mediaType.getType(),
                mediaType.getSubtype(),
                mediaType.getParameters()
        );
    }
}
