package com.liqkjm.minisql.server.index;

/**
 * @Describe TODO
 * @Author Liqkjm
 * @Date 2018/11/10 21:46
 */
import com.liqkjm.minisql.MinisqlApplication;
import com.liqkjm.minisql.server.api.Api;
import com.liqkjm.minisql.server.buffermanager.FileHandle;
import com.liqkjm.minisql.server.buffermanager.PageHandle;
import com.liqkjm.minisql.server.record.Record;

public class Index
{
    public static final int NUM_OF_VALUE = 100;
    ////****Fields****////
    private int index_no;
    private String index_name;
    private FileHandle fh;
    private PageHandle[] ph;


    public Index() {}

    public Index(FileHandle ff, String in, int io)
    {
        fh = ff;
        index_name = in;
        index_no = io;
    }
    public Index(int io, String in, FileHandle ff, PageHandle[] pp)
    {
        index_no = io;
        index_name = in;
        fh = ff;
        ph = pp;
    }

    public int getIndex_no() {return index_no;}
    public String getIndex_name() {return index_name;}
    public FileHandle getFile_Handle() {return fh;}
    public PageHandle[] getPage_Handle() {return ph;}

    public void setIndex_no(int i) {index_no = i;}
    public void setindex_name(String ss) {index_name = ss;}
    public void setFile_Handle(FileHandle ff) {fh = ff;}
    public void setPage_Handle(PageHandle[] pp) {ph = pp;}


    public int initIndex(String idx_name, PageHandle pp) throws Exception
    {
        pp.initPage();
        Record rr = new Record(MinisqlApplication.api.getCurrentDatabase(), pp.getTable_name(),pp.getFile(), 2, pp.getPage_no(), (short)0, Api.charTobyte('R'));
        rr.insertRecord(pp);
        rr.setRecord_length(4);
        rr.setRecord_data(Api.intTobyte(0));
        rr.setSlot_no(((short)1));
        rr.insertRecord(pp);

        ph = new PageHandle[1];
        ph[0] = pp;

        return 0;
    }

    public int insert_entry(PageHandle pp, byte[] data, byte[] ptr) throws Exception
    {
        byte[] tt = new byte[2];
        tt[0] = pp.getPage_data()[pp.getAddr_of_record()[0]];
        tt[1] = pp.getPage_data()[pp.getAddr_of_record()[0] + 1];
        char type = Api.byteTochar(tt);

        byte[] rt = new byte[4];
        rt[0] = pp.getPage_data()[pp.getAddr_of_record()[1]];
        rt[1] = pp.getPage_data()[pp.getAddr_of_record()[1] + 1];
        rt[2] = pp.getPage_data()[pp.getAddr_of_record()[1] + 2];
        rt[3] = pp.getPage_data()[pp.getAddr_of_record()[1] + 3];
        int parent = Api.byteToint(rt);

        //if there is enough space in pp for data, then insert it
        if((((pp.getNum_of_record() - 3) / 2) < NUM_OF_VALUE) && ((pp.getEnd_of_freespace() - (4 + pp.getNum_of_record() * 2) + 6) > (data.length + 6)))
            insertValue(pp, data, ptr);
            //else split the node pp
        else
        {
            //create new node
            PageHandle p = fh.allocatePage();
            Record temprr = new Record(fh.getDatabase_name(), fh.getTable_name(), fh.getFile(), 2, p.getPage_no(), (short)0, Api.charTobyte(type));
            temprr.insertRecord(p);
            fh.increaseNum_of_record();
            temprr.setRecord_length(4);
            temprr.setSlot_no((short)1);
            temprr.setRecord_data(Api.intTobyte(parent));
            temprr.insertRecord(p);
            fh.increaseNum_of_record();

            int vv, m;

            int[] value = new int[(pp.getNum_of_record() - 1) / 2];
            int[] ptrvalue = new int[value.length + 1];

            int temp;
            int[] sortedvalue = new int[value.length];
            int[] sortedptr = new int[ptrvalue.length];

            byte[] b;

            //get the key values and pointers from the node pp
            for (int i = 0; i < value.length - 1; i++)
            {
                b = new byte[pp.getAddr_of_record()[(i + 1) * 2] - pp.getAddr_of_record()[i * 2 + 3]];
                for (int j = 0; j < b.length; j++)
                    b[j] = pp.getPage_data()[pp.getAddr_of_record()[i * 2 + 3] + j];
                value[i] = Api.byteToint(b);
                b = new byte[pp.getAddr_of_record()[i * 2 + 1] - pp.getAddr_of_record()[i * 2 + 2]];
                for (int j = 0; j < b.length; j++)
                    b[j] = pp.getPage_data()[pp.getAddr_of_record()[i * 2 + 2] + j];
                ptrvalue[i] = Api.byteToint(b);
            }
            value[value.length - 1] = Api.byteToint(data);

            b = new byte[6];
            for (int i = 0; i < b.length; i++)
                b[i] = pp.getPage_data()[pp.getAddr_of_record()[pp.getNum_of_record() - 1] + i];

            //if this node is a leaf node
            if(type == 'L' | type == 'l')
            {
                ptrvalue[ptrvalue.length - 1] = Api.byteToint(b);

                ptrvalue[ptrvalue.length - 2] = Api.byteToint(ptr);

                //sort the values and pointers
                for (int i = 0; i < value.length; i++)
                    sortedvalue[i] = value[i];
                for (int i = 0; i < ptrvalue.length; i++)
                    sortedptr[i] = ptrvalue[i];
                for (int i = 0; i < sortedvalue.length - 1; i++)
                {
                    for (int j = i + 1; j < sortedvalue.length; j++)
                        if (sortedvalue[i] > sortedvalue[j])
                        {
                            temp = sortedvalue[i];
                            sortedvalue[i] = sortedvalue[j];
                            sortedvalue[j] = temp;
                            temp = sortedptr[i];
                            sortedptr[i] = sortedptr[j];
                            sortedptr[j] = temp;
                        }
                }

                //deside the m and vv
                m = (sortedvalue.length - 2) / 2;
                vv = sortedvalue[m + 1];

                for (int i = 0; i < value.length; i++)
                    deleteValue(pp, Api.intTobyte(value[i]));

                for (int i = 0; i <= m; i++)
                    insertValue(pp, Api.intTobyte(sortedvalue[i]), Api.intTobyte(sortedptr[i]));
                for (int i = m + 1; i < sortedvalue.length; i++)
                    insertValue(p, Api.intTobyte(sortedvalue[i]), Api.intTobyte(sortedptr[i]));
            }
            //if pp is a non-leaf node
            else
            {
                ptrvalue[ptrvalue.length - 2] = Api.byteToint(b);
                ptrvalue[ptrvalue.length - 1] = Api.byteToint(ptr);

                //sort the values and pointers
                for (int i = 0; i < value.length; i++)
                    sortedvalue[i] = value[i];
                for (int i = 0; i < ptrvalue.length; i++)
                    sortedptr[i] = ptrvalue[i];
                for (int i = 0; i < sortedvalue.length - 1; i++)
                {
                    for (int j = i + 1; j < sortedvalue.length; j++)
                        if (sortedvalue[i] > sortedvalue[j])
                        {
                            temp = sortedvalue[i];
                            sortedvalue[i] = sortedvalue[j];
                            sortedvalue[j] = temp;
                            temp = sortedptr[i + 1];
                            sortedptr[i + 1] = sortedptr[j + 1];
                            sortedptr[j + 1] = temp;
                        }
                }

                //decide the m and vv
                m = sortedvalue.length / 2;
                vv = sortedvalue[m + 1];

                for(int i = 0; i < value.length; i++)
                    deleteValue(pp, Api.intTobyte((value[i])));

                for (int i = 0; i <= m; i++)
                    insertValue(pp, Api.intTobyte(sortedvalue[i]), Api.intTobyte(sortedptr[i]));
                for (int i = m + 1; i < sortedvalue.length; i++)
                    insertValue(p, Api.intTobyte(sortedvalue[i]), Api.intTobyte(sortedptr[i]));
            }

            //if pp is not the root of the tree, then do insert_entry(parent(p), vv, ptr to pp)
            if(type != 'R' && type != 'r')
            {
                PageHandle ppp = new PageHandle(parent, fh.getFile());
                ppp.readData();
                b = new byte[6];
                for(int i = 0; i < 4; i++)
                    b[i] = Api.intTobyte(p.getPage_no())[i];
                insert_entry(ppp, Api.intTobyte(vv), b);
            }
            //else create a new root node ppp and insert ptr of pp ,vv,  ptr of p into this new root
            else
            {
                PageHandle ppp = fh.allocatePage();
                temprr = new Record(fh.getDatabase_name(), fh.getTable_name(), fh.getFile(), 2, ppp.getPage_no(), (short)0, Api.charTobyte('R'));
                temprr.insertRecord(ppp);
                fh.increaseNum_of_record();

                temprr.setRecord_length(4);
                temprr.setSlot_no((short)1);
                temprr.setRecord_data(Api.intTobyte(0));
                temprr.insertRecord(ppp);
                fh.increaseNum_of_record();

                b = new byte[6];
                for(int i = 0; i < 4; i++)
                    b[i] = Api.intTobyte(pp.getPage_no())[i];
                temprr.setRecord_length(6);
                temprr.setSlot_no((short)2);
                temprr.insertRecord(ppp);
                fh.increaseNum_of_record();

                temprr.setRecord_length(Api.intTobyte(vv).length);
                temprr.setSlot_no((short)3);
                temprr.insertRecord(ppp);
                fh.increaseNum_of_record();

                for(int i = 0; i < 4; i++)
                    b[i] = Api.intTobyte(p.getPage_no())[i];
                temprr.setRecord_length(6);
                temprr.setSlot_no((short)4);
                temprr.insertRecord(ppp);
                fh.increaseNum_of_record();

                //modify pp
                temprr.setPage_no(pp.getPage_no());
                temprr.setSlot_no((short)0);
                temprr.deleteRecord(pp);
                fh.decreaseNum_of_record();

                temprr.setRecord_length(2);
                temprr.setRecord_data(Api.charTobyte('N'));
                temprr.insertRecord(pp);
                fh.increaseNum_of_record();

                temprr.setSlot_no((short)1);
                temprr.deleteRecord(pp);
                fh.decreaseNum_of_record();

                temprr.setRecord_length(4);
                temprr.setRecord_data(Api.intTobyte(ppp.getPage_no()));
                temprr.insertRecord(pp);
                fh.increaseNum_of_record();

                //modify p
                temprr.setPage_no(p.getPage_no());
                temprr.setSlot_no((short)0);
                temprr.deleteRecord(p);
                fh.decreaseNum_of_record();

                temprr.setRecord_length(2);
                temprr.setRecord_data(Api.charTobyte('N'));
                temprr.insertRecord(p);
                fh.increaseNum_of_record();

                temprr.setSlot_no((short)1);
                temprr.deleteRecord(p);
                fh.decreaseNum_of_record();

                temprr.setRecord_length(4);
                temprr.setRecord_data(Api.intTobyte(ppp.getPage_no()));
                temprr.insertRecord(p);
                fh.increaseNum_of_record();
            }

            // if p is a leaf node, modify the Pn pointer of pp and p
            if(type == 'L' | type == 'l')
            {
                temprr.setPage_no(pp.getPage_no());
                temprr.setSlot_no((short)(pp.getNum_of_record() - 1));
                temprr.setRecord_length(6);
                b = temprr.getRecord_data();

                temprr.setPage_no(p.getPage_no());
                temprr.setSlot_no((short)(p.getNum_of_record()));
                temprr.setRecord_data(b);
                temprr.insertRecord(p);

                temprr.setPage_no(pp.getPage_no());
                temprr.setSlot_no((short)(pp.getNum_of_record() - 1));
                temprr.deleteRecord(pp);

                temprr.setRecord_data(Api.intTobyte(p.getPage_no()));
                temprr.insertRecord(pp);
                fh.increaseNum_of_record();
            }

        }
        return 0;
    }

    //insert value to a node which has free space for it
    public int insertValue(PageHandle pp, byte[] data, byte[] ptr)
    {
        byte[] tt = new byte[2];
        tt[0] = pp.getPage_data()[pp.getAddr_of_record()[0]];
        tt[1] = pp.getPage_data()[pp.getAddr_of_record()[0] + 1];
        char type = Api.byteTochar(tt);

        int value;
        short position = (short)pp.getNum_of_record();
        Record rc = new Record(fh.getDatabase_name(), fh.getTable_name(), fh.getFile(), 0, pp.getPage_no(), (short)0, new byte[0]);
        int i;

        if(type == 'L' | type == 'l')
        {
            for(i = 0; i < (pp.getNum_of_record() - 3) / 2; i++)
            {
                rc.setSlot_no((short)(3 + i * 2));
                rc.setRecord_length(pp.getAddr_of_record()[rc.getSlot_no() - 1] - pp.getAddr_of_record()[rc.getSlot_no()]);
                rc.getRecord(pp);
                value = Api.byteToint(rc.getRecord_data());
                if(value > Api.byteToint(data))
                {
                    position = (short)(rc.getSlot_no() - 1);
                    break;
                }
            }
            if( i >= ((pp.getNum_of_record() - 1) / 2 - 1))
                position = (short)(pp.getNum_of_record() - 1);

            rc.setSlot_no(position);
            rc.setRecord_length(ptr.length);
            rc.setRecord_data(ptr);
            rc.insertRecord(pp);

            fh.increaseNum_of_record();

            rc.setSlot_no((short)(position + 1));
            rc.setRecord_length(data.length);
            rc.setRecord_data(data);
            rc.insertRecord(pp);

            fh.increaseNum_of_record();
        }
        else
        {
            for(i = 0; i < (pp.getNum_of_record() - 3) / 2; i++)
            {
                rc.setSlot_no((short)(3 + i * 2));
                rc.setRecord_length(pp.getAddr_of_record()[rc.getSlot_no() - 1] - pp.getAddr_of_record()[rc.getSlot_no()]);
                rc.getRecord(pp);
                value = Api.byteToint(rc.getRecord_data());
                if(value > Api.byteToint(data))
                {
                    position = (short)(rc.getSlot_no());
                    break;
                }
            }
            if( i >= ((pp.getNum_of_record() - 3) / 2))
                position = (short)(pp.getNum_of_record());

            rc.setSlot_no(position);
            rc.setRecord_length(data.length);
            rc.setRecord_data(data);
            rc.insertRecord(pp);

            fh.increaseNum_of_record();

            rc.setSlot_no((short)(position + 1));
            rc.setRecord_length(ptr.length);
            rc.setRecord_data(ptr);
            rc.insertRecord(pp);

            fh.increaseNum_of_record();
        }
        return 0;
    }

    //delete the value from a leaf node
    public int deleteValue(PageHandle pp, byte[] data) throws Exception
    {
        byte[] tt = new byte[2];
        tt[0] = pp.getPage_data()[pp.getAddr_of_record()[0]];
        tt[1] = pp.getPage_data()[pp.getAddr_of_record()[0] + 1];
        char type = Api.byteTochar(tt);

        int[] value = new int[(pp.getNum_of_record() - 3) / 2];
        byte[] b;
        int i;
        Record rc;

        for (i = 0; i < value.length; i++)
        {
            b = new byte[pp.getAddr_of_record()[(i + 1) * 2] - pp.getAddr_of_record()[i * 2 + 3]];
            for (int j = 0; j < b.length; j++)
                b[j] = pp.getPage_data()[pp.getAddr_of_record()[i * 2 + 3] + j];
            value[i] = Api.byteToint(b);
        }

        for (i = 0; i < value.length; i++)
            if (value[i] == Api.byteToint(data))
                break;
        if(type == 'L' | type == 'l')
            rc = new Record(fh.getDatabase_name(), fh.getTable_name(), fh.getFile(), 0, pp.getPage_no(), (short) ((i * 2 + 2)), new byte[0]);
        else
            rc = new Record(fh.getDatabase_name(), fh.getTable_name(), fh.getFile(), 0, pp.getPage_no(), (short) ((i * 2 + 3)), new byte[0]);
        rc.deleteRecord(pp);
        fh.decreaseNum_of_record();
        rc.deleteRecord(pp);
        fh.decreaseNum_of_record();

        return 0;
    }

    public int writeIndex() throws Exception
    {
        fh.writeFile();
        for(int i = 0; i < ph.length; i++)
        {
            ph[i].writeData();
            ph[i].writePageHead();
        }
        return 0;
    }
}
