import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.util.Scanner;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MidpointEllipse extends JPanel {
    private Point centre, rPoint;
    private int rx, ry;
    private double angle;
    int[][] image = new int[1000][1000];

    public static void main(String[] args) {
        MidpointEllipse ellipse = new MidpointEllipse();
        ellipse.takeInput();

        JFrame frame = new JFrame("Midpoint Ellipse Algorithm Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(ellipse);
        frame.setSize(1000, 1000);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void takeInput() {
        centre = new Point();
        System.out.print("Enter the coordinates of centre: ");
        Scanner input = new Scanner(System.in);
        centre.x = input.nextInt();
        centre.y = input.nextInt();

        System.out.print("Enter rx: ");
        rx = input.nextInt();
        System.out.print("Enter ry: ");
        ry = input.nextInt();

        rPoint = new Point();
        System.out.print("Enter rotation point: ");
        rPoint.x = input.nextInt();
        rPoint.y = input.nextInt();

        System.out.print("Enter the rotation angle: ");
        angle = input.nextFloat();
        angle = angle*(Math.PI/180);

        input.close();

        System.out.println("You entered the following:");
        System.out.println("Centre: (" + centre.x + "," + centre.y + ")");
        System.out.println("rx: " + rx);
        System.out.println("ry: " + ry);
        System.out.println("Rotation point: (" + rPoint.x + "," + rPoint.y + ")");
        System.out.println("Rotation angle: " + angle);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.GRAY);

        try {
            File file = new File("ellipse.pgm");

            file.createNewFile();

            Point point = new Point();

            point.x = 0;
            point.y = ry;

            int decisionParam1, decisionParam2;
            int a = 0, b = ry*ry;
            int c = 2*rx*rx*ry, d = 2*rx*rx;

            decisionParam1 = (int)(ry*ry + (0.25 - ry)*rx*rx);
            while (ry*ry*point.x <= rx*rx*point.y) {
                a = 2*a - b;
                if (decisionParam1 > 0) {
                    point.x++;
                    point.y--;
                    decisionParam1 += (2*point.x + 1)*ry*ry - 2*point.y*rx*rx;
                } else {
                    point.x++;
                    decisionParam1 += (2*point.x + 1)*ry*ry;
                }
                plotEllipse(point, centre, angle, g2d);
            }

            int tworxSq = 2*rx*rx;
            int tworySq = 2*ry*ry;

            decisionParam2 = (int)((point.x + 0.5)*(point.x + 0.5)*ry*ry + (point.y - 1)*(point.y - 1)*rx*rx - rx*rx*ry*ry);

            while (point.y >= 0) {
                point.y--;
                if (decisionParam2 < 0) {
                    point.x++;
                    decisionParam2 += point.x*tworySq - point.y*tworxSq + rx*rx;
                } else {
                    decisionParam2 -= point.y*tworxSq + rx*rx;
                }
                plotEllipse(point, centre, angle, g2d);
            }

            PGMIO pgmFile = new PGMIO();
            pgmFile.write(image, file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void plotEllipse(Point point, Point centre, Graphics2D g2d) {
        g2d.drawLine(-point.x + centre.x, -point.y + centre.y, -point.x + centre.x, -point.y + centre.y);
        g2d.drawLine(point.x + centre.x, point.y + centre.y, point.x + centre.x, point.y + centre.y);
        g2d.drawLine(point.x + centre.x, -point.y + centre.y, point.x + centre.x, -point.y + centre.y);
        g2d.drawLine(-point.x + centre.x, point.y + centre.y, -point.x + centre.x, point.y + centre.y);
    }

    public void plotEllipse1And3Quad(Point point, Point centre, Graphics2D g2d) {
        g2d.drawLine(-point.x + centre.x, -point.y + centre.y, -point.x + centre.x, -point.y + centre.y);
        g2d.drawLine(point.x + centre.x, point.y + centre.y, point.x + centre.x, point.y + centre.y);

        image[-point.y + centre.y][-point.x + centre.x] = 255;
        image[point.y + centre.y][point.x + centre.x] = 255;
    }

    public void plotEllipse2And4Quad(Point point, Point centre, Graphics2D g2d) {
        g2d.drawLine(point.x + centre.x, -point.y + centre.y, point.x + centre.x, -point.y + centre.y);
        g2d.drawLine(-point.x + centre.x, point.y + centre.y, -point.x + centre.x, point.y + centre.y);

        image[-point.y + centre.y][point.x + centre.x] = 255;
        image[point.y + centre.y][-point.x + centre.x] = 255;
    }

    public void plotEllipse(Point point, Point centre, Point rPoint, double angle, Graphics2D g2d) {
        Point rotatedPoint = new Point();

        rotatedPoint.x = (int)(rPoint.x + (point.x - rPoint.x)*Math.cos(angle) -
                (point.y - rPoint.y)*Math.sin(angle));

        rotatedPoint.y = (int)(rPoint.y + (point.x - rPoint.x)*Math.sin(angle) +
                (point.y - rPoint.y)*Math.cos(angle));

        Point rCentre = new Point();

        rCentre.x = (int)(rPoint.x + (centre.x - rCentre.x)*Math.cos(angle) -
                (point.y - rCentre.y)*Math.sin(angle));
        rCentre.y = (int)(rPoint.y + (centre.x - rCentre.x)*Math.sin(angle) +
                (point.y - rCentre.y)*Math.cos(angle));

        plotEllipse(rotatedPoint, rCentre, g2d);
    }

    public void plotEllipse(Point point, Point centre, double angle, Graphics2D g2d) {
        Point rCentre = new Point();
        rCentre.x = (int)(centre.x*Math.cos(angle) - point.y*Math.sin(angle));
        rCentre.y = (int)(centre.x*Math.sin(angle) + point.y*Math.cos(angle));

        //Rotate by theta and -theta otherwise the ellipse parts will intersect
        Point rotatedPoint = new Point();
        rotatedPoint.x = (int)(point.x*Math.cos(angle) - point.y*Math.sin(angle));
        rotatedPoint.y = (int)(point.x*Math.sin(angle) + point.y*Math.cos(angle));

        plotEllipse2And4Quad(rotatedPoint, centre, g2d);

        Point rotatedPointOppAngle = new Point();
        rotatedPointOppAngle.x = (int)(point.x*Math.cos(-angle) - point.y*Math.sin(-angle));
        rotatedPointOppAngle.y = (int)(point.x*Math.sin(-angle) + point.y*Math.cos(-angle));

        plotEllipse1And3Quad(rotatedPointOppAngle, centre, g2d);
    }

    private void initializeObjectShapeMatrix(int[][] image, Point point, Point centre) {
        //Reverse x and y axis to get correct image in PGM file
        image[point.y + centre.y][point.x + centre.x] = 255;
        image[point.y + centre.y][-point.x + centre.x] = 255;
        image[-point.y + centre.y][-point.x + centre.x] = 255;
        image[-point.y + centre.y][point.x + centre.x] = 255;
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
