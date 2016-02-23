package com.example.administrator.datainterface;

import android.util.Log;

import com.example.administrator.utils.DataToData;
import com.example.administrator.utils.UseData;

/**
 * Created by HBL on 2015/12/25.
 */
public class NetPack {
    //包头标志
    private int m_Start; // 包头标志
    private int m_Crc;
    private int nDataLen;
    private int m_nFlag;
    private byte[] m_buffer;

    //除了数据以外的信息size
    public static int infoSize = 2 + 2 + 2 + 2;
    // 该pack总size
    public int size = infoSize;
    private byte buf[];

    public byte[] getM_buffer() {
        return m_buffer;
    }

    public void setM_buffer(byte[] m_buffer) {
        this.m_buffer = m_buffer;
    }

    public NetPack() {
        Reset();
    }

    public int getM_Start() {
        return m_Start;
    }

    public void setM_Start(int m_Start) {
        this.m_Start = m_Start;
    }

    public int getM_Crc() {
        return m_Crc;
    }

    public void setM_Crc(int m_Crc) {
        this.m_Crc = m_Crc;
    }

    public int getnDataLen() {
        return nDataLen;
    }

    public void setnDataLen(int nDataLen) {
        this.nDataLen = nDataLen;
    }

    public int getM_nFlag() {
        return m_nFlag;
    }

    public void setM_nFlag(int m_nFlag) {
        this.m_nFlag = m_nFlag;
    }

    public void Reset() {
        m_Start = UseData.PACK_START_FLAG;
        m_Crc = -1;
        nDataLen = 0;
        m_nFlag = 0;
    }


    // 按byte加在一起，模65536，返回一个0-65535的数据设置成m_Crc
    public void CalCRC() {
        int sum = 0;
        for (int i = 0; i < nDataLen; i++) {
            sum += m_buffer[i];
        }
        this.m_Crc = sum % 65536;
        byte[] temp = DataToData.shortTobyte(m_Crc);
        Log.e("CalCRC", "temp" + temp.length);
        System.arraycopy(temp, 0, this.buf, 2, temp.length);
    }

    // 返回要发送的byte数组
    public byte[] getBuf() {
        return buf;
    }

    // 构造并转化
    public NetPack(int m_Crc, int nDataLen, int m_nFlag, byte[] m_buffer) {
        this.m_Start = UseData.PACK_START_FLAG;
        this.m_Crc = m_Crc;
        this.nDataLen = nDataLen;
        this.m_nFlag = m_nFlag;
        this.m_buffer = m_buffer;

        int truesize = infoSize + m_buffer.length;
        buf = new byte[truesize];

        // m_Start(2)2个字节
        byte[] temp = DataToData.shortTobyte(m_Start);
        System.arraycopy(temp, 0, buf, 0, temp.length);

        // m_Crc(2)2个字节
        temp = DataToData.shortTobyte(m_Crc);
        System.arraycopy(temp, 0, buf, 2, temp.length);

        // nDataLen(2)2个字节
        temp = DataToData.shortTobyte(size);
        System.arraycopy(temp, 0, buf, 4, temp.length);

        // m_nFlag 2个字节
        temp = DataToData.shortTobyte(m_nFlag);
        System.arraycopy(temp, 0, buf, 6, temp.length);

        // m_buffer
        System.arraycopy(m_buffer, 0, buf, 8, m_buffer.length);
    }
}
