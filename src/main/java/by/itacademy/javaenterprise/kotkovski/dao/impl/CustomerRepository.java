package by.itacademy.javaenterprise.kotkovski.dao.impl;

import by.itacademy.javaenterprise.kotkovski.dao.CrudRepository;
import by.itacademy.javaenterprise.kotkovski.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomerRepository implements CrudRepository<Integer, Customer> {
    private final static Logger logger = LoggerFactory.getLogger(CustomerRepository.class);

    private final static String SAVE = "insert into Customer(first_name, last_name, tel_number) values(?, ?, ?)";
    private final static String UPDATE = "update Customer set first_name = ?, last_name = ?, tel_number = ? where id = ?";
    private final static String DELETE = "delete from Customer where id = ?";
    private final static String FIND_ALL_SQl = "select id, first_name, last_name, tel_number from Customer";
    private final static String FIND_BY_ID_SQL = FIND_ALL_SQl + " where id = ?";

    @Autowired
    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private JdbcTemplate jdbcTemplate;

    public CustomerRepository() {
    }

    @Override
    public Customer save(Customer customer) {
        jdbcTemplate.update(SAVE, customer.getFirstName(), customer.getLastName(), customer.getTelNumber());
        return customer;
    }

    @Override
    public void update(Integer id, Customer customer) {
        jdbcTemplate.update(UPDATE, customer.getFirstName(), customer.getLastName(), customer.getTelNumber(), id);
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            FIND_BY_ID_SQL,
                            new Object[]{id},
                            new BeanPropertyRowMapper<>(Customer.class)));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQl, new BeanPropertyRowMapper<>(Customer.class));
    }
}
