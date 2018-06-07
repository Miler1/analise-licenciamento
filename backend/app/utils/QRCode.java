package utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by luiz on 14/03/16.
 */
public class QRCode {

	private String chave;

	private QRCodeWriter qrCodeWriter;

	public QRCode(String chave) {
		this.chave = chave;
		this.qrCodeWriter = new QRCodeWriter();
	}

	private ByteArrayOutputStream gerarQRcode() throws IOException, WriterException {
		BitMatrix byteMatrix = qrCodeWriter.encode(chave, BarcodeFormat.QR_CODE, 150, 150);

		BufferedImage image = new BufferedImage(byteMatrix.getWidth(), byteMatrix.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, byteMatrix.getWidth(), byteMatrix.getHeight());
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < byteMatrix.getWidth(); i++) {
			for (int j = 0; j < byteMatrix.getHeight(); j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ImageIO.write(image, "PNG", out);

		return out;
	}

	public String getBase64() throws WriterException, IOException {

		return "data:image/png;base64," + Base64.encodeBase64String(this.gerarQRcode().toByteArray());

	}
}