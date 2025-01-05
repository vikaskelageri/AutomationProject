package Ai.Attri.Utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class TakeSnapShotWithHeader {
	
	public static void updateHeaderUrl(String currentUrl) throws IOException {
		BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir")+"\\Attri.png"));
	    Graphics g = image.getGraphics();
	    g.setFont(g.getFont().deriveFont(14f));
	    g.setColor(Color.BLACK);
	    g.drawString(currentUrl, 140, 20);
	    g.dispose();
	    ImageIO.write(image, "png", new File(System.getProperty("user.dir")+"\\Attriheader.png"));
		
	}
	
	public static void verticallyAttachSnapShot(){//Processing pictures vertically
		try {
			/* 1 Read the first picture*/
			File fileOne = new File(System.getProperty("user.dir")+"\\Attriheader.png");
			BufferedImage imageFirst = ImageIO.read(fileOne);
			int width = imageFirst.getWidth();//Image width
			int height = imageFirst.getHeight();//Image height
			int[] imageArrayFirst = new int[width * height];//read RGB from the picture
			imageArrayFirst = imageFirst.getRGB(0, 0, width, height, imageArrayFirst, 0, width);

			/* 1 Do the same for the second picture*/
			File fileTwo = new File(System.getProperty("user.dir")+"\\snapshot.png");
			BufferedImage image2= ImageIO.read(fileTwo);
			int width2 = image2.getWidth();//Image width
			int height2 = image2.getHeight();//Image height
			int[] imageArray2= new int[width2 * height2];
			imageArray2= image2.getRGB(0, 0, width, height2, imageArray2, 0, width);
			
			//Generate a new picture 
			BufferedImage imageResult = new BufferedImage(width, height +height2,BufferedImage.TYPE_INT_RGB);
			imageResult.setRGB(0, 0, width, height, imageArrayFirst, 0, width);//Set the RGB of the left half
			imageResult.setRGB(0, height, width, height2, imageArray2, 0, width);//Set the RGB of the right half
			File outFile = new File(System.getProperty("user.dir")+"\\output.png");
			ImageIO.write(imageResult, "png", outFile);//Write pictures
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
