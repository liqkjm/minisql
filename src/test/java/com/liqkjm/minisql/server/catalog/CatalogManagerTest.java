package com.liqkjm.minisql.server.catalog;

import org.junit.Test;

import static org.junit.Assert.*;

public class CatalogManagerTest {

    @Test
    public void createDatabase() throws Exception{
        CatalogManager cm = new CatalogManager();

        cm.createDatabase("test");
    }

    @Test
    public void dropDatabase() {
    }
}