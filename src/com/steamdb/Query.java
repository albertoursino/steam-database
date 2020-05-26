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
     */
    public enum implementedQueries {
        QUERY4("SELECT game_id, nome, round(ottenuti::numeric/totali, 2) * 100 AS percentuale_completamento\n" +
                "FROM (( SELECT game_id, count(*) AS totali\n" +
                "\t   FROM ACHIEVEMENT NATURAL JOIN ( \n" +
                "\t   \t\tSELECT game_id\n" +
                "\t   \t\tFROM Libreria\n" +
                "\t   \t\tWHERE steam_id = %1$s ) AS X\n" +
                "\t   GROUP BY game_id ) AS Y NATURAL JOIN \n" +
                "\t ( SELECT game_id, count(*) AS ottenuti\n" +
                "\t   FROM Guadagna\n" +
                "\t   WHERE steam_id = %1$s\n" +
                "\t   GROUP BY game_id ) AS Z) NATURAL JOIN GIOCO;", 1),
        QUERY1("SELECT steam_id, username FROM(\n" +
                "\tSELECT (CASE\n" +
                "\t\t   \tWHEN utente1 = %1$s THEN utente2\n" +
                "\t\t\tWHEN utente2 = %1$s THEN utente1\t   \n" +
                "\t\t    END) as steam_id\n" +
                "\tFROM amicodi\n" +
                "\tWHERE utente1 = %1$s or utente2 = %1$s) AS X\n" +
                "\tNATURAL JOIN Libreria NATURAL JOIN UTENTE\n" +
                "\tWHERE game_id = %1$s", 2);


        static final int count = implementedQueries.values().length;
        final String sql;
        final int params;

        implementedQueries(String str, int noParams) {
            sql = str;
            params = noParams;
        }
    }

    ;

    private final String SQLQuery;
    String[] columns = new String[0];

    final ArrayList<Object[]> result = new ArrayList<>();
    String error = "";

    /**
     * Constructor.
     *
     * @param query number as specified in the enum,
     *              which is implemented and ready to be displayed in {@code QueryWindow}
     */
    public Query(implementedQueries query) {
        this(query.sql);
    }

    /**
     * Constructor. This query isn't supposed to be displayed in  {@code QueryWindow}
     *
     * @param SQLQueryStr sql code
     */
    public Query(String SQLQueryStr) {
        SQLQuery = SQLQueryStr;
    }

    /**
     * Execute the query, saving the result set in {@code result}.
     * If an error occurs while accessing the database, save the error string in {@code error}, then return error code.
     *
     * @param userParams an array of parameters for the query, chosen by the final user before search
     * @return 0 if no error occurs, otherwise -1
     */
    public int execute(Object[] userParams) {
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
        } catch (NullPointerException e) {
            result.clear();
            error = "L'oggetto connection è null!";
            return -1;
        }
        return 0;
    }

    /**
     * Get the current row of data from the result set of the query as an array of Objects.
     */
    private Object[] getRecord(ResultSet rs) throws SQLException {
        Object[] row = new Object[columns.length];
        for (int ic = 0; ic < columns.length; ic++)
            row[ic] = rs.getObject(ic + 1);
        return row;
    }

    /**
     * Set the column labels to be retrieved by this query.
     */
    private void setColumns(ResultSetMetaData data) throws SQLException {
        columns = new String[data.getColumnCount()];
        for (int ic = 0; ic < columns.length; ic++)
            columns[ic] = data.getColumnLabel(ic + 1);
    }
}
