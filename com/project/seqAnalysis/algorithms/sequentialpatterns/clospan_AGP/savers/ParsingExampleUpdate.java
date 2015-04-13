package com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.savers;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by astaputhra on 31/3/15.
 */
public class ParsingExampleUpdate {


    public static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    static final String USERNAME = "imdevelopment";
    static final String PASSWORD = "imdevelopment";
    public static final int NUM_FIELDS = 11;
    static int LINE_COUNTER = 0;

    public static void main(String argv[]) throws IOException {

        String logEntryPattern = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"(.*)\" \"([^\"]+)\" \"([^\"]+)\" \"([^\"]+)\"";
        FileInputStream fis = null;
        BufferedReader reader = null;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            fis = new FileInputStream("/home/astaputhra/Downloads/spmf/ca/pfv/spmf/algorithms/sequentialpatterns/clospan_AGP/savers/sample.log");
            fis = new FileInputStream(argv[0]);
            reader = new BufferedReader(new InputStreamReader(fis));
            //Class.forName("com.mysql.jdbc.Driver");
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            stmt = conn
                    .prepareStatement("INSERT INTO parsertable VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");

            System.out
                    .println("Reading File line by line using BufferedReader");

            String line = reader.readLine();
            Pattern p = Pattern.compile(logEntryPattern);
            while (line != null) {
                LINE_COUNTER++;
                if ((!line.contains("GET /sparql")) && (!line.contains("/snorql/")))

                {


                    System.out.println(line);
                    Matcher matcher = p.matcher(line);

                    if (!matcher.matches()
                            || NUM_FIELDS != matcher.groupCount()) {
                        System.err
                                .println("Bad log entry (or problem with RE?):");
                        System.err.println(matcher.groupCount());
                        return;
                    }


                    String request = matcher.group(5);
                    String[] requests = request.split(" ");
                    String[] dates = matcher.group(4).split(":", 2);
                    String[] requestNames = {"RequestType", "Request",
                            "Protocol"};
                    String[] dateNames = {"Date", "Time"};

                    System.out.println("IP Address: " + matcher.group(1));
                    for (int i = 0; i <= 1; i++) {
                        System.out.println(dateNames[i] + ": " + dates[i]);
                    }
                    for (int i = 0; i <= 2; i++) {
                        System.out
                                .println(requestNames[i] + ": " + requests[i]);
                    }

                    System.out.println("Response: " + matcher.group(6));
                    System.out.println("Bytes Sent: " + matcher.group(7));
                    if (!matcher.group(8).equals("-"))
                        System.out.println("Referer: " + matcher.group(8));
                    System.out.println("Browser: " + matcher.group(9));
                    System.out.println("Country:" + matcher.group(10));
                    System.out.println("HashCode of User:" + matcher.group(11));

                    if (!requests[1].endsWith("css")
                            && !requests[1].endsWith("gif")
                            && !requests[1].endsWith("png")
                            && !requests[1].endsWith("jpg")) {

                        // Adding values to prepared statement
                        stmt.setString(1, matcher.group(1));
                        stmt.setString(2, dates[0]);
                        stmt.setString(3, dates[1]);
                        stmt.setString(4, requests[0]);
                        stmt.setString(5, requests[1]);
                        stmt.setString(6, requests[2]);
                        stmt.setInt(7, new Integer(matcher.group(6)));
                        stmt.setInt(8, new Integer(matcher.group(7)));
                        stmt.setString(9, matcher.group(8));
                        stmt.setString(10, matcher.group(9));
                        stmt.setString(11, matcher.group(10));
                        stmt.setString(12, matcher.group(11));

                        stmt.executeUpdate();
                    }
                }
                line = reader.readLine();
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Counter:" + LINE_COUNTER);
                reader.close();
                fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

