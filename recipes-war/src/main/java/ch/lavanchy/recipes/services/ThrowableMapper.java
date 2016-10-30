package ch.lavanchy.recipes.services;

import ch.lavanchy.recipes.services.model.ServiceException;
import com.google.gson.Gson;
import com.google.inject.Inject;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;

/**
 * Map the exceptions to a 500 HTTP response
 *
 * @since 2.1.0
 */
@Provider
public class ThrowableMapper implements ExceptionMapper<Throwable> {
    @Inject
    public ThrowableMapper() {}

    @Override
    public Response toResponse(final Throwable throwable) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(createJsonFromException(throwable))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private String createJsonFromException(final Throwable throwable) {
        return new Gson().toJson(ServiceException
                .builder()
                .withCode(0)
                .withMessage(throwable.getMessage())
                .withStacktrace(String.format("%s\n%s", throwable.getClass().getName(), stackTraceToString(throwable.getStackTrace())))
                .toServiceException()
        );
    }

    private String stackTraceToString(final StackTraceElement[] stackTrace) {
        return Arrays
                .stream(stackTrace)
                .map(s -> String.format("%s\n", s.toString()))
                .reduce(String::concat)
                .orElse("no stack trace");
    }
}
