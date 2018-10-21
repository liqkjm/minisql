package com.liqkjm.minisql;

import com.liqkjm.minisql.util.Client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinisqlApplication {

    public static void main(String[] args) {

        new Client();

        //SpringApplication.run(MinisqlApplication.class, args);
    }
}
