package tradingapplication;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class H2jdbcInsert {

    // API connection & extraction
    private static String[] compNameAndPrice;
    private static ConnectionToAPI apiConn = new ConnectionToAPI();
    private static List<String> importedSymbols = ImportExcel.importSymbolsFromExcel(TradingApplication.path2);

    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; //for H2 is "org.h2.Driver"
    private static final String DB_URL = "jdbc:mysql://localhost:3306/apiextractor"; //for H2 is "jdbc:h2:file:C:/Users/kdren/IdeaProjects/demo-rest-api/DB_OUT"; //change to your DB_OUT path

    //  Database credentials
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        int counter = 0;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // STEP 2: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            while(counter < importedSymbols.size()){
                // STEP 3: Execute a query
                compNameAndPrice = apiConn.extractPrices(importedSymbols.get(0));
                preparedStatement = conn.prepareStatement("INSERT INTO prices (COMPANYNAME, COMPANYPRICE) " + "VALUES (?,?)");
                preparedStatement.setString(1, compNameAndPrice[0]);
                System.out.println("COMPANYNAME added: " + compNameAndPrice[0]);
                preparedStatement.setString(2, compNameAndPrice[1]);
                System.out.println("COMPANYPRICE added: " + compNameAndPrice[1]);
                preparedStatement.executeUpdate();
                System.out.println("Records inserted into table!");
                importedSymbols.remove(0);
                counter++;
            }

            // STEP 4: Clean-up environment
            preparedStatement.close();
            conn.close();
        } catch (Exception se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } finally {
            // finally block used to close resources
            if (preparedStatement != null) preparedStatement.close();
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try
        System.out.println("Goodbye!");
    }
}