package com.liqkjm.minisql.server;

import com.liqkjm.minisql.util.Client;

/**
 * @Describe TODO
 * @Author Liqkjm
 * @Date 2018/10/21 11:16
 */
public class OnlyServerStart {
    public OnlyServerStart() {

    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        new Client();
    }
}
