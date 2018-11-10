package com.liqkjm.minisql.server;

import com.liqkjm.minisql.MinisqlApplication;

/**
 * @Describe TODO
 * @Author Liqkjm
 * @Date 2018/10/21 11:11
 */
public class API {
    //public void API(){}
    public API() {

    }

    public int createDatabase(String database_name) throws Exception {
        if(true) {
            MinisqlApplication.cm.createDatabase(database_name);
            return 0;
        }else {
            System.out.println("数据库已经存在");
            return -1;
        }

        // return 0;
    }

    public static void main(String[] args) {
        new API();
    }
}
