package by.itacademy.javaenterprise.kotkovski;

import by.itacademy.javaenterprise.kotkovski.configuration.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DaoRunner {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }
}
