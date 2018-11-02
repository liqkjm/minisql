package com.liqkjm.minisql.server.interpreter;

import java.io.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Describe SQL语句翻译器，分析命令并调用底层接口，执行对应操作
 * 采用正则表达式来匹配命令
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

    /**
     * 对外接口，传过来的值，可能包含多条命令，需要分割每条命令，并判断每条命令的有效性。
     * 分别检查每条命令的有效性，并执行对应操作
     * @param querys
     * @return
     */
    public String getResult(String querys) {

        /**
         * 两条命令中间可能存在多个无效字符，需要清除
         */
        char[] querysArray = querys.toCharArray();
        int flag = 0;
        String result = "";
        for(int i = 0; i < querysArray.length - 1; i++) {

            if (querysArray[i] == ';' && querysArray[i + 1] != ';') {
                String currentQuery = querys.substring(flag, i + 1);
                result += sqlParser(clean(currentQuery));
                flag = i + 1;
            }else if((i == (querysArray.length - 1)) && (querysArray[i] == ';')) {

            }
        }
        /**
         * 判断最后一条命令是不是以分号结束？
         * 限定最后一个字符只能是分号，不能是回车等等其他的字符
         * 如果最后的命令有分号，那么querysArray[len - 1] = ";"
         */
        if(querysArray[querysArray.length - 1] != ';') {
            result += "命令必须以分号结束";
        }else {
            result += sqlParser(clean(querys.substring(flag, querysArray.length - 1)));
        }

        return result;


        // return "无效命令，请检查重试";
    }

    /**
     * 格式化输入语句，清除其中的无效格式符
     * @param s
     * @return
     */
    public static String clean(String s)
    {
        if(s == null || s.length() < 2) {
            return "";
        }
        char[] c = s.toCharArray();
        //清除命令前的空格 回车符 tab符 以及换行符
        if(c[0] == ' ' | c[0] == '\r' | c[0] == '\n' | c[0] == '\t')
        {
            int i;
            for(i = 0; i < c.length; i++)
                if(c[i] != ' ' && c[i] != '\r' && c[i] != '\n' && c[i] != '\t')
                    break;
            c = s.substring(i).toCharArray();
            s = new String(c);
        }

        /*清除注释*/
        c = s.toCharArray();
        int count = 0;
        int j, k;
        for(j = 0; j < c.length; j++)
        {
            if(c[j] == '\"' | c[j] == '\'')
                count++;
        }
        count = count / 2;

        String[] yes = new String[count];
        String[] no = new String[count + 1];

        j = 0; k = -1;
        for(int i = 0; i < yes.length; i++)
        {
            for(j = k + 1; j < c.length; j++)
                if(c[j] == '\"' | c[j] == '\'')
                    break;
            no[i] = s.substring(k + 1, j);
            for(k = j + 1; k < c.length; k++)
                if(c[k] == '\"' | c[k] == '\'')
                    break;
            if(k == (c.length - 1))
                yes[i] = s.substring(j);
            else
                yes[i] = s.substring(j, k+ 1);
        }
        if(c[c.length - 1] == '\"' | c[c.length - 1] == '\'')
            no[no.length - 1] = "";
        else
            no[no.length - 1] = s.substring(k + 1);

        for(int i = 0; i < no.length; i++)
        {
            String patternStr = "\\s+";
            String replaceStr = " ";
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(no[i]);
            no[i] = matcher.replaceAll(replaceStr);
        }

        s = "";
        for(int i = 0; i < yes.length; i++)
        {
            s += no[i];
            s += yes[i];
        }
        s += no[no.length - 1];


        //清除末尾的分号 以及空格 回车符 tab符 以及换行符
        c = s.toCharArray();

        if(c[c.length - 1] == ' ' | c[c.length - 1] == ';' | c[c.length - 1] == '\r' | c[c.length - 1] == '\n' |c[c.length - 1] != '\t')
        {
            int i;
            for(i = c.length - 1; i > -1; i--)
                if(c[i] != ' ' && c[i] != ';' && c[i] != '\r' && c[i] != '\n' && c[i] != '\t')
                    break;
            c = s.substring(0, i + 1).toCharArray();
            s = new String(c);
        }
        return s;
    }

    /**
     * 检查命令是否有效，并执行相应操作，无效返回“命令无效”
     * @return 返回字符串
     */
    public String sqlParser(String query) {

        if(query.matches(create_database_pattern)) {
            // 调用API
            return query + "\n该命令执行成功\n\n";
        }else if(query.matches(show_database_pattern)) {
            return show_database_interpret();
        }

        return query + " \n该命令无效，请检查重新输入。\n\n";
    }

    //显示已经存在的数据库
    public String show_database_interpret(){
        String[] databaseList = null;
        String result = "";
        File ff = new File("H:/Program/idea/minisql/data/");
        File[] fileList = ff.listFiles();
        databaseList = new String[fileList.length];
        for(int i = 0;i<fileList.length;i++){
            if(fileList[i].isDirectory()){
                databaseList[i] = fileList[i].getName();
                result += fileList[i].getName() + "\n";
                System.out.println(fileList[i].getName());
            }
        }
        return "数据库列表：\n" + result + "该命令执行成功\n\n";
    }

    public static void main(String[] arg) throws Exception
    {
        new Interpreter();
    }
}