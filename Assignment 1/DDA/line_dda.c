//Implementation of DDA Line Drawing Algorithm


#include <graphics.h>
#include <stdio.h>
#include<stdlib.h>
#include <math.h>
#include <dos.h>

void line_dda(int, int, int, int);
void input_points(int *, int *, int);

void main() {
	int x1, y1, x2, y2, x3, y3;
	int gd=DETECT,gm;

	initgraph(&gd, &gm, "C:\\TC\\BGI");

	input_points(&x1, &y1, 1);
	input_points(&x2, &y2, 2);

	line_dda(x1, y1, x2, y2);
	closegraph();
}

void line_dda(int x1, int y1, int x2, int y2)
{
	int i;
	float x, y;
	float dx, dy, step;

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