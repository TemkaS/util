/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.json;

import java.io.IOException;




/**
 *  Ошибка json сериализации
 */
public class JsonException extends IOException {
    private static final long serialVersionUID = 2051156110978766383L;


    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }


    public JsonException(String message) {
        super(message);
    }


    public JsonException(Throwable cause) {
        super(cause);
    }


}