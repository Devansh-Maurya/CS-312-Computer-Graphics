import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

    public class MidpointCircle extends JPanel {
        Point centre = new Point();
        int radius; 

    public static void main(String[] args) {
        MidpointCircle circle = new MidpointCircle();
        circle.takeInput();

        JFrame frame = new JFrame("Midpoint Circle Algorithm Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(circle);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void takeInput() {

        System.out.print("Enter the coordinates of centre: ");
        Scanner input = new Scanner(System.in);
        centre.x = input.nextInt();
        centre.y = input.nextInt();

        System.out.print("Enter the radius: ");;
        radius = input.nextInt();
        input.close();

        System.out.println("You entered the following centre: " + 
            centre.x + ", " + centre.y);
        System.out.println("You entered the following radius: " + radius);    
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.GRAY);

        try {
            File file = new File("circle.pgm");

            file.createNewFile();

            Point leftCentre = new Point(centre.x - 2*radius, centre.y);
            Point rightCentre = new Point(centre.x + 2*radius, centre.y);

            Point point = new Point();
            int decisionParam;
            int[][] image = new int[1000][1000];
            point.x = 0;
            point.y = radius;

            decisionParam = 1 - radius;

            while (point.x <= point.y) {
                if (decisionParam > 0) {
                    point.x = point.x + 1;
                    point.y = point.y - 1;
                    decisionParam = decisionParam + 2 * (point.x - point.y) + 1;
                } else {
                    point.x = point.x + 1;
                    decisionParam = decisionParam + 2*point.x + 1;
                }
                plotCircle(point, centre, g2d);
                plotLowerSemiCircle(point, leftCentre, g2d);
                plotUpperSemiCircle(point, rightCentre, g2d);

                initializeObjectShapeMatrix(image, point, centre);
                initializeLowerSemiCircleMatrix(image, point, leftCentre);
                initializeUpperSemiCircleMatrix(image, point, rightCentre); 
            }

            g2d.drawLine(leftCentre.x - radius, centre.y, rightCentre.x + radius, centre.y);
            drawLineInPGM(image, leftCentre.x - radius, centre.y, rightCentre.x + radius, centre.y);

            PGMIO pgmFile = new PGMIO();
            pgmFile.write(image, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void plotCircle(Point point, Point centre, Graphics2D g2d) {
        plotUpperSemiCircle(point, centre, g2d);
        plotLowerSemiCircle(point, centre, g2d);
    }

    public void plotUpperSemiCircle(Point point, Point centre, Graphics2D g2d) {
        g2d.drawLine(point.x + centre.x, point.y + centre.y, point.x + centre.x, point.y  + centre.y);
        g2d.drawLine(-point.x + centre.x, point.y + centre.y, -point.x + centre.x, point.y + centre.y);
        g2d.drawLine(point.y + centre.x, point.x + centre.y, point.y + centre.x, point.x + centre.y);
        g2d.drawLine(-point.y + centre.x, point.x + centre.y, -point.y + centre.x, point.x + centre.y);        
    }

    public void plotLowerSemiCircle(Point point, Point centre, Graphics2D g2d) {
        g2d.drawLine(-point.x + centre.x, -point.y + centre.y, -point.x + centre.x, -point.y + centre.y);
        g2d.drawLine(point.x + centre.x, -point.y + centre.y, point.x + centre.x, -point.y + centre.y);
        g2d.drawLine(-point.y + centre.x, -point.x + centre.y, -point.y + centre.x, -point.x + centre.y);
        g2d.drawLine(point.y + centre.x, -point.x + centre.y, point.y + centre.x, -point.x + centre.y);
    }

    private void initializeObjectShapeMatrix(int[][] image, Point point, Point centre) {
        initializeLowerSemiCircleMatrix(image, point, centre);
        initializeUpperSemiCircleMatrix(image, point, centre);
    }

    private void initializeUpperSemiCircleMatrix(int[][] image, Point point, Point centre) {
        //Reverse x and y axis to get correct image in PGM file
        image[point.y + centre.y][point.x + centre.x] = 255;
        image[point.y + centre.y][-point.x + centre.x] = 255;
        image[point.x + centre.y][point.y + centre.x] = 255;
        image[point.x + centre.y][-point.y + centre.x] = 255;
    }

    private void initializeLowerSemiCircleMatrix(int[][] image, Point point, Point centre) {
        image[-point.y + centre.y][point.x + centre.x] = 255;
        image[-point.y + centre.y][-point.x + centre.x] = 255;
        image[-point.x + centre.y][point.y + centre.x] = 255;
        image[-point.x + centre.y][-point.y + centre.x] = 255;
    }

    private void drawLineInPGM(int[][] image, int x1, int y1, int x2, int y2) {
        for(int i = x1; i <= x2; i++) {
            image[y1][i] = 255;
        }
    }

    
    public final class PGMIO {

        private static final String MAGIC = "P5";
        /**
         * Character indicating a comment.
         */
        private static final char COMMENT = '#';
        /**
         * The maximum gray value.
         */
        private static final int MAXVAL = 255;

        /**
         * Writes a grayscale image to a file in PGM format.
         * @param image a two-dimensional byte array representation of the image
         * @param file the file to write to
         * @throws IllegalArgumentException
         * @throws IOException
         */
        public void write(final int[][] image, final File file) throws IOException {
            write(image, file, MAXVAL);
        }

        /**
         * Writes a grayscale image to a file in PGM format.
         * @param image a two-dimensional byte array representation of the image
         * @param file the file to write to
         * @param maxval the maximum gray value
         * @throws IllegalArgumentException
         * @throws IOException
         */
        public void write(final int[][] image, final File file, final int maxval) throws IOException {
            if (maxval > MAXVAL)
                throw new IllegalArgumentException("The maximum gray value cannot exceed " + MAXVAL + ".");
            final BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            try {
                stream.write(MAGIC.getBytes());
                stream.write("\n".getBytes());
                stream.write(Integer.toString(image[0].length).getBytes());
                stream.write(" ".getBytes());
                stream.write(Integer.toString(image.length).getBytes());
                stream.write("\n".getBytes());
                stream.write(Integer.toString(maxval).getBytes());
                stream.write("\n".getBytes());
                for (int i = 0; i < image.length; ++i) {
                    for (int j = 0; j < image[0].length; ++j) {
                        final int p = image[i][j];
                        if (p < 0 || p > maxval)
                            throw new IOException("Pixel value " + p + " outside of range [0, " + maxval + "].");
                        stream.write(image[i][j]);
                    }
                }
            } finally {
                stream.close();
            }
        }

    }
}