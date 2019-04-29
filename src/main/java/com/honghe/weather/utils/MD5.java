package com.honghe.weather.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class MD5 
{
	public String computeMd5Value(byte[] b) {
		MessageDigest md5 = null;
		try {
			md5=MessageDigest.getInstance("MD5");
		} catch(Exception e) {
			e.printStackTrace();
		}
		byte[] md5Result = md5.digest(b);
		String value = "";
        for (int i = 0;i < 16;i++) {
        	value += NumberUtil.byte2uchar(md5Result[i]);
        }
        return value;
	}
	
	
	public String computeMd5Value(String value) {		
		return computeMd5Value(value.getBytes());
	}
	
	/**
	 * @param length
	 * @return
	 */
	public int getMD5Length(long length) {
		int result = 0;
		if (length <= 100 * 1000){
			return (int)length; 
		} else if(length <= 1 * 1000 * 1000){
			result = 1000 * ((int)length / 10000);
			if(length % 10000 == 0) {
				return result;
			} else {
				return result + 1000;
			}
		} else if(length <= 10 * 1000 * 1000){
			result = 1000 * ((int)length / 100000);
			if (length % 100000 == 0){
				return result;
			} else {
				return result + 1000;
			}
		} else {
			result = 1000 * ((int)length / 1000000);
			if (length % 1000000 == 0){
				return result;
			} else {
				return result + 1000;
			}
		}
	}
	
	public String computeFileMd5Value(String absoluteFileName) {
		
		File f= new File(absoluteFileName);
		if(!f.exists() || (!f.isFile()) || f.length() == 0 ){
			return null;
		}
		
		FileInputStream fis=null;
		int bSize = getMD5Length(f.length());
		byte[] fileBytes = new byte[bSize] ;
		
		try {			
		    fis = new FileInputStream(f);
		    if(f.length() <= 100 * 1000){
		    	fis.read(fileBytes);
		    } else if(f.length() <= 1 * 1000 * 1000){
				int visited = 0;
				int read = 0;
				for(int i=0;;i++) {
					if((visited + 1000) > f.length()) {
						fis.read(fileBytes, read, (int)(f.length() - visited));
						break;
					}
					fis.read(fileBytes, read, 1000);
					fis.skip(10000 - 1000);
					
					visited += 10*1000;
					read += 1000;
					if (visited>=f.length()) {
						break;
					}					
				}
			} else if (f.length() <= 10 * 1000 * 1000) {
				int visited = 0;
				int read = 0;
				for (int i = 0;;i++){
					if ((visited + 1000) > f.length()) {
						fis.read(fileBytes, read, (int)(f.length() - visited));
						break;
					}
					fis.read(fileBytes, read, 1000);
					fis.skip(100000-1000);
					
					visited += 100*1000;
					read += 1000;
					if(visited >= f.length()){
						break;
					}					
				}
			} else {
				int visited = 0;
				int read = 0;
				for (int i = 0;;i++){
					if ((visited + 1000) > f.length()) {
						fis.read(fileBytes, read, (int)(f.length() - visited));
						break;
					}
					fis.read(fileBytes, read, 1000);
					fis.skip(1000000 - 1000);
					
					visited += 1000 * 1000;
					read += 1000;
					if(visited >= f.length()){
						break;
					}					
				}
			}		    
			fis.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
        String md5Value=computeMd5Value(fileBytes);
        
		return md5Value;
	}
	
	/*public static void main(String[] args) {
		MD5 md5=new MD5();
		String absoluteFileName = "D:\\source code\\WinSign_xhs\\bin\\netmanager\\chinese debug\\server daemon\\resources\\58F565990BAE7349E659AE4A4C079EA6";
		String md5Res = md5.computeFileMd5Value(absoluteFileName);
		System.out.println(md5Res);
	}*/
}
