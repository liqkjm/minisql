package com.liqkjm.minisql.server;

import java.io.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Describe 翻译器，分析命令并执行对应操作
 * @Author Liqkjm
 * @Date 2018/10/21 11:42
 */
public class Interpreter {
    private String query;
    private Date query_time;
    private long millisecond;
    private static String create_database_pattern;
    private static String create_table_pattern;
    private static String create_index_pattern;
    private static String use_pattern;
    private static String drop_database_pattern;
    private static String drop_table_pattern;
    private static String drop_index_pattern;
    private static String insert_pattern;
    private static String delete_pattern;
    private static String update_pattern;
    private static String select_pattern;
    private static String execfile_pattern;
    private static String show_database_pattern;
    private static String show_table_pattern;
    public Interpreter()
    {
        query = "";
        create_database_pattern = "create database [^\\s]+";
        create_table_pattern = "create table [^\\s]+.*\\(.*\\)";
        create_index_pattern = "create index [^\\s]+ on [^\\s]+.*\\([\\s]*[^\\s]+[\\s]*\\)";
        use_pattern = "use [^\\s]+";
        drop_database_pattern = "drop database [^\\s]+";
        drop_table_pattern = "drop table [^\\s]+";
        drop_index_pattern = "drop index [^\\s]+";
        insert_pattern = "insert into [^\\s]+ values[\\s]*\\(.+\\)";
        delete_pattern = "delete from [^\\s]+.*";
        update_pattern = "update [^\\s]+ set [^\\s]+.* where .*";
        select_pattern = "select [^\\s].* from [^\\s]+.*";
        execfile_pattern = "execfile .*";
        show_database_pattern = "show databases";
        show_table_pattern = "show tables";
    }
    public static void main(String[] arg) throws Exception
    {
        String s = "confds = \"123.89";
        Interpreter ii = new Interpreter();
        //System.out.println(ii.condition_check(clean(s)));

    }


}