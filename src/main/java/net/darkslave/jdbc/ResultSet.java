/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.jdbc;


import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;



public class ResultSet implements AutoCloseable {
    private static final String EMPTY_STRING = "";
    private static final byte[] EMPTY_BYTES  = new byte[0];

    private final java.sql.ResultSet target;


    public ResultSet(java.sql.ResultSet target) {
        this.target = target;
    }


    public java.sql.ResultSet getResultSet() {
        return target;
    }


    public boolean getBoolean(String name) throws SQLException {
        return getBoolean(index(name));
    }


    public boolean getBoolean(int index) throws SQLException {
        return target.getInt(index) != 0;
    }


    public Integer getInteger(String name) throws SQLException {
        return getInteger(index(name));
    }


    public Integer getInteger(int index) throws SQLException {
        int value = target.getInt(index);

        if (target.wasNull())
            return null;

        return value;
    }


    public Long getLong(String name) throws SQLException {
        return getLong(index(name));
    }


    public Long getLong(int index) throws SQLException {
        long value = target.getLong(index);

        if (target.wasNull())
            return null;

        return value;
    }


    public Double getDouble(String name) throws SQLException {
        return getDouble(index(name));
    }


    public Double getDouble(int index) throws SQLException {
        double value = target.getDouble(index);

        if (target.wasNull())
            return null;

        return value;
    }


    public Date getDate(String name) throws SQLException {
        return getDate(index(name));
    }


    public Date getDate(int index) throws SQLException {
        return target.getTimestamp(index);
    }


    public BigDecimal getBigDecimal(String name) throws SQLException {
        return getBigDecimal(index(name));
    }


    public BigDecimal getBigDecimal(int index) throws SQLException {
        return target.getBigDecimal(index);
    }


    public String getString(String name) throws SQLException {
        return getString(index(name));
    }


    public String getString(int index) throws SQLException {
        return target.getString(index);
    }


    public Object getObject(String name) throws SQLException {
        return getObject(index(name));
    }


    public Object getObject(int index) throws SQLException {
        return target.getObject(index);
    }


    public Array getArray(String name) throws SQLException {
        return getArray(index(name));
    }


    public Array getArray(int index) throws SQLException {
        return target.getArray(index);
    }


    public String getClob(String name) throws SQLException {
        return getClob(index(name));
    }


    public String getClob(int index) throws SQLException {
        Clob stream = target.getClob(index);
        long length = stream.length();

        if (length == 0)
            return EMPTY_STRING;

        if (length > Integer.MAX_VALUE)
            throw new OutOfMemoryError("Clob length " + length + " is too large");

        return stream.getSubString(1, (int) length);
    }


    public byte[] getBlob(String name) throws SQLException {
        return getBlob(index(name));
    }


    public byte[] getBlob(int index) throws SQLException {
        Blob stream = target.getBlob(index);
        long length = stream.length();

        if (length == 0)
            return EMPTY_BYTES;

        if (length > Integer.MAX_VALUE)
            throw new OutOfMemoryError("Blob length " + length + " is too large");

        return stream.getBytes(1, (int) length);
    }


    public boolean next() throws SQLException {
        return target.next();
    }


    public ResultSetMetaData getMetaData() throws SQLException {
        return target.getMetaData();
    }


    private int index(String name) throws SQLException {
        return target.findColumn(name);
    }


    @Override
    public void close() throws SQLException {
        target.close();
    }

}
