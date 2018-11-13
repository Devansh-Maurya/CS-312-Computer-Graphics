import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class Assignment3 extends JPanel{
    Point p1 = new Point();
    Point p2 = new Point();
    Point p3 = new Point();
    Point p4 = new Point();

    public static final int rx = 100;
    public static final int ry = 50;
    public static final float shearFactor = .2f;
    public static final int ROTATE_X_OFFSET = 400;

    int rotateXLine;

    Point c = new Point();

    Graphics2D graphics;
    int[][] image = new int[1000][1000];

    File file;

    public static void main(String[] args) {
        Assignment3 figure = new Assignment3();
        figure.takeInput();

        JFrame frame = new JFrame("Assignment 3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(figure);
        frame.setSize(1000, 1000);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        graphics = g2d;

        g2d.setColor(Color.BLACK);
        g2d.setBackground(Color.CYAN);

        try {
            file = new File("assignment3.pgm");
            MidpointEllipse ellipse = new MidpointEllipse();
            ellipse.drawEllipse(c, rx, ry, 90);

            LineBresenham quadrilateral = new LineBresenham();

            int halfLength = 200;
            int halfWidth = rx;
            int widht = rx;

            Point point3 = new Point(c.x + halfLength, c.y + halfWidth);
            Point point4 = new Point(c.x - halfLength, c.y + halfWidth);
            Point point1 = new Point(point4.x + (int)((c.y - halfWidth)*shearFactor), c.y - halfWidth);
            Point point2 = new Point(point3.x + (int)((c.y - halfWidth)*shearFactor), c.y - halfWidth);

            quadrilateral.drawLine(point1, point2);
            quadrilateral.drawLine(point2, point3);
            quadrilateral.drawLine(point3, point4);
            quadrilateral.drawLine(point4, point1);

            PGMIO pgmFile = new PGMIO();
            pgmFile.write(image, file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takeInput() {
        System.out.print("Enter the coordinates of centre (xc, yc): ");
        Scanner input = new Scanner(System.in);
        c.x = input.nextInt();
        c.y = input.nextInt();

        System.out.print("You entered the following coordinates: ");
        System.out.println("(" + c.x + ", " + c.y + ")");

        rotateXLine = c.y + rx + ROTATE_X_OFFSET;

        input.close();
    }

    public class MidpointEllipse {

        public void drawEllipse(Point c, int rx, int ry, int angle) {
            Point p = new Point();
            p.x = 0;
            p.y = ry;

            int decisionParam1, decisionParam2;

            decisionParam1 = (int) (ry*ry + (0.25 - ry)*rx*rx);
            while (ry*ry*p.x <= rx*rx*p.y) {
                if (decisionParam1 > 0) {
                    p.x++;
                    p.y--;
                    decisionParam1 += (2*p.x + 1)*ry*ry - 2*p.y*rx*rx;
                } else {
                    p.x++;
                    decisionParam1 += (2*p.x + 1)*ry*ry;
                }
                plotRotatedEllipse(image, p, c, angle, graphics);
            }

            int tworxSq = 2*rx*rx;
            int tworySq = 2*ry*ry;

            decisionParam2 = (int) ((p.x + 0.5)*(p.x + 0.5)*ry*ry + (p.y - 1)*(p.y - 1)*rx*rx - rx*rx*ry*ry);

            while (p.y >= 0) {
                p.y--;
                if(decisionParam2 < 0) {
                    p.x++;
                    decisionParam2 += p.x*tworySq - p.y*tworxSq + rx*rx;
                } else {
                    decisionParam2 -= p.y*tworxSq + rx*rx;
                }
                plotRotatedEllipse(image, p, c, angle, graphics);
            }
        }

        public void plotRotatedEllipse(int[][] image, Point point, Point c, int angle, Graphics2D g2d) {
            double radAngle = angle*(Math.PI/180);
            Point rotatedPoint = new Point();
            rotatedPoint.x = (int)(point.x*Math.cos(radAngle) - point.y*Math.sin(radAngle));
            rotatedPoint.y = (int)(point.x*Math.sin(radAngle) + point.y*Math.cos(radAngle));

            plotEllipse2And4Quad(rotatedPoint, c, g2d);

            Point rotatedPointOppAngle = new Point();
            rotatedPointOppAngle.x = (int)(point.x*Math.cos(-radAngle) - point.y*Math.sin(-radAngle));
            rotatedPointOppAngle.y = (int)(point.x*Math.sin(-radAngle) + point.y*Math.cos(-radAngle));

            plotEllipse1And3Quad(rotatedPointOppAngle, c, g2d);
        }


        private void plotEllipse1And3Quad(Point p, Point c, Graphics2D g2d) {
            g2d.drawLine(-p.x + c.x, -p.y + c.y, -p.x + c.x, -p.y + c.y);
            g2d.drawLine(p.x + c.x, p.y + c.y, p.x + c.x, p.y + c.y);
            //Inverted about x = a line
            g2d.drawLine(-p.x + c.x, -(-p.y + c.y) + rotateXLine, -p.x + c.x, -(-p.y + c.y) + rotateXLine);
            g2d.drawLine(p.x + c.x, -(p.y + c.y) + rotateXLine, p.x + c.x, -(p.y + c.y) + rotateXLine);

            image[-p.y + c.y][-p.x + c.x] = 255;
            image[p.y + c.y][p.x + c.x] = 255;

            image[rotateXLine - (-p.y + c.y)][-p.x + c.x] = 255;
            image[rotateXLine - (p.y + c.y)][p.x + c.x] = 255;
        }


        private void plotEllipse2And4Quad(Point p, Point c,Graphics2D g2d) {
            g2d.drawLine(p.x + c.x, -p.y + c.y, p.x + c.x, -p.y + c.y);
            g2d.drawLine(-p.x + c.x, p.y + c.y, -p.x + c.x, p.y + c.y);

            g2d.drawLine(p.x + c.x, -(-p.y + c.y) + rotateXLine, p.x + c.x, -(-p.y + c.y) + rotateXLine);
            g2d.drawLine(-p.x + c.x, -(p.y + c.y) + rotateXLine, -p.x + c.x, -(p.y + c.y) + rotateXLine);

            image[-p.y + c.y][p.x + c.x] = 255;
            image[p.y + c.y][-p.x + c.x] = 255;
            //Inverted about x = a line
            image[rotateXLine - (-p.y + c.y)][p.x + c.x] = 255;
            image[rotateXLine - (p.y + c.y)][-p.x + c.x] = 255;
        }
    }

    public class LineBresenham extends JPanel {
        Point p1 = new Point();
        Point p2 = new Point();
        Point p3 = new Point();
        Point p4 = new Point();

        public void drawLine(Point p1, Point p2) {
            if (Math.abs(p2.y - p1.y ) < Math.abs(p2.x - p1.x)) {
                if (p1.x > p2.x) {
                    drawLowSlope(p2, p1);
                } else {
                    drawLowSlope(p1, p2);
                }
            } else {
                if (p1.y > p2.y) {
                    drawHighSlope(p2, p1);
                } else {
                    drawHighSlope(p1, p2);
                }
            }
        }

        private void drawLowSlope(Point p1, Point p2) {
            int dx, dy, change_y, p;
            Point point = new Point(0, 0);

            dx = p2.x - p1.x;
            dy = p2.y - p1.y;
            change_y = 1;

            if (dy < 0) {
                change_y = -1;
                dy = -dy;
            }

            p = 2*dy - dx;
            point.y = p1.y;

            for (point.x = p1.x; point.x <= p2.x; point.x++) {
                graphics.drawLine(point.x, point.y, point.x, point.y);
                //Reflection about x = a line
                graphics.drawLine(point.x, rotateXLine - point.y, point.x, rotateXLine - point.y);
                image[point.y][point.x] = 255;
                //Reflection about x = a line
                image[rotateXLine - point.y][point.x] = 255;

                if (p > 0) {
                    point.y = point.y + change_y;
                    p = p + 2*dy - 2*dx;
                } else {
                    p = p + 2*dy;
                }
            }
        }

        private void drawHighSlope(Point p1, Point p2) {
            Point point = new Point(0, 0);
            int dx, dy, p, change_x;

            dx = p2.x - p1.x;
            dy = p2.y - p1.y;
            change_x = 1;

            if (dx < 0) {
                change_x = -1;
                dx = -dx;
            }

            p = 2*dx - dy;
            point.x = p1.x;

            for (point.y = p1.y; point.y <= p2.y; point.y++) {
                graphics.drawLine(point.x, point.y, point.x, point.y);
                //Reflection about x = a line
                graphics.drawLine(point.x, rotateXLine - point.y, point.x, rotateXLine - point.y);
                image[point.y][point.x] = 255;
                //Reflection about x = a line
                image[rotateXLine - point.y][point.x] = 255;
                if (p > 0) {
                    point.x = point.x + change_x;
                    p = p + 2*dx - 2*dy;
                } else {
                    p = p + 2*dx;
                }
            }
        }
    }


    public final class PGMIO {

        private static final String MAGIC = "P5";

        private static final char COMMENT = '#';

        private static final int MAXVAL = 255;

        public void write(final int[][] image, final File file) throws IOException {
            write(image, file, MAXVAL);
        }

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
