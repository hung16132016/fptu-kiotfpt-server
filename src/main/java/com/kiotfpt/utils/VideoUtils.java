package com.kiotfpt.utils;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class VideoUtils {

	public static byte[] decompressData(byte[] data) {
	    Inflater inflater = new Inflater();
	    inflater.setInput(data);
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
	    byte[] buffer = new byte[4 * 1024];

	    try {
	        while (!inflater.finished()) {
	            int count = inflater.inflate(buffer);
	            outputStream.write(buffer, 0, count);
	        }
	    } catch (DataFormatException e) {
	        // Handle the exception (e.g., log it)
	        throw new RuntimeException("Error decompressing data", e);
	    } finally {
	        try {
	            outputStream.close();
	        } catch (IOException e) {
	            // Handle the exception (e.g., log it)
	            throw new RuntimeException("Error closing output stream", e);
	        }
	    }

	    return outputStream.toByteArray();
	}

}
