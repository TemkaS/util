/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.prop;





public class StringParameter extends AbstractStringPresenter {
    private final String value;


    public StringParameter(String value) {
        this.value = value;
    }


    @Override
    protected String getValue() {
        return value;
    }


}
