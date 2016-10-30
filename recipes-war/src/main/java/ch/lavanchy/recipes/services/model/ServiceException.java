package ch.lavanchy.recipes.services.model;

/**
 * @since 2.2.0
 */
public class ServiceException {
    private int status;
    private int code;
    private String message;
    private String stacktrace;

    private ServiceException() {}

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public static ServiceExceptionBuilder builder() {
        return new ServiceExceptionBuilder();
    }

    public static class ServiceExceptionBuilder {
        private final ServiceException serviceException = new ServiceException();

        private ServiceExceptionBuilder() {}

        public ServiceExceptionBuilder withStatus(final int status) {
            serviceException.status = status;
            return this;
        }

        public ServiceExceptionBuilder withCode(final int code) {
            serviceException.code = code;
            return this;
        }
        public ServiceExceptionBuilder withMessage(final String message) {
            serviceException.message = message;
            return this;
        }
        public ServiceExceptionBuilder withStacktrace(final String stacktrace) {
            serviceException.stacktrace = stacktrace;
            return this;
        }

        public ServiceException toServiceException() {
            return serviceException;
        }
    }
}
