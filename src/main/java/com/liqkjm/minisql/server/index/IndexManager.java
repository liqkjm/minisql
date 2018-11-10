package com.liqkjm.minisql.server.index;

/**
 * @Describe TODO
 * @Author Liqkjm
 * @Date 2018/11/10 21:47
 */
import com.liqkjm.minisql.MinisqlApplication;
import com.liqkjm.minisql.server.buffermanager.FileHandle;
import java.io.*;


/**
 * 索引管理器，提供从不同键值映射到对应记录的快速索引信息
 */
public class IndexManager {
    public int createIndex() {return 0;}
    public int destroyIndex() {return 0;}
    public int openIndex() {return 0;}
    public int closeIndex() {return 0;}

    /**
     * 创建一个<database>.idx索引文件,并序列化，将其写入硬盘
     */
    public int createDatabase(String database_name) throws Exception
    {
        File ff= new File(MinisqlApplication.MINISQL_PATH + File.separator + "data" + File.separator + database_name + File.separator + database_name + ".idx");
        ff.createNewFile();
        FileHandle fh = new FileHandle(ff);
        fh.setDatabase_name(database_name);
        fh.setTable_name("");
        fh.setRecord_length(0);
        fh.setNum_of_page(0);
        fh.setNum_of_record(0);
        boolean[] bb = new boolean[0];
        fh.setIs_page_full(bb);
        fh.writeFile();
        return 0;
    }

    public int createIndex(String index_name, String table_name, String attr_name)
    {
        return 0;
    }
    public int dropIndex(String index_name)
    {
        return 0;
    }

    public int dropDatabase(String database_name)
    {
        File ff= new File(MinisqlApplication.MINISQL_PATH + File.separator + "data" + File.separator + database_name + File.separator + database_name + ".idx");
        ff.delete();
        return 0;
    }
}
