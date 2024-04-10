package org.example.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Child;
import org.example.Registration;
import org.example.Sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RegistrationRepository implements IRegistration {
    public JdbcUtils jdbcUtils;
    public static final Logger logger = LogManager.getLogger();
    private IRepository<Integer, Child> childRepository;
    private IRepository<Integer, Sample> sampleRepository;

    public RegistrationRepository(Properties props, IRepository<Integer,Child> childRepository, IRepository<Integer,Sample> sampleRepository) {
        logger.info("Initializing RegistrationRepository with properties: {}", props);
        jdbcUtils = new JdbcUtils(props);
        this.childRepository = childRepository;
        this.sampleRepository = sampleRepository;
    }

    @Override
    public List<Child> listChildrenForSample(Sample sample) {
       logger.traceEntry();
       Connection con = jdbcUtils.getConnection();
       List<Child> children = new ArrayList<>();
         try(PreparedStatement preparedStatement=con.prepareStatement("select * from registration as r  inner join children as c on r.id_child=c.id where id_sample=?")) {
              preparedStatement.setInt(1, sample.getId());
              try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                     int idChild = result.getInt("id");
                     int age = result.getInt("age");
                     String name = result.getString("name");
                     int number_of_samples = result.getInt("number_of_samples");
                     Child child = new Child(name, age);
                     child.setId(idChild);
                     child.setNumberOfSamples(number_of_samples);
                     children.add(child);
                }
              }
         } catch (Exception e) {
              logger.error(e);
              System.out.println("Error DB " + e);
              logger.traceExit(children);
              return children;
         }
            logger.traceExit(children);
            return children;
    }

    @Override
    public int numberOfChildrenForSample(Sample sample) {
        logger.traceEntry();
        Connection con = jdbcUtils.getConnection();
        int number=0;
        try(PreparedStatement preparedStatement=con.prepareStatement("select count(*) as number_children  from registration where id_sample=?")) {
            preparedStatement.setInt(1, sample.getId());
            try (ResultSet result = preparedStatement.executeQuery()) {
                    number = result.getInt("number_children");
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
            logger.traceExit(number);
            return number;
        }
        logger.traceExit(number);
        return number;
    }

    @Override
    public Registration findOneByChildAndSample(Integer child, Integer sample) {
        logger.traceEntry();
        Connection con = jdbcUtils.getConnection();
        Registration reg = null;
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from registration where id_child=? and id_sample=?")) {
            preparedStatement.setInt(1, child);
            preparedStatement.setInt(2, sample);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id_registration");
                    int idChild = result.getInt("id_child");
                    int idSample = result.getInt("id_sample");
                    Child child1 = childRepository.findOne(idChild);
                    Sample sample2 = sampleRepository.findOne(idSample);
                    reg= new Registration( child1, sample2);
                    reg.setId(id);
                    logger.trace("Found {} instances", reg);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
            logger.traceExit(reg);
            return reg;
        }
        logger.traceExit(reg);
        return reg;
    }

    @Override
    public Registration findOne(Integer integer) {
        logger.traceEntry();
        Connection con = jdbcUtils.getConnection();
        Registration reg = null;
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from registration where id_registration=?")) {
            preparedStatement.setInt(1, integer);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id_registration");
                    int idChild = result.getInt("id_child");
                    int idSample = result.getInt("id_sample");
                    Child child = childRepository.findOne(idChild);
                    Sample sample = sampleRepository.findOne(idSample);
                    reg= new Registration( child, sample);
                    reg.setId(id);
                    logger.trace("Found {} instances", reg);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
            logger.traceExit(reg);
            return reg;
        }
        logger.traceExit(reg);
        return reg;
    }

    @Override
    public Iterable<Registration> findAll() {
        logger.traceEntry();
        Connection con = jdbcUtils.getConnection();
        List<Registration> regs = new ArrayList<>();
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from registration")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id_registration");
                    int idChild = result.getInt("id_child");
                    int idSample = result.getInt("id_sample");
                    Child child = childRepository.findOne(idChild);
                    Sample sample = sampleRepository.findOne(idSample);
                    Registration reg= new Registration( child, sample);
                    reg.setId(id);
                    regs.add(reg);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
            logger.traceExit(regs);
            return regs;
        }
        logger.traceExit(regs);
        return regs;
    }
    //o sa fac in service sa dau save la registration doar daca numarul de sample la copil este mai mic decat 2
    //intreb daca e ok sa fac update si sa primesc asa un mesaj de eroare,pentru ca primesc eroare de la db
    //sau daca e ok sa fac un findone si sa verific cate sampleluri are asociate
    //am introdus un camp in registration care sa tina cont de numarul de sampleuri asociate unui copil ca sa nu ma mai conectez de mai multe ori la baza de date
    //asa doar caut daca un copil exista si atunci nu il mai adaug si ii spun direct daca are voie sa se mai inscrie
    @Override
    public Registration save(Registration entityForAdd) {
        logger.traceEntry();
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=con.prepareStatement("insert into registration(id_child,id_sample) values(?,?)")) {
            preparedStatement.setInt(1, entityForAdd.getChild().getId());
            preparedStatement.setInt(2, entityForAdd.getSample().getId());
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
        }
        catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
            logger.traceExit();
           return entityForAdd;
        }
        return entityForAdd;
    }
}
