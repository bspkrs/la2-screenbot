package ru.snslabs.la2.recognizer;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Recognizer {
    public static final DecimalFormat DF = new DecimalFormat("0.00");
    private static final int SYMBOL_WIDTH = 7;

    public String recognize(BufferedImage img) {
        return recognizeSymbols(img);
    }

    private String recognizeSymbols(BufferedImage img) {
        int startPosition = 0;
        String result = "";

        while (startPosition < img.getWidth()) {
            if (hasBlackInColumn(img, startPosition)) {
                // looking for white column - space between characters
                int st = startPosition;
                for (; hasBlackInColumn(img, startPosition); startPosition++) ;
                final int charWidth = startPosition - st;
//                System.out.println("Symbol size:" + charWidth);
                result += recognizeSymbol(img, st, charWidth);
            }
            else {
                startPosition++;
            }

        }


        return result;
    }

    public void createRecognisionMaps(BufferedImage img, String expectingText) {

        int startPosition = 0;
        int index = 0;
        while (startPosition < img.getWidth()) {
            if (hasBlackInColumn(img, startPosition)) {
                // looking for white column - space between characters
                int st = startPosition;
                for (; hasBlackInColumn(img, startPosition); startPosition++) ;
                final int charWidth = startPosition - st;
//                System.out.println("Symbol size:" + charWidth);
                buildRecognisionMap(img, st, charWidth, expectingText.charAt(index++));
            }
            else {
                startPosition++;
            }

        }
    }

    private void buildRecognisionMap(BufferedImage img, int startPosition, int charWidth, char symbol) {
        int[][] map = new int[img.getHeight()][charWidth];

//        System.out.println("            recognisionMatrix.put('"+symbol+"', new double[][]\n" +
//                "                    {\n");
        for (int y = 0; y < img.getHeight(); y++) {
//            System.out.print("{");
            for (int x = 0; x < charWidth; x++) {
                map[y][x] = (img.getRGB(x + startPosition, y) == 0xFF000000) ? 1 : 0;
//                System.out.print((img.getRGB(x + startPosition, y) == 0xFF000000) ? "1" : " ");
            }
//            System.out.println("},");
        }
//        System.out.println("                    });");

        double[][] m = new double[img.getHeight()][charWidth];
        for (int y = 0; y < img.getHeight(); y++) {
            double totalInRow = 0;
            for (int x = 0; x < charWidth; x++) {
                totalInRow += map[y][x];
            }
            for (int x = 0; x < charWidth; x++) {
                m[y][x] = map[y][x] == 1 ? (1 / totalInRow) : 0;
            }

        }


        System.out.println("            recognisionMatrix.put('" + symbol + "', new double[][]\n" +
                "                    {\n");
        for (int y = 0; y < img.getHeight(); y++) {
            System.out.print("{");
            for (int x = 0; x < charWidth; x++) {
                System.out.print(DF.format(m[y][x]).replace(",", ".") + ",");
            }
            System.out.println("},");
        }
        System.out.println("                    });");
    }

    private char recognizeSymbol(BufferedImage img, int startPosition, int charWidth) {
        Character recognizedCharacter = null;
        double recognizedWeight = 0;
        for (Map.Entry<Character, double[][]> entry : getRecognisionMatrix().entrySet()) {
            double weight = 0;
            double[][] m = entry.getValue();
            for (int x = 0; x < charWidth; x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    if (y > m.length - 1 || x > m[y].length - 1) {
                        break;
                    }
                    if ((img.getRGB(x + startPosition, y) == 0xFF000000)) {
                        if (m[y][x] != 0) {
                            weight += m[y][x];
                        }
                        else {
                            weight -= 1;
                        }
                    }
                    weight += (1 - ((img.getRGB(x + startPosition, y) == 0xFFFFFFFF) ? m[y][x] : 0));
                }
            }
            if (weight > recognizedWeight) {
                recognizedWeight = weight;
                recognizedCharacter = entry.getKey();
            }
        }
//        System.out.println("recognized (" + recognizedCharacter + ") Weight=" + recognizedWeight / 8);
        return recognizedCharacter;
    }

    private boolean hasBlackInColumn(BufferedImage img, int startPosition) {
        if (startPosition >= img.getWidth()) {
            return false;
        }
        for (int i = 0; i < img.getHeight(); i++) {
            if (img.getRGB(startPosition, i) == 0xFF000000) {
                return true;
            }
        }
        return false;
    }


    private HashMap<Character, double[][]> recognisionMatrix = new HashMap<Character, double[][]>();

    public HashMap<Character, double[][]> getRecognisionMatrix() {
        if (recognisionMatrix.isEmpty()) {
            recognisionMatrix.put('1', new double[][]
                    {
                            {0.3, 0.5, 0.2, 0, 0, 0},
                            {0.1, 0.8, 0.1, 0, 0, 0},
                            {0.1, 0.8, 0.1, 0, 0, 0},
                            {0.1, 0.8, 0.1, 0, 0, 0},

                            {0.1, 0.8, 0.1, 0, 0, 0},
                            {0.1, 0.8, 0.1, 0, 0, 0},
                            {0.1, 0.8, 0.1, 0, 0, 0},
                            {0.3, 0.3, 0.3, 0.1, 0, 0}

                    });
            recognisionMatrix.put('2', new double[][]
                    {
                            {0.05, 0.3, 0.3, 0.3, 0.05, 0},
                            {0.45, 0.1, 0, 0.1, 0.45, 0},
                            {0, 0, 0, 0.2, 0.8, 0},
                            {0, 0, 0.1, 0.8, 0.1, 0},

                            {0, 0.1, 0.8, 0.1, 0, 0},
                            {0.1, 0.8, 0.1, 0, 0, 0},
                            {0.8, 0.2, 0, 0, 0, 0},
                            {0.2, 0.2, 0.2, 0.2, 0.2, 0},

                    });
            recognisionMatrix.put('3', new double[][]
                    {
                            {0.05, 0.3, 0.3, 0.3, 0.05, 0},
                            {0.45, 0.1, 0, 0.1, 0.45, 0},
                            {0, 0, 0, 0.2, 0.8, 0},
                            {0, 0.1, 0.4, 0.4, 0.1, 0},

                            {0, 0, 0, 0.2, 0.8, 0},
                            {0, 0, 0, 0.2, 0.8, 0},
                            {0.45, 0.1, 0, 0.1, 0.45, 0},
                            {0.05, 0.3, 0.3, 0.3, 0.05, 0},

                    });
            recognisionMatrix.put('4', new double[][]
                    {
                            {0, 0, 0, 0.4, 0.4, 0.2},
                            {0, 0, 0.3, 0.3, 0.3, 0.1},
                            {0, 0.2, 0.3, 0.1, 0.3, 0.1},
                            {0, 0.2, 0.3, 0.1, 0.3, 0.1},

                            {0.1, 0.3, 0.1, 0.1, 0.3, 0.1},
                            {0.15, 0.15, 0.15, 0.15, 0.25, 0.15},
                            {0, 0, 0, 0, 0.8, 0.2},
                            {0, 0, 0, 0, 0.8, 0.2},

                    });
            recognisionMatrix.put('5', new double[][]
                    {
                            {0.1, 0.2, 0.2, 0.2, 0.2, 0.1},
                            {0.8, 0.2, 0, 0, 0, 0},
                            {0.8, 0.2, 0, 0, 0, 0},
                            {0.25, 0.25, 0.25, 0.25, 0, 0},

                            {0, 0, 0, 0.2, 0.8, 0},
                            {0, 0, 0, 0.2, 0.8, 0},
                            {0.45, 0.1, 0, 0.1, 0.45, 0},
                            {0.05, 0.3, 0.3, 0.3, 0.05, 0},

                    });
            recognisionMatrix.put('6', new double[][]
                    {
                            {0, 0, 0.1, 0.8, 0.1, 0},
                            {0, 0.1, 0.8, 0.1, 0, 0},
                            {0.1, 0.8, 0.1, 0, 0, 0},
                            {0.05, 0.3, 0.3, 0.3, 0.05, 0},

                            {0.45, 0.1, 0, 0.1, 0.45, 0},
                            {0.45, 0.1, 0, 0.1, 0.45, 0},
                            {0.45, 0.1, 0, 0.1, 0.45, 0},
                            {0.05, 0.3, 0.3, 0.3, 0.05, 0},

                    });
            recognisionMatrix.put('7', new double[][]
                    {
                            {0.2, 0.2, 0.2, 0.2, 0.2, 0},
                            {0.45, 0.1, 0, 0.1, 0.45, 0},
                            {0, 0, 0, 0.2, 0.8, 0},
                            {0, 0, 0.1, 0.8, 0.1, 0},

                            {0, 0, 0.1, 0.8, 0.1, 0},
                            {0.1, 0.8, 0.1, 0, 0, 0},
                            {0.1, 0.8, 0.1, 0, 0, 0},
                            {0.1, 0.8, 0.1, 0, 0, 0},

                    });
            recognisionMatrix.put('8', new double[][]
                    {
                            {0.05, 0.3, 0.3, 0.3, 0.05, 0},
                            {0.4, 0.1, 0, 0.1, 0.4, 0},
                            {0.4, 0.1, 0, 0.1, 0.4, 0},
                            {0.05, 0.3, 0.3, 0.3, 0.05, 0},

                            {0.05, 0.3, 0.3, 0.3, 0.05, 0},
                            {0.4, 0.1, 0, 0.1, 0.4, 0},
                            {0.4, 0.1, 0, 0.1, 0.4, 0},
                            {0.05, 0.3, 0.3, 0.3, 0.05, 0}

                    });
            recognisionMatrix.put('9', new double[][]
                    {
                            {0.05, 0.3, 0.3, 0.3, 0.05, 0},
                            {0.45, 0.1, 0, 0.1, 0.45, 0},
                            {0.45, 0.1, 0, 0.1, 0.45, 0},
                            {0.45, 0.1, 0, 0.1, 0.45, 0},

                            {0.05, 0.3, 0.3, 0.3, 0.05, 0},
                            {0, 0, 0.1, 0.8, 0.1, 0},
                            {0, 0.1, 0.8, 0.1, 0, 0},
                            {0, 0.2, 0.8, 0, 0, 0},
                    });
            recognisionMatrix.put('0', new double[][]
                    {
                            {0, 0.1, 0.4, 0.4, 0.1, 0},
                            {0.2, 0.3, 0, 0, 0.3, 0.2},
                            {0.4, 0.1, 0, 0, 0.1, 0.4},
                            {0.4, 0.1, 0, 0, 0.1, 0.4},

                            {0.4, 0.1, 0, 0, 0.1, 0.4},
                            {0.4, 0.1, 0, 0, 0.1, 0.4},
                            {0.2, 0.3, 0, 0, 0.3, 0.2},
                            {0, 0.1, 0.4, 0.4, 0.1, 0},
                    });
            recognisionMatrix.put('/', new double[][]
                    {
                            {0, 0.1, 0.8, 0.1, 0, 0},
                            {0, 0.1, 0.8, 0.1, 0, 0},
                            {0, 0.1, 0.8, 0.1, 0, 0},
                            {0, 0.1, 0.8, 0.1, 0, 0},

                            {0.1, 0.8, 0.1, 0, 0, 0},
                            {0.1, 0.8, 0.1, 0, 0, 0},
                            {0.1, 0.8, 0.1, 0, 0, 0},
                            {0.7, 0.3, 0, 0, 0, 0}

                    });
            recognisionMatrix.put('.', new double[][]
                    {
                            {0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0},

                            {0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0},
                            {6, 0, 0, 0, 0, 0},

                    });
            recognisionMatrix.put('%', new double[][]
                    {
                            {0.1, 0.4, 0, 0, 0.1, 0.4},
                            {0.3, 0.05, 0.3, 0.05, 0.3, 0},
                            {0.3, 0.05, 0.3, 0.05, 0.3, 0},
                            {0.1, 0.4, 0, 0.4, 0.1, 0},

                            {0, 0, 0, 0.4, 0.1, 0.4, 0.1},
                            {0, 0, 0.3, 0.1, 0.3, 0, 0.3},
                            {0, 0, 0.3, 0.1, 0.3, 0, 0.3},
                            {0.1, 0.3, 0, 0, 0, 0.3, 0.3},

                    });
            // autofilled letters matrix
            recognisionMatrix.put('A', new double[][]
                    {

                            {0.00, 0.00, 0.00, 1.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.33, 0.33, 0.33, 0.00, 0.00,},
                            {0.00, 0.00, 0.50, 0.00, 0.50, 0.00, 0.00,},
                            {0.00, 0.00, 0.50, 0.00, 0.50, 0.00, 0.00,},
                            {0.00, 0.50, 0.00, 0.00, 0.00, 0.50, 0.00,},
                            {0.00, 0.20, 0.20, 0.20, 0.20, 0.20, 0.00,},
                            {0.00, 0.50, 0.00, 0.00, 0.00, 0.50, 0.00,},
                            {0.33, 0.33, 0.00, 0.00, 0.00, 0.00, 0.33,},
                            {0.50, 0.00, 0.00, 0.00, 0.00, 0.00, 0.50,},
                    });

            recognisionMatrix.put('m', new double[][]
                    {

                            {0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.14, 0.14, 0.14, 0.14, 0.00, 0.14, 0.14, 0.14, 0.00,},
                            {0.33, 0.00, 0.00, 0.00, 0.33, 0.00, 0.00, 0.00, 0.33,},
                            {0.33, 0.00, 0.00, 0.00, 0.33, 0.00, 0.00, 0.00, 0.33,},
                            {0.33, 0.00, 0.00, 0.00, 0.33, 0.00, 0.00, 0.00, 0.33,},
                            {0.33, 0.00, 0.00, 0.00, 0.33, 0.00, 0.00, 0.00, 0.33,},
                            {0.33, 0.00, 0.00, 0.00, 0.33, 0.00, 0.00, 0.00, 0.33,},
                            {0.33, 0.00, 0.00, 0.00, 0.33, 0.00, 0.00, 0.00, 0.33,},
                    });

            recognisionMatrix.put('b', new double[][]
                    {

                            {1.00, 0.00, 0.00, 0.00, 0.00,},
                            {1.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.25, 0.25, 0.25, 0.25, 0.00,},
                            {0.33, 0.00, 0.00, 0.33, 0.33,},
                            {0.50, 0.00, 0.00, 0.00, 0.50,},
                            {0.50, 0.00, 0.00, 0.00, 0.50,},
                            {0.50, 0.00, 0.00, 0.00, 0.50,},
                            {0.33, 0.00, 0.00, 0.33, 0.33,},
                            {0.25, 0.25, 0.25, 0.25, 0.00,},
                    });

            recognisionMatrix.put('e', new double[][]
                    {

                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.33, 0.33, 0.33, 0.00,},
                            {0.33, 0.33, 0.00, 0.00, 0.33,},
                            {0.50, 0.00, 0.00, 0.00, 0.50,},
                            {0.20, 0.20, 0.20, 0.20, 0.20,},
                            {1.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.50, 0.50, 0.00, 0.00, 0.00,},
                            {0.00, 0.25, 0.25, 0.25, 0.25,},
                    });

            recognisionMatrix.put('r', new double[][]
                    {

                            {0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00,},
                            {0.33, 0.33, 0.33,},
                            {1.00, 0.00, 0.00,},
                            {1.00, 0.00, 0.00,},
                            {1.00, 0.00, 0.00,},
                            {1.00, 0.00, 0.00,},
                            {1.00, 0.00, 0.00,},
                            {1.00, 0.00, 0.00,},
                    });

            recognisionMatrix.put('B', new double[][]
                    {

                            {0.25, 0.25, 0.25, 0.25, 0.00, 0.00,},
                            {0.50, 0.00, 0.00, 0.00, 0.50, 0.00,},
                            {0.50, 0.00, 0.00, 0.00, 0.50, 0.00,},
                            {0.33, 0.00, 0.00, 0.33, 0.33, 0.00,},
                            {0.25, 0.25, 0.25, 0.25, 0.00, 0.00,},
                            {0.33, 0.00, 0.00, 0.00, 0.33, 0.33,},
                            {0.50, 0.00, 0.00, 0.00, 0.00, 0.50,},
                            {0.33, 0.00, 0.00, 0.00, 0.33, 0.33,},
                            {0.20, 0.20, 0.20, 0.20, 0.20, 0.00,},
                    });

            recognisionMatrix.put('a', new double[][]
                    {

                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.25, 0.25, 0.25, 0.25, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 1.00,},
                            {0.00, 0.00, 0.00, 0.50, 0.50,},
                            {0.25, 0.25, 0.25, 0.00, 0.25,},
                            {0.50, 0.00, 0.00, 0.00, 0.50,},
                            {0.33, 0.00, 0.00, 0.33, 0.33,},
                            {0.00, 0.25, 0.25, 0.25, 0.25,},
                    });

            recognisionMatrix.put('s', new double[][]
                    {

                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.33, 0.33, 0.33, 0.00,},
                            {1.00, 0.00, 0.00, 0.00, 0.00,},
                            {1.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.33, 0.33, 0.33, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 1.00,},
                            {0.00, 0.00, 0.00, 0.00, 1.00,},
                            {0.25, 0.25, 0.25, 0.25, 0.00,},
                    });

            recognisionMatrix.put('i', new double[][]
                    {

                            {1.00,},
                            {0.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                    });

            recognisionMatrix.put('l', new double[][]
                    {

                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                    });
            recognisionMatrix.put('i', new double[][]
                    {

                            {1.00,},
                            {0.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                    });
            recognisionMatrix.put('s', new double[][]
                    {

                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.33, 0.33, 0.33, 0.00,},
                            {1.00, 0.00, 0.00, 0.00, 0.00,},
                            {1.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.33, 0.33, 0.33, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 1.00,},
                            {0.00, 0.00, 0.00, 0.00, 1.00,},
                            {0.25, 0.25, 0.25, 0.25, 0.00,},
                    });
            recognisionMatrix.put('k', new double[][]
                    {

                            {1.00, 0.00, 0.00, 0.00, 0.00,},
                            {1.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.50, 0.00, 0.00, 0.50, 0.00,},
                            {0.33, 0.00, 0.33, 0.33, 0.00,},
                            {0.33, 0.33, 0.33, 0.00, 0.00,},
                            {0.33, 0.33, 0.33, 0.00, 0.00,},
                            {0.50, 0.00, 0.50, 0.00, 0.00,},
                            {0.50, 0.00, 0.00, 0.50, 0.00,},
                            {0.33, 0.00, 0.00, 0.33, 0.33,},
                    });

            // рыбалка
            recognisionMatrix.put('Р', new double[][]
                    {

                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.25, 0.25, 0.25, 0.25, 0.00,},
                            {0.33, 0.00, 0.00, 0.33, 0.33,},
                            {0.50, 0.00, 0.00, 0.00, 0.50,},
                            {0.50, 0.00, 0.00, 0.00, 0.50,},
                            {0.50, 0.00, 0.00, 0.50, 0.00,},
                            {0.33, 0.33, 0.33, 0.00, 0.00,},
                    });
            recognisionMatrix.put('b', new double[][]
                    {

                            {0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00,},
                            {1.00, 0.00, 0.00, 0.00,},
                            {1.00, 0.00, 0.00, 0.00,},
                            {1.00, 0.00, 0.00, 0.00,},
                            {0.25, 0.25, 0.25, 0.25,},
                    });
            recognisionMatrix.put('I', new double[][]
                    {

                            {0.00,},
                            {0.00,},
                            {0.00,},
                            {0.00,},
                            {0.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                            {1.00,},
                    });
            recognisionMatrix.put('б', new double[][]
                    {

                            {0.00, 0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.33, 0.33, 0.33, 0.00, 0.00,},
                            {1.00, 0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.20, 0.20, 0.20, 0.20, 0.20, 0.00,},
                            {0.50, 0.00, 0.00, 0.00, 0.50, 0.00,},
                            {0.50, 0.00, 0.00, 0.00, 0.00, 0.50,},
                            {0.50, 0.00, 0.00, 0.00, 0.00, 0.50,},
                    });
            recognisionMatrix.put('а', new double[][]
                    {

                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.33, 0.33, 0.33, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 1.00,},
                            {0.00, 0.00, 0.00, 0.00, 1.00,},
                            {0.33, 0.33, 0.00, 0.00, 0.33,},
                    });
            recognisionMatrix.put('л', new double[][]
                    {

                            {0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00,},
                            {0.25, 0.25, 0.25, 0.25,},
                            {0.50, 0.00, 0.00, 0.50,},
                            {0.50, 0.00, 0.00, 0.50,},
                            {0.50, 0.00, 0.00, 0.50,},
                    });
            recognisionMatrix.put('к', new double[][]
                    {

                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.33, 0.00, 0.00, 0.33, 0.33,},
                            {0.50, 0.00, 0.00, 0.50, 0.00,},
                            {0.50, 0.00, 0.50, 0.00, 0.00,},
                            {0.33, 0.33, 0.33, 0.00, 0.00,},
                    });
            recognisionMatrix.put('а', new double[][]
                    {

                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 0.00,},
                            {0.00, 0.33, 0.33, 0.33, 0.00,},
                            {0.00, 0.00, 0.00, 0.00, 1.00,},
                            {0.00, 0.00, 0.00, 0.00, 1.00,},
                            {0.33, 0.33, 0.00, 0.00, 0.33,},
                    });

        }
        return recognisionMatrix;
    }
}
