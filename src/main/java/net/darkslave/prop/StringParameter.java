/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.prop;





public class StringParameter extends StringPresenter {
    private final String value;


    public StringParameter(String value) {
        this.value = value;
    }


    @Override
    public String getValue() {
        return value;
    }


}
