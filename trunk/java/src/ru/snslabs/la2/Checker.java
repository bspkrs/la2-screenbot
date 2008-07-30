package ru.snslabs.la2;

import com.jniwrapper.DefaultLibraryLoader;
import com.jniwrapper.Function;
import com.jniwrapper.Int32;
import com.jniwrapper.Library;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.PrimitiveArray;
import com.jniwrapper.UInt32;
import com.jniwrapper.UInt8;
import ru.snslabs.la2.recognizer.Recognizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class Checker {
    public static final int BUFFER_SIZE = 10;
    private Function shoot;
    private int windowHandler;
    private Pointer.Void hWnd;
    private PrimitiveArray primitiveArray;
    private Int32 result;
    private Recognizer recognizer;
    private static final byte[] INVERT_TABLE = new byte[256];
    private static final byte[] CONTRAST_TABLE = new byte[256];

    {
        // initialization of color convertion tables
        for (int i = 0; i < 256; i++) {
            INVERT_TABLE[i] = (byte) (255 - i);
        }
        for (int i = 0; i < 256; i++) {
            if (i < 120) {
                CONTRAST_TABLE[i] = (byte) 0;
            }
            else {
                CONTRAST_TABLE[i] = (byte) 255;
            }
        }
    }

    private static final BufferedImageOp INVERT_OP = new LookupOp(new ByteLookupTable(0, INVERT_TABLE), null);
    private static final BufferedImageOp CONTRAST_OP = new LookupOp(new ByteLookupTable(0, CONTRAST_TABLE), null);
    private static final int SYMBOL_HEIGHT = 9;

    public static void main(String[] args) throws Exception {
        final Checker checker = new Checker(0x03880134);
        for(int i = 0; i < 100; i ++){
            try{
            checker.checkStatus();
            }catch(Exception e){
                System.out.println("Cannot parse...");
            }
            Thread.sleep(1000);
        }
    }

    public Checker(int windowHandler) {
        this.windowHandler = windowHandler;
        initScreenShooter();
        /** initializer of the recognizer */
        recognizer = new Recognizer();
    }

    public Status checkStatus() throws Exception {
        
        /* AUTOMATIC MODE */
        shootScreen();
        final File screenShot = new File("screenshot.bmp");
        
        /* MANUAL MODE */
//        final File screenShot = new File("C:\\Serge\\games\\Lineage II\\system\\Shot00003.bmp");
        
        
        final BufferedImage img = ImageIO.read(screenShot);
//        screenShot.delete();

        final Status status = new Status();

        populateHeroStatus(img, status);
        populateTargetInfo(img, status);

        System.out.println("Current status: \n" + status);
        return status;
    }

    private void populateTargetInfo(BufferedImage img, Status status) throws IOException {
        /**
         * preparing target info
         */
        BufferedImage subImage = img.getSubimage(660, 8, 140, 35);
        INVERT_OP.filter(subImage, subImage);
        CONTRAST_OP.filter(subImage, subImage);
        ImageIO.write(subImage, "BMP", new File("target.bmp"));

        {
            // recognizing target's name
            BufferedImage targetNameImage = subImage.getSubimage(0, 7, subImage.getWidth(), SYMBOL_HEIGHT);
            ImageIO.write(targetNameImage, "BMP", new File("targetname.bmp"));
            String targetName = recognizer.recognize(targetNameImage);
            //        recognizer.createRecognisionMaps(targetNameImage, "AmberBasilisk");
            status.setTargetName(targetName);
        }

        {
            // checking target's health
            subImage = img.getSubimage(660, 8, 140, 35);
//            INVERT_OP.filter(subImage, subImage);
//            BufferedImage targetNameImage = subImage.getSubimage(0, 7, subImage.getWidth(), SYMBOL_HEIGHT);
//            ImageIO.write(targetNameImage,"BMP",new File("targetHealth.bmp"));
            if (subImage.getRGB(3, 28) != 0xFFFFFFFF) {
                status.setTargetAlive(true);
                double lastHealthPixelX = 3;
                for (int x = 3; x < subImage.getWidth(); x++) {
                    if (subImage.getRGB(3, 28) != 0xFFFFFFFF) {
                        lastHealthPixelX = x;
                    }
                    else{
                        break;
                    }
                }
                status.setTargetHp(new BigDecimal(100*((double) (subImage.getWidth() - 3)) / lastHealthPixelX).setScale(2, BigDecimal.ROUND_HALF_DOWN));
            }
            else {
                status.setTargetAlive(false);
                status.setTargetHp(BigDecimal.ZERO);
            }
            ImageIO.write(subImage, "BMP", new File("targetHealth.bmp"));

        }


    }


    private void populateHeroStatus(BufferedImage img, Status status) throws IOException {
        /**
         * Preparing HP
         */
        final BufferedImage subImage = img.getSubimage(0, 0, 180, 80);

        INVERT_OP.filter(subImage, subImage);
        CONTRAST_OP.filter(subImage, subImage);

        // удаляем все цвета кроме чёрного
        for (int x = 0; x < subImage.getWidth(); x++) {
            for (int y = 0; y < subImage.getHeight(); y++) {
                if (subImage.getRGB(x, y) != 0xFF000000) {
                    subImage.setRGB(x, y, 0xFFFFFFFF);
                }
            }
        }
        // remove later... it is for debug only
        ImageIO.write(subImage, "BMP", new File("status.bmp"));

        // распознаём здоровье
        {
            BufferedImage healthImage = subImage.getSubimage(44, 40, 112, SYMBOL_HEIGHT);
            String result = recognizer.recognize(healthImage);
//            System.out.println("Recognized : " + result);
            ImageIO.write(healthImage, "BMP", new File("health.bmp"));

            status.setHp(Integer.parseInt(result.substring(0, result.indexOf("/"))));
            status.setMaxHp(Integer.parseInt(result.substring(result.indexOf("/") + 1)));
        }

        // распознаём ману
        {
            BufferedImage manaImage = subImage.getSubimage(44, 53, 112, SYMBOL_HEIGHT);
            String result = recognizer.recognize(manaImage);
//            System.out.println("Recognized : " + result);
            ImageIO.write(manaImage, "BMP", new File("mana.bmp"));

            status.setMp(Integer.parseInt(result.substring(0, result.indexOf("/"))));
            status.setMaxMp(Integer.parseInt(result.substring(result.indexOf("/") + 1)));
        }

        // распознаём опыт
        {
            BufferedImage manaImage = subImage.getSubimage(44, 66, 112, SYMBOL_HEIGHT);
            String result = recognizer.recognize(manaImage);
//            System.out.println("Recognized : " + result);
            ImageIO.write(manaImage, "BMP", new File("exp.bmp"));

            status.setExpPercent(new BigDecimal(result.replaceAll("%", "")));
        }


    }

    private void shootScreen() {
        shoot.invoke(result,
                new Parameter[]{
                        hWnd, new UInt32(1024), new UInt32(1024), new Pointer(primitiveArray), new UInt32(BUFFER_SIZE)
                });
    }

    private void initScreenShooter() {
        /** initialization of ScrennShooterDLL */
        hWnd = new Pointer.Void();
        hWnd.setValue(windowHandler);
        DefaultLibraryLoader.getInstance().addPath("C:\\Serge\\PROJECT_ROOT\\java\\ConsoleTry\\lib\\");
        Library screenShooterDll = new Library("ScreenShooterDLL.dll");
        shoot = screenShooterDll.getFunction("ScreenShot");
        primitiveArray = new PrimitiveArray(UInt8.class, BUFFER_SIZE);
        result = new Int32();

    }


}
