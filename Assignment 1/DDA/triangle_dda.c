//Implementation of Bresenham's Line Drawing Algorithm
//to draw a triangle

#include <graphics.h>
#include <stdio.h>
#include<stdlib.h>
#include <math.h>
#include <dos.h>

void line_dda(int, int, int, int);
void input_points(int *, int *, int);
int check_triangle(int, int, int, int, int, int);

void main() {
	int x1, y1, x2, y2, x3, y3;
	int gd=DETECT,gm;

	initgraph(&gd, &gm, "C:\\TC\\BGI");

	input_points(&x1, &y1, 1);
	input_points(&x2, &y2, 2);
	input_points(&x3, &y3, 3);

	if (check_triangle(x1, y1, x2, y2, x3, y3))
	{
		line_dda(x1, y1, x2, y2);
		line_dda(x2, y2, x3, y3);
		line_dda(x3, y3, x1, y1);
	}
	else
	{
		printf("A triangle can't be drawn from the given points\n");
		delay(4000);
		exit(0);
	}

	closegraph();
}

void line_dda(int x1, int y1, int x2, int y2)
{
	float x, y;
	float dx, dy, step;
	int i;

	dx = abs(x2 - x1);
	dy = abs(y2 - y1);

	if(dx >= dy)
		step = dx;
	else
		step = dy;

	dx = (x2-x1)/step;
	dy = (y2-y1)/step;

	x = x1;
	y = y1;

	i = 1;

	while( i <= step) {
		putpixel(x, y, 5);
		x = x + dx;
		y = y + dy;
		i = i + 1;
		delay(25);
	}
}

void input_points(int *x, int *y, int point_num)
{
	printf("Enter the value of x%d: ", point_num);
	scanf("%d", x);
	printf("Enter the value of y%d: ", point_num);
	scanf("%d", y);
}

int check_triangle(int x1, int y1, int x2, int y2, int x3, int y3)
{
	float d1, d2, d3;

	d1 = sqrt(abs((x2 - x1)*(x2 - x1) - (y2 - y1)*(y2 - y1)));
	d2 = sqrt(abs((x3 - x2)*(x3 - x2) - (y3 - y2)*(y3 - y2)));
	d3 = sqrt(abs((x1 - x3)*(x1 - x3) - (y1 - y3)*(y1 - y3)));

	if(((d1+d2)<=d3) || ((d1+d3)<=d2) || ((d2+d3)<=d1))
	{
		return 0;
	}
	return 1;
}