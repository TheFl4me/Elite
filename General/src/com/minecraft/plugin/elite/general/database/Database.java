package com.minecraft.plugin.elite.general.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Database {

    private DatabaseCore core;

    public Database(DatabaseCore core) throws ConnectionException {
        try {
            try {
                if (!core.getConnection().isValid(10)) {
                    throw new ConnectionException("Database does not appear to be valid!");
                }
            } catch (AbstractMethodError e) {
                //You don't need to validate this core.
            }
        } catch (SQLException e) {
            throw new ConnectionException(e.getMessage());
        }
        this.core = core;
    }

    public DatabaseCore getCore() {
        return this.core;
    }

    public Connection getConnection() {
        return this.core.getConnection();
    }

    public void execute(String query, Object... objs) {
        BufferStatement bs = new BufferStatement(query, objs);
        this.core.queue(bs);
    }

    public boolean hasTable(String table) throws SQLException {
        ResultSet rs = getConnection().getMetaData().getTables(null, null, "%", null);
        while (rs.next()) {
            if (table.equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                rs.close();
                return true;
            }
        }
        rs.close();
        return false;
    }

    public boolean hasColumn(String table, String column) throws SQLException {
        if (!hasTable(table))
            return false;

        DatabaseMetaData md = this.getConnection().getMetaData();
        ResultSet rs = md.getColumns(null, null, table, column);
        return rs.next();
    }

    public static class ConnectionException extends Exception {
        private static final long serialVersionUID = 8348749992936357317L;

        public ConnectionException(String msg) {
            super(msg);
        }
    }

    public boolean containsValue(String table, String column, String value) {
        ResultSet res;
        try {
            res = this.select(table, column, value);
            while (res.next()) {
                if (res.getString(column).equalsIgnoreCase(value)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createTable(String query, String table) {
        try {
            Statement st = this.getConnection().createStatement();
            st.execute(query);
            System.out.println("Table " + table + " has been created");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not create table " + table);
        }

    }

    public ResultSet select(String table, String where, String whereValue) {
        try {
            return this.getConnection().createStatement().executeQuery("SELECT * FROM " + table + " WHERE " + where + " = '" + whereValue + "';");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet select(String table) {
        try {
            return this.getConnection().createStatement().executeQuery("SELECT * FROM " + table + ";");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(String table, String column, Object columnValue, String where, Object whereValue) {
        this.execute("UPDATE " + table + " SET " + column + " = ? WHERE " + where + " = ?;", columnValue, whereValue);
    }

    public void delete(String table, String where, Object whereValue) {
        this.execute("DELETE FROM " + table + " WHERE " + where + " = ?;", whereValue);
    }
}