/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.jdbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import util.resc.ResourceService;





public class ConnectionService implements ResourceService<Connection> {
    private final String path;
    private final String user;
    private final String pass;


    public ConnectionService(String path, String user, String pass) {
        this.path = path;
        this.user = user;
        this.pass = pass;
    }


    @Override
    public Connection create() throws SQLException {
        return DriverManager.getConnection(path, user, pass);
    }


    @Override
    public void reset(Connection resource) throws SQLException {
        resource.clearWarnings();
        resource.setAutoCommit(true);
    }


    @Override
    public void close(Connection resource) throws SQLException {
        resource.close();
    }

}
