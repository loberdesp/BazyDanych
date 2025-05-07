package com.northpole.snow;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Clock;

@SpringBootApplication
@EnableJpaRepositories("com.northpole.snow.todo.domain")
@EntityScan("com.northpole.snow.todo.domain")
@Theme("default")
public class Application implements AppShellConfigurator {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone(); // You can also use Clock.systemUTC()
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
