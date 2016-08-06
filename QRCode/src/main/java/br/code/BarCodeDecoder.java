package br.code;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class BarCodeDecoder {
	
	// Tutorial: https://dzone.com/articles/java-barcode-api
	 
	public static void main(String[] args) {
	
	InputStream barCodeInputStream;
	try {
		barCodeInputStream = new FileInputStream("C:/QR/CODE_128.png");
		//barCodeInputStream = new FileInputStream("C:/QR/PDF_147.png");
		BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);

		LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Reader reader = new MultiFormatReader();
		Result result = reader.decode(bitmap);

		System.out.println("Barcode text is " + result.getText());
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ChecksumException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (FormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}

}
