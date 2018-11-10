package com.liqkjm.minisql.server.buffermanager;

/**
 * @Describe TODO
 * @Author Liqkjm
 * @Date 2018/11/10 21:44
 */
import com.liqkjm.minisql.MinisqlApplication;
import com.liqkjm.minisql.server.index.Index;

import java.io.*;

/**
 *
 * 缓存管理器...管理缓存
 *
 */
public class BufferManager {
    ////****Fields****////
    public static final int MAXNUM_OF_PAGE = 25;    //the max number of pages in buffer
    public static final int MAXNUM_OF_INDEX = 5;    //the max number of indexes in buffer
    private FileHandle[] tables;                   //all the files of the specified one database
    private PageHandle[] page_buffer;              //the handle of pages in buffer
    private Index[] index_buffer;					//the indexes in buffer
    private boolean[] page_dirty;                   //if the page is modified in buffer
    private short[] page_pin;                       //if the page is pinned
    private short[] page_usage;                     //use to modify the time that page in buffer(used in schedule)


    ////****Methods****////
    //**constructors**//
    public BufferManager()
    {
        tables = new FileHandle[0];
        page_buffer = new PageHandle[MAXNUM_OF_PAGE];
        index_buffer = new Index[MAXNUM_OF_INDEX];
        page_dirty = new boolean[MAXNUM_OF_PAGE];
        page_pin = new short[MAXNUM_OF_PAGE];
        page_usage = new short[MAXNUM_OF_PAGE];

        for(int i = 0; i < MAXNUM_OF_PAGE; i++)
        {
            page_dirty[i] = false;
            page_pin[i] = 0;
            page_usage[i] = 0;
        }
    }


    //**Accessors**//
    public FileHandle[] getTables() {return tables;}
    public PageHandle[] getPage_buffer() {return page_buffer;}
    public boolean[] getPage_dirty() {return page_dirty;}
    public short[] getPage_pin() {return page_pin;}
    public short[] getPage_usage() {return page_usage;}

    public FileHandle getThisFile_Handle(File ff)
    {
        for(int i = 0; i < tables.length; i++)
            if(tables[i].getFile().equals(ff))
                return tables[i];
        return null;
    }

    public PageHandle getThisPage_Handle(File ff, int pgno)
    {
        for(int i = 0; i < page_buffer.length; i ++)
            if((page_buffer[i].getFile().equals(ff)) && (page_buffer[i].getPage_no() == pgno))
                return page_buffer[i];
        return null;
    }

    public int getPage_in_buffer_no(File ff, int pgno)
    {
        for(int i = 0; i < page_buffer.length; i++)
            if((page_buffer[i].getFile().equals(ff)) & (page_buffer[i].getPage_no() == pgno))
                return i;
        return -1;
    }

    public boolean getPage_dirty(File ff, int pgno) {return page_dirty[getPage_in_buffer_no(ff, pgno)];}
    public short getPage_pin(File ff, int pgno) {return page_pin[getPage_in_buffer_no(ff, pgno)];}
    public short getPage_usage(File ff, int pgno) {return page_usage[getPage_in_buffer_no(ff, pgno)];}

    //**Mutators**//
    public void setPage_buffer(PageHandle ph, int pgbfno) {page_buffer[pgbfno] = ph;}
    public void setTables(FileHandle fh, File thefile)
    {
        for(int i = 0; i < tables.length; i++)
            if(tables[i].getFile().equals(thefile))
            {
                tables[i] = fh;
                break;
            }
    }
    public void setPage_dirty(File ff, int pgno, boolean bool) {page_dirty[getPage_in_buffer_no(ff, pgno)] = bool;}
    public void setPage_dirty(int pgbfno, boolean bool) {page_dirty[pgbfno] = bool;}
    public void setPage_pin(File ff, int pgno) {page_pin[getPage_in_buffer_no(ff, pgno)]++;}
    public void setPage_pin(int pgbfno, short pin) {page_pin[pgbfno] = pin;}
    public void setPage_unpin(File ff, int pgno)
    {
        if(page_pin[getPage_in_buffer_no(ff, pgno)] == 0)
            return;
        else
            page_pin[getPage_in_buffer_no(ff, pgno)]--;
    }
    public void setPage_usage(File ff, int pgno) { page_usage[getPage_in_buffer_no(ff, pgno)]++; }
    public void setPage_usage(int pgbfno, short use) { page_usage[pgbfno] = use;}

    /**
     * 将创建的表文件存在Buffer_Manager的tables数组（类型为File_Handle）之中
     * 一开始，tables.length的值为0
     * @param fh
     * @return
     */
    public int insertFile_Handle(FileHandle fh)
    {
        FileHandle[] tb = new FileHandle[tables.length + 1];
        for(int i = 0; i < tables.length; i++)
            tb[i] = tables[i];
        tb[tb.length - 1] = fh;
        tables = tb;
        return 0;
    }

    public int deleteFile_Handle(int fhno)
    {
        FileHandle[] new_tables = new FileHandle[tables.length - 1];
        for(int i = 0; i < new_tables.length; i++)
            if(i < fhno)
                new_tables[i] = tables[i];
            else
                new_tables[i] = tables[i + 1];
        tables = new_tables;
        return 0;
    }
    //**Functions**//
    //select which database to use
    public int UseDatabase(String basename) throws Exception
    {
        //save old database files and pages
        for(int i = 0; i < tables.length; i++)
            if(tables[i] != null)
                tables[i].writeFile();
        for(int i = 0; i < page_buffer.length; i++)
        {
            if(page_buffer[i] != null)
            {
                page_buffer[i].writeData();
                page_buffer[i].writePageHead();
                page_buffer[i] = null;
            }
        }


        //use new database
        File dir = new File(MinisqlApplication.MINISQL_PATH + File.separator + "data" + File.separator + basename);
        String[] filenames = dir.list();

        int tablenum = 0;

        //get the number of tables
        for(int i = 0; i < filenames.length; i++)
        {
            if(filenames[i].endsWith(".dat"))
                tablenum++;
        }

        tables = new FileHandle[tablenum];

        //read the file_head of each file
        for(int i = 0; i < filenames.length; i++)
        {
            if(!filenames[i].endsWith(".dat"))
                continue;
            else
            {
                tables[i] = new FileHandle();
                tables[i].setFile(new File(dir.getAbsolutePath() + File.separator + filenames[i]));
                tables[i].readFile();
            }
        }


        return 0;
    }

    //test whether the page buffer is full
    private boolean Is_page_buffer_full()
    {
        int i;
        for(i = 0; i < page_buffer.length; i++)
            if(page_buffer[i] == null)
                break;
        if(i < page_buffer.length - 1)
            return false;
        return true;
    }

    //check whether the specified page is in buffer
    public boolean Check_page_in_buffer(File thefile, int page_no)
    {
        for(int i = 0; i < page_buffer.length; i++)
            if((page_buffer[i] != null) && (page_buffer[i].getFile().equals(thefile)) && (page_buffer[i].getPage_no() == page_no))
            {
                setPage_usage(thefile, page_no);
                return true;
            }
        return false;
    }

    //get the page into buffer

    /**
     * 从文件中取出页，将其存入缓存page_buffer中
     * @param ff
     * @param pgno
     * @param pgbfno
     * @throws Exception
     */
    private void Get_page_in(File ff, int pgno, int pgbfno) throws Exception
    {
        PageHandle ph = new PageHandle(pgno, ff); // 初始化一个page_data数组，准备存储数据
        ph.readPageHead();
        ph.readData();
        page_buffer[pgbfno] = ph;

    }

    /**
     * 读取缓存，如果缓存满了，则交换，
     * 如果没满，则将其插入（将文件中的页插入缓存），并设置该页的状态
     * @param ff
     * @param pgno
     * @throws Exception
     */
    public void Read_page_in(File ff, int pgno) throws Exception
    {
        if(Check_page_in_buffer(ff, pgno))
            return;
        if(Is_page_buffer_full())
            PageSwap(ff, pgno); // TODO: 如果缓存满了，则交换
        else
        {
            int free = 0;
            for(int i = 0; i < MAXNUM_OF_PAGE; i++)
                if(page_buffer[i] == null)
                {
                    free = i;
                    break;
                }
            Get_page_in(ff, pgno, free); // 将文件中的页插入缓存page_buffer中
            setPage_dirty(free, false);	// 该页是否被修改
            setPage_usage(free, (short)0);	// 被使用（之后用于交换，交换最久使用的页）
            setPage_pin(free, (short)0); // 是否被固定（不可交换）
        }
    }
    // 牛逼牛逼 --liqkjm
    //swap pages between buffer and file according to LRU strategy
    private int PageSwap(File ff, int pgno) throws Exception
    {
        int min = 0;
        for(int i = 0; i < MAXNUM_OF_PAGE; i++)
        {
            if(page_pin[i] > 0)
                continue;
            if(page_usage[i] < page_usage[min])
                min = i;
        }

        if(page_dirty[min])
        {
            page_buffer[min].writeData();
            page_buffer[min].writePageHead();
        }

        Get_page_in(ff, pgno, min);
        page_pin[min] = 0;
        page_usage[min] = 1;

        return 0;
    }

    public int dropDatabase(String database)
    {
        if(MinisqlApplication.api.getClass().equals(database))
        {
            for(int i = 0; i < tables.length; i++)
                tables[i] = null;
            for(int i = 0; i < page_buffer.length; i++)
                page_buffer[i] = null;
        }
        return 0;
    }

    public int dropTable(File ff)
    {
        for(int i = 0; i < page_buffer.length; i++)
            if((page_buffer[i] != null) && (page_buffer[i].getFile().equals(ff)))
                page_buffer[i] = null;
        for(int i = 0; i < tables.length; i++)
            if((tables[i] != null) && (tables[i].getFile().equals(ff)))
                deleteFile_Handle(i);
        return 0;
    }

    /**
     * 退出时，将缓存中的所有数据都写入文件
     * @return
     * @throws Exception
     */
    public int quit() throws Exception
    {
        for(int i = 0; i < tables.length; i++)
            if(tables[i] != null)
                tables[i].writeFile();

        for(int i = 0; i < page_buffer.length; i++)
        {
            if(page_buffer[i] != null)
            {
                page_buffer[i].writeData();
                page_buffer[i].writePageHead();
            }
        }
        // TODO: 索引功能
        //for(int i = 0; i < index_buffer.length; i++)
            //if(index_buffer[i] != null)
                // index_buffer[i].writeIndex();

        return 0;

    }


}
