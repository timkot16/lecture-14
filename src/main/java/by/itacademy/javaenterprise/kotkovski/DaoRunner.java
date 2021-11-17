package by.itacademy.javaenterprise.kotkovski;

import by.itacademy.javaenterprise.kotkovski.configuration.SpringConfig;
import by.itacademy.javaenterprise.kotkovski.dao.impl.CustomerRepository;
import by.itacademy.javaenterprise.kotkovski.entity.Customer;
import by.itacademy.javaenterprise.kotkovski.service.CarService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DaoRunner {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        CustomerRepository customerRepository = context.getBean("customerRepository", CustomerRepository.class);
        customerRepository.delete(14);
    }
}
