package com.honghe.weather.utils;

/**
 * @author H.Kevin
 *
 */
public class NumberUtil {
	/**
	 * 把无符号int（用long)表示转换为4字节
	 * 第0字节是最低位，第3字节是最高位
	 * @param res
	 * @return
	 */
	public final static byte[] uint2byte(long res) {
		byte[] targets = new byte[4];
		targets[0] = (byte) (res & 0xff);//最低位
		targets[1] = (byte) ((res >> 8) & 0xff);//次低位
		targets[2] = (byte) ((res >> 16) & 0xff);//次高位
		targets[3] = (byte) (res >>> 24 & 0xff);//最高位,无符号右移。
		return targets;
	}
	
	/**
	 * 把无符号Short（用int)表示转换为2字节
	 * 第0字节是最低位，第1字节是最高位
	 * @param res
	 * @return
	 */
	public final static byte[] uShort2byte(int res) {
		byte[] targets = new byte[2];
		targets[0] = (byte) (res & 0xff);//最低位
		targets[1] = (byte) (res >>> 8 & 0xff);//最高位,无符号右移。
		return targets;
	}

	
	public final static String byte2uchar(byte b) {
		char[] hex = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		//char high = '0';
		//char low = '0';
		int high = (b>>>4) & 0x0f;
		char highChar = hex[high];
		int low = b & 0x0f;
		char lowChar = hex[low];
		return "" + highChar + lowChar;
	}
	/**
	 * 把四字节的无符号int用long来表示
	 * @param res
	 * @return
	 */
	public static final long byte2uint(byte[] res) {
		return (res[0] & 0xff)| ((res[1] << 8) & 0xff00) 
				| ((res[2] << 16) & 0xff0000 ) | ((res[3] << 24) & 0x00000000ff000000l);
		
	}
	
	public static final long byte2uint(byte[] res,int begin) {
		return (res[begin] & 0xff)| ((res[begin + 1] << 8) & 0xff00) 
				| ((res[begin+2] << 16) & 0xff0000 ) | ((res[begin + 3] << 24) & 0x00000000ff000000l);
		
	}
	
	/**
	 * 把两字节长度的转成int类型
	 */
	public static final int byte2Short(byte[] res) {
		return (res[0] & 0xff)| ((res[1] << 8) & 0xff00);		
	}
	
	public static final int byte2Short(byte[] res, int begin) {
		return (res[begin] & 0xff)| ((res[begin + 1] << 8) & 0xff00);		
	}
	/**
	 * res 需要8字节
	 * @param res
	 * @return
	 */
	public static final long byte2long(byte[] res) {
		return (res[0] & 0x0ffl)
			| ((res[1] << 8) & 0x0ff00l) 
			| ((res[2] <<16) & 0x0ff0000l )
			| ((res[3]<< 24) & 0x00000000ff000000l)
			| (((long)res[4] << 32) & 0x000000ff00000000l)
			| (((long)(res[5] << 20) <<20) & 0x0000ff0000000000l)
			| ((((long)(res[6] << 20) << 20) << 8 ) & 0x00ff000000000000l)
			| (((((long)(res[7] << 20) << 20) << 16) ) & 0xff00000000000000l)
			;		
	}
	
	public static final long byte2long(byte[] res, int begin) {
		return (res[begin] & 0x0ffl)
			| ((res[begin+1] << 8) & 0x0ff00l) 
			| ((res[begin+2] <<16) & 0x0ff0000l )
			| ((res[begin+3] << 24) & 0x00000000ff000000l)
			| (((long)res[begin+4] << 32) & 0x000000ff00000000l)
			| (((long)(res[begin+5] << 20) <<20) & 0x0000ff0000000000l)
			| ((((long)(res[begin+6] << 20) << 20) << 8 ) & 0x00ff000000000000l)
			| (((((long)(res[begin+7] << 20) << 20) << 16) ) & 0xff00000000000000l)
			;		
	}
	
	/**
	 * 把无符号int（用long)表示转换为4字节
	 * 第0字节是最低位，第3字节是最高位
	 * @param res
	 * @return
	 */
	public final static byte[] long2byte(long res) {
		byte[] targets = new byte[8];
		targets[0] = (byte) (res & 0x0ff);//最低位
		targets[1] = (byte) ((res >> 8) & 0xff);//次低位
		targets[2] = (byte) ((res >> 16) & 0xff);//次高位
		targets[3] = (byte) (res >> 24 & 0xff);//最高位,无符号右移。
		
		targets[4] = (byte) ((res >> 24) >> 8 & 0x0ff);//最低位
		targets[5] = (byte) ((res >> 24) >> 16 & 0x0ff);//次低位
		targets[6] = (byte) ((res >> 24) >> 24 & 0x0ff);//次高位
		targets[7] = (byte) (((res >> 24) >> 24) >>> 8 & 0xff);//最高位,无符号右移。

		return targets;
	}
	
	/*public static void main(String[] args) {
		// System.out.println(NumberTools.longToString(123));
		*//*byte b = (byte) 0xf1;
		System.out.println(b);*//*
		byte bs[] = new byte[8];
		bs[3] = (byte)0x00;
		bs[4] = (byte)0x00;		
		bs[5] = (byte)0x00;
		
		bs[6] = (byte)0xff;
		bs[7] = (byte)0xff;
		//byte result[] = NumberUtil.uint2byte(xx);
		*//*for (int i = 0; i < result.length; i++) {
			// System.out.println(result[i] & 0xff);
		}*//*

		System.out.println(NumberUtil.byte2long(bs));
		byte as[] = long2byte(byte2long(bs));
		System.out.println(as[6]&0xff);
		// System.out.println(b & 0xff);
	}*/
}
