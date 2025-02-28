package com.example.Dunzo_2023.vmm;

import java.sql.*;

public class DBLoader 
{
    public static ResultSet executeSQL(String sql) throws Exception
    {
        // Set the library path to where the sqljdbc_auth.dll is located
System.setProperty("java.library.path", "\"C:\\Program Files\\Java\\jdk-20\\bin\\ mssql-jdbc_auth-11.2.0.x64.dll\"");

// Load the SQL Server JDBC driver
Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        // Loading the SQL Server JDBC driver
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        System.out.println("SQL Server Driver loaded");

        // Establishing connection using Windows Authentication and the correct connection string
        String url = "jdbc:sqlserver://DESKTOP-BBDKPRM\\SQLEXPRESS;databaseName=talent_project;integratedSecurity=true;encrypt=false;";

        Connection conn = DriverManager.getConnection(url);
        System.out.println("Connection established");

        // Creating a Statement to execute SQL queries
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 
                                              ResultSet.CONCUR_UPDATABLE);
        System.out.println("Statement created");

        // Executing the query to fetch Name and Message columns from the feedback table
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("ResultSet created");

        // Return the result set to be processed later
        return rs;
    }
}