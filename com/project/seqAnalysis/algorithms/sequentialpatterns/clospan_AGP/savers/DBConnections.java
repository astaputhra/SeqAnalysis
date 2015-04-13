package com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.savers;

import com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.AlgoCloSpan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by astaputhra on 1/4/15.
 */
public class DBConnections {

    public static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    static final String USERNAME = "imdevelopment";
    static final String PASSWORD = "imdevelopment";
    public static final int NUM_FIELDS = 11;
    static int LINE_COUNTER = 0;

    public void insert(AlgoCloSpan algoCloSpan)  {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            long difftime = algoCloSpan.getOverallStart()-algoCloSpan.getOverallEnd();
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            String query = "INSERT INTO GRAPHOUTPUT values(?,?, ?,?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, String.valueOf(algoCloSpan.getMinSupRelative())); // set input parameter 1
            pstmt.setString(2, String.valueOf(difftime)); // set input parameter 2
            pstmt.setString(3, algoCloSpan.getAlgorithmName()); // set input parameter 3
            pstmt.setString(4, "SPMF");
            pstmt.executeUpdate(); // execute insert statement
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public GraphOutput selectMethod(List<GraphOutput> graphOutputs){
        GraphOutput graphOutput = new GraphOutput();
        Connection conn = null;
        List<RequestObject> requestObject = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            String query = "SELECT type,time, remarks, minSup FROM GRAPHOUTPUT";
            Statement pstmt = conn.createStatement();
            ResultSet rs = pstmt.executeQuery(query);
            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                graphOutput.setType(rs.getString("type"));
                graphOutput.setTime(rs.getString("time"));
                graphOutput.setRemarks(rs.getString("remarks"));
                graphOutput.setMinSup(rs.getInt("minSup"));
                graphOutputs.add(graphOutput);

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return graphOutput;
    }
}
