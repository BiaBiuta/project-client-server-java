package org.example.repository;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JdbcUtils {
    private Properties jbdcProps;

    private static  final Logger logger = LogManager.getLogger();
    public  JdbcUtils(Properties props){
        jbdcProps = props;
    }
    private Connection instance = null;
    private Connection getNewConnection(){
        logger.traceEntry();
        String url = jbdcProps.getProperty("competition.jdbc.url");
        String user = jbdcProps.getProperty("competition.jdbc.url");
        String pass = jbdcProps.getProperty("competition.jdbc.url");
        logger.info("trying to connect to database ... {}",url);
        logger.info("user: {}",user);
        logger.info("pass: {}", pass);
        Connection con = null;
        try {
            if (user!=null && pass!=null)
                con= DriverManager.getConnection(url,user,pass);
            else
                con=DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection "+e);
        }
        return con;
    }
    public Connection getConnection(){
        logger.traceEntry();
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(instance);
        return instance;
    }

}
