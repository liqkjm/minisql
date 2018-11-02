package com.liqkjm.minisql.server.catalog;

import java.io.*;
import java.util.ArrayList;

/**
 * @Describe TODO
 * @Author Liqkjm
 * @Date 2018/10/26 9:29
 */
public class Catalog implements Serializable {

    private static final long serialVersionUID = 1L;

    private File theFile;
    private String database_name;
    private int num_of_table;

    private ArrayList table_name;
    private ArrayList primary_key;
    private int[] table_length;
    private int[] num_of_attr;
    private int[] num_of_indexed_attr;

    public ArrayList[] attr_name;
    public ArrayList[] offset;
    public ArrayList[] attr_type;
    public ArrayList[] attr_length;
    public ArrayList[] is_unique;
    public ArrayList[] index_name;


    public Catalog(String database_name) {


        this.database_name = database_name;
        num_of_table = 0;
        table_name = new ArrayList();
        primary_key = new ArrayList();
        table_length = new int[0];
        num_of_attr = new int[0];
        num_of_indexed_attr = new int[0];

        attr_name = new ArrayList[0];
        offset = new ArrayList[0];
        attr_type = new ArrayList[0];
        attr_length = new ArrayList[0];
        is_unique = new ArrayList[0];
        index_name = new ArrayList[0];
    }

    public int writeCatalog() throws Exception {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(theFile));
        out.writeObject(new Catalog(database_name));
        out.close();
        return 0;
    }

    public int readCatalog() throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(theFile));
        Catalog catalog = (Catalog)in.readObject();
        in.close();
        /*测试输出*/
        System.out.println(catalog.toString());
        System.out.println(catalog.database_name);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        Catalog catalog = new Catalog("测试数据库的名字");
        catalog.theFile = new File("test.dat");
        catalog.writeCatalog();
        catalog.readCatalog();
    }
}
