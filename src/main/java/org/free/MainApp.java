package org.free;

import org.free.graph.PotHelper;
import org.free.ui.Fisher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MainApp {

    public static void main(String... args){
        new SpringApplicationBuilder(Fisher.class).headless(false).web(false).run();
//        new SpringApplicationBuilder(PotHelper.class).headless(false).web(false).run();
    }
}
