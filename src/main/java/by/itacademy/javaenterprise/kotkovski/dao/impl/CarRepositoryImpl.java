package by.itacademy.javaenterprise.kotkovski.dao.impl;

import by.itacademy.javaenterprise.kotkovski.dao.CarRepository;
import by.itacademy.javaenterprise.kotkovski.entity.Car;
import by.itacademy.javaenterprise.kotkovski.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CarRepositoryImpl implements CarRepository {

    private final static Logger logger = LoggerFactory.getLogger(CarRepositoryImpl.class);

    private final static String FIND_ALL_SQl = "select id, car, vin, number, year, customer_id from Cars";
    private final static String FIND_BY_ID_SQL = FIND_ALL_SQl + " where id = ?";
    private final static String FIND_BY_CUSTOMER_ID = "select id, car, vin, number, year from Cars where customer_id = ?";
    private final static String DELETE = "delete from Cars where id = ?";
    private final static String SAVE = "insert into Cars(car, vin, number, year) values(?, ?, ?, ?)";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomerRepositoryImpl customerRepository;

    @Override
    public void delete(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Failed to delete car with current id {}", id, e);
            throw new DAOException(e);
        }
    }

    @Override
    public Car save(Car car) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE)) {
            connection.setAutoCommit(false);
            statement.setString(1, car.getCar());
            statement.setString(2, car.getVin());
            statement.setString(3, car.getNumber());
            statement.setInt(4, car.getYear());
            statement.executeUpdate();
            connection.commit();
            return car;
        } catch (SQLException e) {
            logger.error("Failed to save {}", car, e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Car> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQl)) {
            ResultSet rs = preparedStatement.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (rs.next()) {
                cars.add(buildCar(rs));
            }
            return cars;
        } catch (SQLException e) {
            logger.error("Failed to find all cars", e);
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Car> findById(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, id);
            connection.setAutoCommit(false);
            ResultSet resultSet = statement.executeQuery();
            Car car = null;
            if (resultSet.next()) {
                car = buildCar(resultSet);
            }
            connection.commit();
            return Optional.ofNullable(car);
        } catch (SQLException e) {
            logger.error("Failed to find car with current id {}", id, e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Car> findByCustomerId(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CUSTOMER_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<Car> carList = new ArrayList<>();
            while (resultSet.next()) {
                carList.add(buildCar(resultSet));
            }
            return carList;
        } catch (SQLException e) {
            logger.error("Failed to find cars with current customer id {}", id, e);
            throw new DAOException(e);
        }
    }

    private Car buildCar(ResultSet resultSet) throws SQLException {
        return new Car(
                resultSet.getInt("id"),
                resultSet.getString("car"),
                resultSet.getString("vin"),
                resultSet.getString("number"),
                resultSet.getInt("year"),
                customerRepository.findById(resultSet.getInt("id")).orElse(null)
        );
    }
}
