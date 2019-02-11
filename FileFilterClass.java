package com.staples.eCatalogue.utility;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.zip.ZipFile;
import java.io.FileInputStream;

public class FileFilterClass implements FileFilter {
	
	final static int BUFFER = 2048;
	public static byte[] MAGIC = { 'P', 'K', 0x3, 0x4 };

	private String fex;

	public FileFilterClass(String fex) {
		this.fex = fex.toLowerCase();	
	}

	public boolean accept(File file) {
		if (".zip".equals(fex)) {
			if (izf(file) && !irjf(file) && file.getName().toLowerCase().endsWith(fex) && ifu(file)) {
				return true;
			} else {
				return false;
			}
		} else {
			if (file.getName().toLowerCase().endsWith(fex) && ifu(file)) {
				return true;

			} else {
				return false;
			}
		}
	}

	public static boolean izf(File f) {
		boolean izp = true;
		byte[] buffer = new byte[MAGIC.length];
		try {
			RandomAccessFile raf = new RandomAccessFile(f, "r");
			raf.readFully(buffer);
			for (int i = 0; i < MAGIC.length; i++) {
				if (buffer[i] != MAGIC[i]) {
					izp = false;
					break;
				}    
			}    
			raf.close(); 			  
		} catch (Throwable e) {
			izp = false;
		}
		return izp; 
	}

	public static boolean irjf(File f) {
		boolean irj = false;
		try {
			ZipFile zip = new ZipFile(f);
			irj = zip.getEntry("META-INF/MANIFEST.MF") != null;
			zip.close();	  
		} catch (Throwable e) {
			irj = false;
		}
		return irj; 
	}	

	public static boolean ifu(File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			if (in!=null) {
				in.close();
			}
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
