package ch.lavanchy.recipes.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Write all the bytes onto the StreamingOutput
 *
 * @since 2.0.0
 */
class BytesStreamingOutput implements StreamingOutput {
    private static final Logger LOGGER = LogManager.getLogger(BytesStreamingOutput.class);

    private final File pdf;

    BytesStreamingOutput(final File pdf) {
        this.pdf = pdf;
    }

    @Override
    public void write(final OutputStream outputStream) throws IOException, WebApplicationException {
        try {
            final byte[] bytes = Files.readAllBytes(pdf.toPath());
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bytes.length);
            LOGGER.debug("Start reading file. Estimated size={}", bytes.length);

            final long start = System.currentTimeMillis();
            byteArrayOutputStream.write(bytes, 0, bytes.length);
            byteArrayOutputStream.writeTo(outputStream);
            final long end = System.currentTimeMillis();

            LOGGER.debug("Reading the stream complete - duration={}", end - start);
        } catch (final Exception e) {
            throw new WebApplicationException(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (final IOException e) {
                    LOGGER.error("Error while closing the output stream", e);
                }
            }
        }
    }
}
