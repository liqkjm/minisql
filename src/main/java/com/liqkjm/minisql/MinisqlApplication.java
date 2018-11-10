package com.liqkjm.minisql;

import com.liqkjm.minisql.server.api.Api;
import com.liqkjm.minisql.server.buffermanager.BufferManager;
import com.liqkjm.minisql.server.catalog.CatalogManager;
import com.liqkjm.minisql.server.index.IndexManager;
import com.liqkjm.minisql.server.record.RecordManager;
import com.liqkjm.minisql.util.ClientAbsoluteLayout;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class MinisqlApplication {

    /*并不需要把翻译器设置为全局对象*/
    // public static Interpreter interpreter = new Interpreter();
    public static CatalogManager cm = new CatalogManager();
    public static RecordManager rm = new RecordManager();
    public static BufferManager bm = new BufferManager();
    public static IndexManager im = new IndexManager();

    public static Api api = new Api();
    public static final String dataPath = (new File("MinisqlApplication.java")).getAbsolutePath();
    public static final String MINISQL_PATH = (new File((new File("MinisqlApplication.java")).getAbsolutePath())).getParentFile().getAbsolutePath();

    public static void main(String[] args) {
        System.out.println("Path" + MINISQL_PATH);
        //new ClientAbsoluteLayout();
        //Interpreter interpreter = new Interpreter();

        //SpringApplication.run(MinisqlApplication.class, args);
    }
}
