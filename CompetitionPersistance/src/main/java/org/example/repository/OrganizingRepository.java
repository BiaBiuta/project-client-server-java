package org.example.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Organizing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OrganizingRepository implements IOrganizationRepository{
    private JdbcUtils dbUtils;
    private  static  final Logger logger = LogManager.getLogger();
    public OrganizingRepository(Properties props){
        logger.info("Initializing OrganizingRepository with properties: {}",props);
        dbUtils = new JdbcUtils(props);
    }
    @Override
    public Organizing findByName(String username, String password) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Organizing org = null;
        try (PreparedStatement preStm = con.prepareStatement("select * from organizings where username=? and password=?")) {
            preStm.setString(1, username);
            preStm.setString(2, password);
            try (ResultSet result = preStm.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id_organization");
                    String name = result.getString("name");
                    String usernameToIntroduce = result.getString("username");
                    String passwordToIntroduce = result.getString("password");
                    org = new Organizing(name, usernameToIntroduce, passwordToIntroduce);
                    org.setId(id);
                    logger.trace("Found {} instances", org);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
            logger.traceExit(org);
            return org;
        }
        logger.traceExit(org);
        return org;
    }

    @Override
    public Organizing findOne(Integer integer) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Organizing org = null;
        try (PreparedStatement prepareStatement = con.prepareStatement("select * from organizings where id_organization=?")) {
            prepareStatement.setInt(1, integer);
            try (ResultSet result = prepareStatement.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id_organization");
                    String name = result.getString("name");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    org = new Organizing(name, username, password);
                    org.setId(id);
                    logger.trace("Found {} instances", org);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
            logger.traceExit(org);
            return org;
        }
        logger.traceExit(org);
        return org;
    }

    @Override
    public Iterable<Organizing> findAll() {
        logger.traceEntry();
        Connection con =dbUtils.getConnection();
        List<Organizing> orgs = new ArrayList<>();
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from organizings")){
            try(ResultSet result=preparedStatement.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id_organization");
                    String name = result.getString("name");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    Organizing org = new Organizing(name, username, password);
                    org.setId(id);
                    orgs.add(org);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
            logger.traceExit(orgs);
            return orgs;
        }
        logger.traceExit(orgs);
        return orgs ;
    }

    @Override
    public Organizing save(Organizing entityForAdd) {
        return null;
    }
}
