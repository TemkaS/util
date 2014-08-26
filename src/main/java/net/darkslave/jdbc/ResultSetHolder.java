/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.jdbc;


import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;



public class ResultSetHolder implements AutoCloseable {
    private final ResultSet result;


    public ResultSetHolder(ResultSet result) {
        this.result = result;
    }


    public ResultSet getResultSet() {
        return result;
    }


    public boolean getBoolean(String name) throws SQLException {
        return result.getInt(name) != 0;
    }


    public boolean getBoolean(int index) throws SQLException {
        return result.getInt(index) != 0;
    }


    public Integer getInteger(String name) throws SQLException {
        int value = result.getInt(name);

        if (result.wasNull())
            return null;

        return value;
    }


    public Integer getInteger(int index) throws SQLException {
        int value = result.getInt(index);

        if (result.wasNull())
            return null;

        return value;
    }


    public Long getLong(String name) throws SQLException {
        long value = result.getLong(name);

        if (result.wasNull())
            return null;

        return value;
    }


    public Long getLong(int index) throws SQLException {
        long value = result.getLong(index);

        if (result.wasNull())
            return null;

        return value;
    }


    public Double getDouble(String name) throws SQLException {
        double value = result.getDouble(name);

        if (result.wasNull())
            return null;

        return value;
    }


    public Double getDouble(int index) throws SQLException {
        double value = result.getDouble(index);

        if (result.wasNull())
            return null;

        return value;
    }


    public Date getDate(String name) throws SQLException {
        return result.getTimestamp(name);
    }


    public Date getDate(int index) throws SQLException {
        return result.getTimestamp(index);
    }


    public BigDecimal getBigDecimal(String name) throws SQLException {
        return result.getBigDecimal(name);
    }


    public BigDecimal getBigDecimal(int index) throws SQLException {
        return result.getBigDecimal(index);
    }


    public String getString(String name) throws SQLException {
        return result.getString(name);
    }


    public String getString(int index) throws SQLException {
        return result.getString(index);
    }


    public Object getObject(String name) throws SQLException {
        return result.getObject(name);
    }


    public Object getObject(int index) throws SQLException {
        return result.getObject(index);
    }


    public Array getArray(String name) throws SQLException {
        return result.getArray(name);
    }


    public Array getArray(int index) throws SQLException {
        return result.getArray(index);
    }


    public Clob getClob(String name) throws SQLException {
        return result.getClob(name);
    }


    public Clob getClob(int index) throws SQLException {
        return result.getClob(index);
    }


    public Blob getBlob(String name) throws SQLException {
        return result.getBlob(name);
    }


    public Blob getBlob(int index) throws SQLException {
        return result.getBlob(index);
    }


    public boolean next() throws SQLException {
        return result.next();
    }


    @Override
    public void close() throws SQLException {
        result.close();
    }

}
