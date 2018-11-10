package com.liqkjm.minisql.server.record;

/**
 * @Describe TODO
 * @Author Liqkjm
 * @Date 2018/11/10 21:47
 */
import com.liqkjm.minisql.MinisqlApplication;
import com.liqkjm.minisql.server.buffermanager.FileHandle;
import com.liqkjm.minisql.server.buffermanager.PageHandle;

import java.io.*;


/**
 * 记录管理器,管理数据记录
 */
public class RecordManager
{
    /**
     * 插入数据到dat文件
     * @param table_name    表名
     * @param data          数据
     * @return
     * @throws Exception
     */
    public int insertRecord(String table_name, byte[] data) throws Exception
    {
        File ff = new File(MinisqlApplication.MINISQL_PATH + File.separator + "data" + File.separator + MinisqlApplication.api.getCurrentDatabase() + File.separator + table_name + ".dat");
        Record rr = new Record(MinisqlApplication.api.getCurrentDatabase(), table_name, ff, data.length, 0, (short)0, data);
        rr.insertRecord(MinisqlApplication.bm); // 将文件插入缓存，创建表的时候，将表的信息加入了缓存对象的tables数组里面，插入数据的时候，将缓存插入文件

        return 0;
    }
    public int deleteRecord(String table_name, int record_no, int record_length) throws Exception
    {
        File ff = new File(MinisqlApplication.MINISQL_PATH + File.separator + "data" + File.separator + MinisqlApplication.api.getCurrentDatabase() + File.separator + table_name + ".dat");
        int Num_of_page = MinisqlApplication.bm.getThisFile_Handle(ff).getNum_of_page();
        int Num_of_record = MinisqlApplication.bm.getThisFile_Handle(ff).getNum_of_record();
        int[] Num_of_record_each_page = new int[Num_of_page];
        int temp = record_no;
        byte[] data = new byte[record_length];

        for(int i = 0; i < Num_of_page; i++)
        {
            if(!MinisqlApplication.bm.Check_page_in_buffer(ff, i))
                MinisqlApplication.bm.Read_page_in(ff, i);
            Num_of_record_each_page[i] = MinisqlApplication.bm.getThisPage_Handle(ff, i).getNum_of_record();
        }

        int j;
        for(j = 0; j < Num_of_page; j++)
        {
            if(temp <= Num_of_record_each_page[j] - 1)
                break;
            else
                temp -= Num_of_record_each_page[j];
        }
        if(!MinisqlApplication.bm.Check_page_in_buffer(ff, j))
            MinisqlApplication.bm.Read_page_in(ff, j);

        Record rc = new Record(MinisqlApplication.api.getCurrentDatabase(),table_name,ff, record_length, j, (short)(temp), data);
        rc.deleteRecord(MinisqlApplication.bm);

        return 0;
    }

    public int getNum_of_Record(String table_name)
    {
        File ff = new File(MinisqlApplication.MINISQL_PATH + File.separator + "data" + File.separator + MinisqlApplication.api.getCurrentDatabase() + File.separator + table_name + ".dat");
        return MinisqlApplication.bm.getThisFile_Handle(ff).getNum_of_record();
    }

    /**
     * 查询数据
     * @param table_name    表名
     * @param record_no     记录序号
     * @param record_length
     * @return
     * @throws Exception
     */
    public byte[] getRecord(String table_name, int record_no, int record_length) throws Exception
    {
        File ff = new File(MinisqlApplication.MINISQL_PATH + File.separator + "data" + File.separator + MinisqlApplication.api.getCurrentDatabase() + File.separator + table_name + ".dat");

        int Num_of_page = MinisqlApplication.bm.getThisFile_Handle(ff).getNum_of_page(); // 已有页的数量

        int temp = 0;
        int Num_of_record_each_page = PageHandle.PAGE_SIZE / record_length; // 每页含有多少条记录
        boolean[] bb = new boolean[Num_of_page];
        bb = MinisqlApplication.bm.getThisFile_Handle(ff).getIs_page_full();
        int j;
        byte[] data = new byte[record_length];
        /**
         * 统计记录的数量
         * temp：页偏移，所指定记录在该页的位置
         */
        for(j = 0; j < Num_of_page; j++)
        {
            if(bb[j])
                temp = temp + Num_of_record_each_page;
            else
            {
                if(!MinisqlApplication.bm.Check_page_in_buffer(ff, j))
                    MinisqlApplication.bm.Read_page_in(ff, j);
                temp = temp + MinisqlApplication.bm.getThisPage_Handle(ff,j).getNum_of_record();
            }

            if(temp > record_no)
                break;
        }

        if(!MinisqlApplication.bm.Check_page_in_buffer(ff, j))
            MinisqlApplication.bm.Read_page_in(ff, j);

        temp = record_no - temp + MinisqlApplication.bm.getThisPage_Handle(ff,j).getNum_of_record();

        Record rc = new Record(MinisqlApplication.api.getCurrentDatabase(),table_name, ff, record_length, j, (short)(temp), data);
        rc.getRecord(MinisqlApplication.bm.getThisPage_Handle(ff, j));
        data = rc.getRecord_data();
        return data;

    }

    /**
     * 创建表dat文件
     * @param table_name
     * @return
     * @throws Exception
     */
    public int createTable(String table_name) throws Exception
    {

        File ff = new File(MinisqlApplication.MINISQL_PATH + File.separator + "data" + File.separator + MinisqlApplication.api.getCurrentDatabase() + File.separator + table_name + ".dat");
        ff.createNewFile();
        FileHandle fh = new FileHandle(ff);
        fh.initFile_Handle(MinisqlApplication.api.getCurrentDatabase(),table_name, MinisqlApplication.cm.getRecord_length(table_name));
        MinisqlApplication.bm.insertFile_Handle(fh);
        fh.writeFile();
        return 0;
    }
    public int dropTable(String table_name)
    {
        File ff = new File(MinisqlApplication.MINISQL_PATH + File.separator + "data" + File.separator + MinisqlApplication.api.getCurrentDatabase() + File.separator + table_name + ".dat");
        MinisqlApplication.bm.dropTable(ff);
        ff.delete();
        return 0;
    }

    public int dropDatabase(String database_name)
    {
        MinisqlApplication.bm.dropDatabase(database_name);
        File dir = new File(MinisqlApplication.MINISQL_PATH + File.separator + "data" + File.separator + database_name);
        String[] filenames = dir.list();


        for(int i = 0; i < filenames.length; i++)
        {
            if(filenames[i].endsWith(".dat"))
            {
                File ff = new File(dir.getAbsolutePath() + File.separator + filenames[i]);
                ff.delete();
            }
        }
        return 0;
    }
}