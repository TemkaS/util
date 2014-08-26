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





public class StatementHolder implements AutoCloseable {
    private final PreparedStatement statement;


    public StatementHolder(PreparedStatement statement) {
        this.statement = statement;
    }


    public PreparedStatement getStatement() {
        return statement;
    }


    public void setBoolean(int index, Boolean value) throws SQLException {
        if (value != null) {
            statement.setInt(index, value ? 1 : 0);
        } else {
            statement.setNull(index, Types.INTEGER);
        }
    }


    public void setBoolean(int index, boolean value) throws SQLException {
        statement.setInt(index, value ? 1 : 0);
    }


    public void setInteger(int index, Integer value) throws SQLException {
        if (value != null) {
            statement.setInt(index, value.intValue());
        } else {
            statement.setNull(index, Types.INTEGER);
        }
    }


    public void setInteger(int index, int value) throws SQLException {
        statement.setInt(index, value);
    }


    public void setLong(int index, Long value) throws SQLException {
        if (value != null) {
            statement.setLong(index, value.longValue());
        } else {
            statement.setNull(index, Types.BIGINT);
        }
    }


    public void setLong(int index, long value) throws SQLException {
        statement.setLong(index, value);
    }


    public void setDouble(int index, Double value) throws SQLException {
        if (value != null) {
            statement.setDouble(index, value.doubleValue());
        } else {
            statement.setNull(index, Types.DOUBLE);
        }
    }


    public void setDouble(int index, double value) throws SQLException {
        statement.setDouble(index, value);
    }


    public void setDate(int index, Date value) throws SQLException {
        if (value != null) {
            statement.setTimestamp(index, new Timestamp(value.getTime()));
        } else {
            statement.setNull(index, Types.TIMESTAMP);
        }
    }


    public void setBigDecimal(int index, BigDecimal value) throws SQLException {
        if (value != null) {
            statement.setBigDecimal(index, value);
        } else {
            statement.setNull(index, Types.NUMERIC);
        }
    }


    public void setString(int index, String value) throws SQLException {
        if (value != null) {
            statement.setString(index, value);
        } else {
            statement.setNull(index, Types.VARCHAR);
        }
    }


    public void setArray(int index, Array value) throws SQLException {
        if (value != null) {
            statement.setArray(index, value);
        } else {
            statement.setNull(index, Types.ARRAY);
        }
    }


    public void setStruct(int index, Struct value) throws SQLException {
        if (value != null) {
            statement.setObject(index, value, Types.STRUCT);
        } else {
            statement.setNull(index, Types.STRUCT);
        }
    }


    public void setClob(int index, Clob value) throws SQLException {
        if (value != null) {
            statement.setClob(index, value);
        } else {
            statement.setNull(index, Types.CLOB);
        }
    }


    public void setBlob(int index, Blob value) throws SQLException {
        if (value != null) {
            statement.setBlob(index, value);
        } else {
            statement.setNull(index, Types.BLOB);
        }
    }


    public ResultSetHolder executeQuery() throws SQLException {
        return new ResultSetHolder(statement.executeQuery());
    }


    public int executeUpdate() throws SQLException {
        return statement.executeUpdate();
    }


    @Override
    public void close() throws SQLException  {
        statement.close();
    }

}
