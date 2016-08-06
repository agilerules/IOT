package qr.code;
 
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
 
import javax.imageio.ImageIO;
 
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
 
/**
 * This Java class will generate the QR code in .png format pointing to sample URL
 */
 
public class QRCodeGenerator {
 
	// Tutorial: http://zxing.github.io/zxing/apidocs/index.html
 
	public static void main(String[] args) {
		String codeText = "http://agilerule.blogspot.in";
		String fileLocation = "C:/QR/QRGenerator.png";
		int size = 250;
		String fileType = "png";
		File myFile = new File(fileLocation);
		try {
			
			Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
 
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(codeText, BarcodeFormat.QR_CODE, size, size, hintMap);
			int byteMatrixWidth = byteMatrix.getWidth();
			int byteMatrixHeight = byteMatrixWidth; //Height and Width are same
			BufferedImage image = new BufferedImage(byteMatrixWidth, byteMatrixHeight, BufferedImage.TYPE_INT_RGB);
			image.createGraphics();
 
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, byteMatrixWidth, byteMatrixWidth);
			graphics.setColor(Color.BLACK);
			//Loop on Width and Height to fill the rectangle block of WHITE and BLACK patches
			for (int i = 0; i < byteMatrixWidth; i++) {
				for (int j = 0; j < byteMatrixHeight; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}
			ImageIO.write(image, fileType, myFile);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n\nYou have successfully created QR Code with the image created at the location: "+fileLocation);
	}
}