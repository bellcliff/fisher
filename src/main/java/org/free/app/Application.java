package org.free.app;

import org.free.ui.MainFrame;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "org.free")
public class Application {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class);
        builder.headless(false);
        ConfigurableApplicationContext ctx = builder.run(args);
        ctx.getBean(MainFrame.class).init();
    }

    @Bean
    MainFrame getMainFrame() {
        return new MainFrame();
    }
}