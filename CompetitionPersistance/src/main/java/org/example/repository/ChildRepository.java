package org.example.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Child;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


public class ChildRepository implements IChildRepository{
    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    public ChildRepository(Properties props){
        logger.info("Initializing ChildRepository with properties: {}",props);
        dbUtils = new JdbcUtils(props);
    }
    @Override
    public Child findOne(Integer integer) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Child child = null;
        try (PreparedStatement preparedStatement = con.prepareStatement("select * from children where id=?")) {
            preparedStatement.setInt(1, integer);
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    int age = result.getInt("age");
                    int number_of_samples = result.getInt("number_of_samples");
                    child = new Child(name, age);
                    child.setId(id);
                    child.setNumberOfSamples(number_of_samples);
                    logger.trace("Found {} instances", child);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
            logger.traceExit(child);
            return child;
        }
        logger.traceExit(child);
        return child;
    }

    @Override
    public Iterable<Child> findAll() {
        return null;
    }

    @Override
    public Child save(Child entityForAdd) {
        logger.traceEntry("saving task {} ",entityForAdd);
        Connection con =dbUtils.getConnection();
        try(PreparedStatement preparedStatement=con.prepareStatement("insert into children(age,name,number_of_samples) values(?,?,?)")){
            preparedStatement.setInt(1,entityForAdd.getAge());
            preparedStatement.setString(2,entityForAdd.getName());
            preparedStatement.setInt(3,entityForAdd.getNumberOfSamples());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                try (Statement statement = con.createStatement()) {
                    try (ResultSet resultSet = statement.executeQuery("SELECT last_insert_rowid()")) {
                        if (resultSet.next()) {
                            int id = resultSet.getInt(1);
                            entityForAdd.setId(id);
                        } else {
                            logger.error("Nu s-au găsit chei generate pentru inserarea entității");
                        }
                    }
                }
            } else {
                logger.error("Inserarea entității a eșuat");
            }
            logger.trace("Saved {} instances", rowsAffected);


        }catch (Exception e){
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
        return entityForAdd;
    }

    @Override
    public Child update(Child entityForUpdate) {
        logger.traceEntry("update task {} ",entityForUpdate);
        Connection con =dbUtils.getConnection();
        try(PreparedStatement preparedStatement=con.prepareStatement("update children set number_of_samples=? where id=?")){
            preparedStatement.setInt(1,entityForUpdate.getNumberOfSamples());
            preparedStatement.setInt(2,entityForUpdate.getId());
            int result = preparedStatement.executeUpdate();
            logger.trace("Update {} instances",result);
        }catch (Exception e){
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
        return entityForUpdate;
    }

    @Override
    public Child findByName(String name) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Child child = null;
        try (PreparedStatement preparedStatement = con.prepareStatement("select * from children where name=?")) {
            preparedStatement.setString(1, name);
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String name1 = result.getString("name");
                    int age = result.getInt("age");
                    int number_of_samples = result.getInt("number_of_samples");
                    child = new Child(name1, age);
                    child.setNumberOfSamples(number_of_samples);
                    child.setId(id);
                    logger.trace("Found {} instances", child);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
            logger.traceExit(child);
            return child;
        }
        logger.traceExit(child);
        return child;
    }
}
