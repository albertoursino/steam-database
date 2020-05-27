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
                "\tWHERE game_id = %2$s", 2),
        QUERY2("SELECT game_id, nome, round(AVG(voto),2) as mediaVoti\n" +
                "FROM GIOCO NATURAL JOIN Recensisce\n" +
                "GROUP BY game_id\n" +
                "ORDER BY mediaVoti DESC\n" +
                "LIMIT 10;",0),
        QUERY3("DROP VIEW IF EXISTS NUM_DOWNLOAD;\n" +
                "\n" +
                "CREATE VIEW NUM_DOWNLOAD AS\n" +
                "\tSELECT game_id, nome, count(*) AS num_download\n" +
                "\tFROM Libreria NATURAL JOIN GIOCO\n" +
                "\tGROUP BY game_id, nome;\n" +
                "\n" +
                "SELECT game_id, nome, num_download\n" +
                "FROM GIOCO NATURAL JOIN NUM_DOWNLOAD\n" +
                "ORDER BY num_download DESC\n" +
                "LIMIT 10;",0),
        QUERY5("DROP VIEW IF EXISTS TAGS;\n" +
                "DROP VIEW IF EXISTS GIOCHI_POSSEDUTI; \n" +
                "DROP VIEW IF EXISTS NUM_DOWNLOAD;\n" +
                "\n" +
                "CREATE VIEW NUM_DOWNLOAD AS\n" +
                "\tSELECT game_id, nome, count(*) AS num_download\n" +
                "\tFROM Libreria NATURAL JOIN GIOCO\n" +
                "\tGROUP BY game_id, nome;\n" +
                "\n" +
                "CREATE VIEW GIOCHI_POSSEDUTI AS\n" +
                "\tSELECT game_id, ore_gioco\n" +
                "\tFROM Libreria\n" +
                "\tWHERE steam_id = %1$s;\n" +
                "\t\n" +
                "CREATE VIEW TAGS AS\n" +
                "\tSELECT genere \n" +
                "\tFROM Tag NATURAL JOIN ( \n" +
                "\t\tSELECT game_id\n" +
                "\t    FROM GIOCHI_POSSEDUTI \n" +
                "\t    WHERE ore_gioco = ( SELECT MAX(ore_gioco) FROM GIOCHI_POSSEDUTI )) AS X;\n" +
                "\n" +
                "SELECT game_id, nome, generi_in_comune, num_download\n" +
                "FROM ( SELECT game_id, count(*) AS generi_in_comune\n" +
                "       FROM Tag NATURAL JOIN (\n" +
                "\t\t   SELECT game_id \n" +
                "\t\t   FROM GIOCO EXCEPT (SELECT game_id FROM GIOCHI_POSSEDUTI)) AS X\n" +
                "\t   WHERE genere IN ( SELECT genere FROM TAGS )\n" +
                "\t   GROUP BY game_id ) AS Y \n" +
                "\t   NATURAL JOIN NUM_DOWNLOAD\n" +
                "ORDER BY generi_in_comune DESC, num_download DESC\n" +
                "LIMIT 5;",1),
        QUERY6("DROP VIEW IF EXISTS FRIENDS;\n" +
                "\n" +
                "CREATE VIEW FRIENDS AS\n" +
                "\tSELECT (CASE\n" +
                "\t\t   \tWHEN utente1 = %1$s THEN utente2\n" +
                "\t\t\tWHEN utente2 = %1$s THEN utente1\t   \n" +
                "\t\t    END) as steam_id\n" +
                "\tFROM amicodi\n" +
                "\tWHERE utente1 = %1$s or utente2 = %1$s;\n" +
                "\n" +
                "SELECT game_id, nome, prezzo\n" +
                "FROM (FRIENDS NATURAL JOIN listadesideri) NATURAL JOIN GIOCO \n" +
                "WHERE prezzo <= (SELECT saldo_steam FROM PORTAFOGLIO WHERE steam_id = %1$s);",1),
        QUERY7("SELECT id_oggetto, COUNT(*) AS num_vendite\n" +
                "FROM VENDITA\n" +
                "WHERE id_oggetto = %1$s AND \n" +
                "\t(SELECT NOW()::DATE) - data_vendita <= %2$s\n" +
                "GROUP BY id_oggetto;",2),
        QUERY8("SELECT id_oggetto, data_vendita, prezzo, game_id, nome\n" +
                "FROM (vendita NATURAL LEFT JOIN OG) AS X NATURAL LEFT JOIN GIOCO\n" +
                "WHERE (acquirente = %1$s AND venditore = %2$s) OR (acquirente = %2$s AND venditore = %1$s)\n" +
                "ORDER BY data_vendita;",2),
        QUERY9("SELECT game_id, id_oggetto, medaglia\n" +
                "FROM OG NATURAL JOIN MO\n" +
                "WHERE game_id IN (SELECT game_id\n" +
                "\t\t\t\t  FROM Libreria\n" +
                "\t\t\t\t  WHERE steam_id = %1$s)\n" +
                "EXCEPT \n" +
                "SELECT game_id, id_oggetto, medaglia\n" +
                "FROM Possiede NATURAL JOIN MO NATURAL JOIN OG\n" +
                "WHERE steam_id = %1$s\n" +
                "ORDER BY game_id;",1);


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
