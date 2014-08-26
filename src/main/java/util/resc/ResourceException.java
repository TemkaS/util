package util.resc;




public class ResourceException extends Exception {
    private static final long serialVersionUID = 8523625994784452696L;


    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }


    public ResourceException(String message) {
        super(message);
    }


    public ResourceException(Throwable cause) {
        super(cause);
    }


    public static ResourceException wrap(Throwable basic) {
        if (basic instanceof ResourceException) {
            return (ResourceException) basic;
        }

        if (basic instanceof InterruptedException) {
            Thread.currentThread().interrupt();
            return new ResourceInterruptedException(basic);
        }

        return new ResourceException(basic);
    }


    public static ResourceException wrap(ResourceException basic, Throwable other) {
        if (basic == null)
            return wrap(other);

        basic.addSuppressed(other);

        return basic;
    }

}
