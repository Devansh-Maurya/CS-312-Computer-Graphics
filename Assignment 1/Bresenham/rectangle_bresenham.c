//Implementation of Bresenham's Line Drawing Algorithm
//to draw a rectangle 

#include <graphics.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <dos.h>

void draw_line(int, int, int, int);
void draw_high_slope(int, int, int, int);
void draw_low_slope(int, int, int, int);
void input_points(int *, int *, int);
int check_rectangle(int, int, int, int, int, int, int, int);


void main() {
	int x1, y1, x2, y2, x3, y3, x4, y4;
	int gd=DETECT,gm;

	initgraph(&gd, &gm, "C:\\TC\\BGI");

	input_points(&x1, &y1, 1);
	input_points(&x2, &y2, 2);
	input_points(&x3, &y3, 3);
	input_points(&x4, &y4, 4);

	if(check_rectangle(x1, y1, x2, y2, x3, y3, x4, y4))
	{
		draw_line(x1, y1, x2, y2);
		draw_line(x2, y2, x3, y3);
		draw_line(x3, y3, x4, y4);
		draw_line(x4, y4, x1, y1);
	}
	else
	{
		printf("A rectangle can't be drawn from the given points\n");
		delay(4000);
		exit(0);
	}

	delay(4000);

	closegraph();
}


void draw_line(int x1, int y1, int x2, int y2)
{
	if (abs(y2 - y1) < abs(x2 - x1))
	{
		if (x1 > x2)
		{
			draw_low_slope(x2, y2, x1, y1);
		}
		else
		{
			draw_low_slope(x1, y1, x2, y2);
		}
	}
	else
	{
		if (y1 > y2)
		{
			draw_high_slope(x2, y2, x1, y1);
		}
		else
		{
			draw_high_slope(x1, y1, x2, y2);
		}
	}
}


void draw_low_slope(int x1, int y1, int x2, int y2)
{
	int dx, dy, x, y, change_y, p;

	dx = x2 - x1;
	dy = y2 - y1;
	change_y = 1;

	if ( dy < 0)
	{
		change_y = -1;
		dy = -dy;
	}

	p = 2*dy - dx;
	y = y1;

	for (x = x1; x <= x2; x++)
	{
		putpixel(x, y, 7);
		if ( p > 0)
		{
			y = y + change_y;
			p = p + 2*dy - 2*dx;
		}
		else{
			p = p + 2*dy;
		}
		delay(25);
	}
}


void draw_high_slope(int x1, int y1, int x2, int y2)
{
	int x, y, dx, dy, p, change_x;

	dx = x2 - x1;
	dy = y2 - y1;
	change_x = 1;

	if ( dx < 0)
	{
		change_x = -1;
		dx = -dx;
	}

	p = 2*dx - dy;
	x = x1;

	for( y = y1; y <= y2; y++)
	{
		putpixel(x, y, 7);
		if( p > 0)
		{
			x = x + change_x;
			p = p + 2*dx - 2*dy;
		}
		else {
			p = p + 2*dx;
		}
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


int check_rectangle(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)
{
	int d1, d2, d3, d4, sq_diag1, sq_diag2;

	d1 = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
	d2 = (x3-x2)*(x3-x2) + (y3-y2)*(y3-y2);
	d3 = (x4-x3)*(x4-x3) + (y4-y3)*(y4-y3);
	d4 = (x1-x4)*(x1-x4) + (y1-y4)*(y1-y4);

	sq_diag1 = (x3-x1)*(x3-x1) + (y3-y1)*(y3-y1);
	sq_diag2 = (x2-x4)*(x2-x4) + (y2-y4)*(y2-y4);

	if((d1==d3 && d2==d4) && (sq_diag1==sq_diag2))
	{
		return 1;
	}
	return 0;
}