package util.resc;




public class ResourceTimeoutException extends ResourceException {
    private static final long serialVersionUID = -4387979998708575107L;


    public ResourceTimeoutException() {
        super("Waiting time is out");
    }

}
