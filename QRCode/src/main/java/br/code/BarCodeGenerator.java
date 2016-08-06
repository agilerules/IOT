package br.code;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
 
/**
 * This Java class will generate the Bar code in .png format pointing to sample URL
 */
 
public class BarCodeGenerator {
 
	// Tutorial: http://zxing.github.io/zxing/apidocs/index.html
 
	public static void main(String[] args) {
		
		int width = 400;
		int height = 300; 
		String fileType = "png";
			
		Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
		hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
 
		BitMatrix bitMatrix;
		Writer writer;
		try {
			// Write Barcode in CODE_128 format
			String codeText = "http://agilerule.blogspot.in";
			String fileLocation = "C:/QR/CODE_128.png";
			writer = new Code128Writer();
			bitMatrix = writer.encode(codeText, BarcodeFormat.CODE_128, width, height, hintMap);
			MatrixToImageWriter.writeToStream(bitMatrix, fileType, new FileOutputStream(new File(fileLocation)));
			System.out.println("\n\nYou have successfully created Barcode 128 Code with the image created at the location: "+fileLocation);
			
			// Write Barcode in PDF_147 format
			codeText = "http://agilerule.blogspot.in";
			fileLocation = "C:/QR/PDF_147.png";
			writer = new PDF417Writer();
            bitMatrix = writer.encode(codeText, BarcodeFormat.PDF_417, width, height);
            MatrixToImageWriter.writeToStream(bitMatrix, fileType, new FileOutputStream(new File(fileLocation)));
            System.out.println("\n\nYou have successfully created PDF147 Code with the image created at the location: "+fileLocation);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	
		
	}
}