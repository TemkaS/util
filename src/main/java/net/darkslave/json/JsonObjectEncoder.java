/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.json;

import java.io.IOException;



/**
 * Интерфейс кастомного сериализатора
 */
@FunctionalInterface
public interface JsonObjectEncoder {

    public void encode(JsonEncoder encoder, Object value, int level) throws IOException;

}

