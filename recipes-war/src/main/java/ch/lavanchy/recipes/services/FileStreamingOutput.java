package ch.lavanchy.recipes.services;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Map the {@link FileInputStream} to the StreamingOutput
 *
 * @since 1.0.0
 */
class FileStreamingOutput implements StreamingOutput {

    private InputStream input;

    FileStreamingOutput(InputStream input) {
        this.input = input;
    }

    @Override
    public void write(OutputStream output)
            throws IOException, WebApplicationException {
        try {
            System.out.println(String.format("Start reading file. Estimated size=%d", input.available()));
            int bytes;
            while ((bytes = input.read()) != -1) {
                output.write(bytes);
            }
            System.out.println("Reading the stream complete");
        } catch (Exception e) {
            throw new WebApplicationException(e);
        } finally {
            if (output != null) output.close();
            if (input != null) input.close();
        }
    }

}
