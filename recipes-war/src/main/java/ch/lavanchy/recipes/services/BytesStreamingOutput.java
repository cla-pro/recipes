package ch.lavanchy.recipes.services;

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
    private final File pdf;

    BytesStreamingOutput(final File pdf) {
        this.pdf = pdf;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException, WebApplicationException {
        try {
            final byte[] bytes = Files.readAllBytes(pdf.toPath());
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bytes.length);
            System.out.println(String.format("Start reading file. Estimated size=%d", bytes.length));

            final long start = System.currentTimeMillis();
            byteArrayOutputStream.write(bytes, 0, bytes.length);
            byteArrayOutputStream.writeTo(outputStream);
            final long end = System.currentTimeMillis();

            System.out.println("Reading the stream complete - duration: " + (end - start));
        } catch (Exception e) {
            throw new WebApplicationException(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (final IOException e) {
                    System.out.println("Error while closing the output stream");
                    e.printStackTrace();
                }
            }
        }
    }
}
