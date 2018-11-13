import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ScanLineFill extends JPanel{

    private int pointsNum;
    ArrayList<Point> vertices = new ArrayList<>();

    Graphics2D graphics;
    int[][] image = new int[600][600];
    ArrayList<Point>[] pointCount = new ArrayList[1000];
    ArrayList<Integer> horizontalY = new ArrayList<>();

    static File file;

    public static void main(String[] args) {
        ScanLineFill figure = new ScanLineFill();
        figure.takeInput();

        JFrame frame = new JFrame("Assignment 3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(figure);
        frame.setSize(1000, 1000);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        file = new File("scan_line_fill.pgm");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        graphics = g2d;

        g2d.setColor(Color.BLACK);
        g2d.setBackground(Color.CYAN);

        try {
            LineBresenham quadrilateral = new LineBresenham();

            for (int i = 0; i < pointCount.length; i++) {
                pointCount[i] = new ArrayList<>();
            }

            Point p1, p2;
            for (int i = 0; i < pointsNum; i++) {
                p1 = vertices.get(i%pointsNum);
                p2 = vertices.get((i+1)%pointsNum);

                //Do not draw horizontal lines
                if ((p1.y - p2.y != 0))
                    quadrilateral.drawLine(p1, p2);
            }


            scanFill();

            PGMIO pgmFile = new PGMIO();
            pgmFile.write(image, file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takeInput() {

        Scanner input = new Scanner(System.in);

        System.out.println("Enter the number of vertices");
        pointsNum = input.nextInt();

        int x, y;

        for (int i = 1; i <= pointsNum; i++) {
            x = input.nextInt();
            y = input.nextInt();
            vertices.add(new Point(x, y));
        }

        Point p;
        System.out.print("You entered the following coordinates: ");
        for (int i = 0; i < pointsNum; i++) {
            p = vertices.get(i);
            System.out.println(p.x + "\t" + p.y);
        }

        input.close();
    }

    public void scanFill() {
        boolean flag = true;

        for (int i = 0; i < 600; i++)
            for (int j = 0; j < 600; j++) {
                if (image[i][j] == 255) {
                    flag = !flag;
                }
                if (flag) {
                    image[i][j] = 255;
                }
            }
    }

    private boolean isVertex(Point point, ArrayList<Point> vertices) {
        for (Point p : vertices) {
            if (p.equals(point))
                return true;
        }
        return false;
    }


    public class LineBresenham extends JPanel {

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
                if(image[point.y][point.x] != 255) {
                    image[point.y][point.x] = 255;
                    pointCount[point.y].add(point);
                }

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
               if (image[point.y][point.x] != 255) {
                   image[point.y][point.x] = 255;
                   pointCount[point.y].add(point);
               }

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

                stream.close();
                System.out.println("Stream closed");
            } finally {
            }
        }

    }
}

