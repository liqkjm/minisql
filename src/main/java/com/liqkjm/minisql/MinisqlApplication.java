package com.liqkjm.minisql;

import com.liqkjm.minisql.server.api.Api;
import com.liqkjm.minisql.util.ClientAbsoluteLayout;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class MinisqlApplication {

    /*并不需要把翻译器设置为全局对象*/
    // public static Interpreter interpreter = new Interpreter();

    public static Api api = new Api();
    public static final String dataPath = (new File("MinisqlApplication.java")).getAbsolutePath();

    public static void main(String[] args) {

        new ClientAbsoluteLayout();
        //Interpreter interpreter = new Interpreter();

        //SpringApplication.run(MinisqlApplication.class, args);
    }
}
