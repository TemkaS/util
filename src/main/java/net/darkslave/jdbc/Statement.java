/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.jdbc;


import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;





public class Statement implements AutoCloseable {
    private final PreparedStatement target;


    public Statement(PreparedStatement target) {
        this.target = target;
    }


    public PreparedStatement getStatement() {
        return target;
    }


    public void setBoolean(int index, Boolean value) throws SQLException {
        if (value != null) {
            target.setInt(index, value ? 1 : 0);
        } else {
            target.setNull(index, Types.INTEGER);
        }
    }


    public void setBoolean(int index, boolean value) throws SQLException {
        target.setInt(index, value ? 1 : 0);
    }


    public void setInteger(int index, Integer value) throws SQLException {
        if (value != null) {
            target.setInt(index, value.intValue());
        } else {
            target.setNull(index, Types.INTEGER);
        }
    }


    public void setInteger(int index, int value) throws SQLException {
        target.setInt(index, value);
    }


    public void setLong(int index, Long value) throws SQLException {
        if (value != null) {
            target.setLong(index, value.longValue());
        } else {
            target.setNull(index, Types.BIGINT);
        }
    }


    public void setLong(int index, long value) throws SQLException {
        target.setLong(index, value);
    }


    public void setDouble(int index, Double value) throws SQLException {
        if (value != null) {
            target.setDouble(index, value.doubleValue());
        } else {
            target.setNull(index, Types.DOUBLE);
        }
    }


    public void setDouble(int index, double value) throws SQLException {
        target.setDouble(index, value);
    }


    public void setDate(int index, Date value) throws SQLException {
        if (value != null) {
            target.setTimestamp(index, new Timestamp(value.getTime()));
        } else {
            target.setNull(index, Types.TIMESTAMP);
        }
    }


    public void setBigDecimal(int index, BigDecimal value) throws SQLException {
        if (value != null) {
            target.setBigDecimal(index, value);
        } else {
            target.setNull(index, Types.NUMERIC);
        }
    }


    public void setString(int index, String value) throws SQLException {
        if (value != null) {
            target.setString(index, value);
        } else {
            target.setNull(index, Types.VARCHAR);
        }
    }


    public void setArray(int index, Array value) throws SQLException {
        if (value != null) {
            target.setArray(index, value);
        } else {
            target.setNull(index, Types.ARRAY);
        }
    }


    public void setStruct(int index, Struct value) throws SQLException {
        if (value != null) {
            target.setObject(index, value, Types.STRUCT);
        } else {
            target.setNull(index, Types.STRUCT);
        }
    }


    public void setClob(int index, Clob value) throws SQLException {
        if (value != null) {
            target.setClob(index, value);
        } else {
            target.setNull(index, Types.CLOB);
        }
    }


    public void setBlob(int index, Blob value) throws SQLException {
        if (value != null) {
            target.setBlob(index, value);
        } else {
            target.setNull(index, Types.BLOB);
        }
    }


    public ResultSet executeQuery() throws SQLException {
        return new ResultSet(target.executeQuery());
    }


    public int executeUpdate() throws SQLException {
        return target.executeUpdate();
    }


    @Override
    public void close() throws SQLException  {
        target.close();
    }


    public void addBatch() throws SQLException {
        target.addBatch();
    }


    public int[] executeBatch() throws SQLException {
        return target.executeBatch();
    }

}
