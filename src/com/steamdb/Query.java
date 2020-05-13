package com.steamdb;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Query {


    /**
     * Enumeration of implemented queries. Each new query ready to be displayed in {@code QueryWindow} should be
     * defined here, along with its sql code and number of parameters. No other code has to be written,
     * except for the form in queryWin.form.
     * */
    public enum implementedQueries {
        QUERY4("SELECT game_id, ottenuti/totali * 100 AS percentuale_completamento " +
                "FROM ( SELECT game_id, count(*) AS totali " +
                "FROM ACHIEVEMENT NATURAL JOIN ( " +
                "SELECT game_id " +
                "FROM Libreria " +
                "WHERE steam_id = %1$s ) AS X " +
                "GROUP BY game_id ) AS Y NATURAL JOIN " +
                "(SELECT game_id, count(*) AS ottenuti " +
                "FROM Guadagna NATURAL JOIN UTENTE " +
                "WHERE steam_id = %1$s " +
                "GROUP BY game_id ) AS Z;", 1);

        static final int count = implementedQueries.values().length;
        final String sql;
        final int params;

        implementedQueries(String str, int noParams){
            sql = str;
            params = noParams;
        }
    };

    private final String SQLQuery;
    String[] columns = new String[0];

    final ArrayList<Object[]> result = new ArrayList<>();
    String error = "";

    /**
     * Constructor.
     * @param query number as specified in the enum,
     *              which is implemented and ready to be displayed in {@code QueryWindow}
     * */
    public Query(implementedQueries query){
        this(query.sql);
    }

    /**
     * Constructor. This query isn't supposed to be displayed in  {@code QueryWindow}
     * @param SQLQueryStr sql code
     * */
    public Query(String SQLQueryStr){
        SQLQuery = SQLQueryStr;
    }

    /**
     * Execute the query, saving the result set in {@code result}.
     * If an error occurs while accessing the database, save the error string in {@code error}, then return error code.
     * @param userParams an array of parameters for the query, chosen by the final user before search
     * @return 0 if no error occurs, otherwise -1
     * */
    public int execute(Object[] userParams){
        error = "";
        result.clear();
        try (Statement stm = SteamApp.connection.createStatement()) {
            ResultSet rs = stm.executeQuery(String.format(SQLQuery, userParams));
            setColumns(rs.getMetaData());
            while (rs.next()) {
                System.out.println("record found"); //TODO
                result.add(getRecord(rs));
            }
        } catch (SQLException e) {
            result.clear();
            error = e.getMessage();
            return -1;
        } catch (NullPointerException e){
            result.clear();
            error = "L'oggetto connection è null!";
            return -1;
        }
        return 0;
    }

    /**
     * Get the current row of data from the result set of the query as an array of Objects.
     * */
    private Object[] getRecord(ResultSet rs) throws SQLException{
        Object[] row = new Object[columns.length];
        for(int ic = 0; ic < columns.length; ic++)
            row[ic] = rs.getObject(ic);
        return row;
    }

    /**
     * Set the column labels to be retrieved by this query.
     * */
    private void setColumns(ResultSetMetaData data) throws SQLException{
        columns = new String[data.getColumnCount()];
        for(int ic = 0; ic < columns.length; ic++)
            columns[ic] = data.getColumnLabel(ic);
    }
}
