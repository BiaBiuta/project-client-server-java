package org.example.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.AgeCategory;
import org.example.Sample;
import org.example.SampleCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SampleRepository implements ISampleRepository{
    private static final Logger logger= LogManager.getLogger();
    private JdbcUtils dbUtils;
    public SampleRepository(Properties props){
        logger.info("Initializing SampleRepository with properties: {}",props);
        dbUtils = new JdbcUtils(props);
    }
    @Override
    public Sample findOne(Integer integer) {
        logger.traceEntry();
        Connection con  = dbUtils.getConnection();
        Sample sample = null;
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from samples where id_sample=?")) {
            preparedStatement.setInt(1, integer);
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id_sample");
                    String name = result.getString("sample_categories");
                    String age = result.getString("age_category");
                    sample = new Sample(SampleCategory.fromString(name), AgeCategory.fromString(age));
                    sample.setId(id);
                    logger.trace("Found {} instances",sample);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
            logger.traceExit(sample);
            return sample;
        }
        logger.traceExit(sample);
        return sample;
    }

    public Sample findOneByCategoryAndAge(String category, String age_category) {
        logger.traceEntry();
        Connection con  = dbUtils.getConnection();
        Sample sample = null;
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from samples where sample_categories=? and age_category=?")) {
            preparedStatement.setString(1,category );
            preparedStatement.setString(2,age_category );
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id_sample");
                    String name = result.getString("sample_categories");
                    String age = result.getString("age_category");
                    sample = new Sample(SampleCategory.fromString(name), AgeCategory.fromString(age));
                    sample.setId(id);
                    logger.trace("Found {} instances",sample);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
            logger.traceExit(sample);
            return sample;
        }
        logger.traceExit(sample);
        return sample;
    }



    @Override
    public Iterable<Sample> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Sample> samples= new ArrayList<>();
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from samples")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id_sample");
                    String name = result.getString("sample_categories");
                    String age = result.getString("age_category");
                    Sample sample = new Sample(SampleCategory.fromString(name), AgeCategory.fromString(age));
                    sample.setId(id);
                    samples.add(sample);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
            logger.traceExit(samples);
            return samples;
        }
        logger.traceExit(samples);
        return samples;
    }

    @Override
    public Sample save(Sample entityForAdd) {
        return null;
    }
}
