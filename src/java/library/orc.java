/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 *
 * @author lucas
 */
public class orc {

    public String getText(byte[] file) {

        try {
            File imageFile = new File("/home/lucas/Pictures/eurotext.png");


            //Tesseract instance = Tesseract.getInstance();  // JNA Interface Mapping
            Tesseract instance = new Tesseract(); // JNA Direct Mapping

            String result = instance.doOCR(imageFile);
            System.out.println(result);
            return result;
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
            return null;
        } catch (Exception ex) {
            return null;
        }
    }
}
