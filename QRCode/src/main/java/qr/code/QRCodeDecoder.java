package qr.code;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class QRCodeDecoder {
       public static void main(String[] args) throws IOException {
    	   File imageFile = new File("C:/QR/QRGenerator.png");
          try {
        	  	 BufferedImage image = ImageIO.read(imageFile);
                 LuminanceSource source = new BufferedImageLuminanceSource(image);
                 BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                 Reader reader = new MultiFormatReader();
                 Result result = reader.decode(bitmap);
                 System.out.println("Barcode text: " + result.getText());
          } catch (Exception e) {
        	  System.out.println("The image is not in a recognizable QR format..");
                 //e.printStackTrace();
          }
     }
}