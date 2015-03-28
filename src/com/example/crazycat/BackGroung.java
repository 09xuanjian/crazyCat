package com.example.crazycat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class BackGroung extends SurfaceView implements OnTouchListener {

	// int k = 1;
	private final int WEITH = 70;
	private final int COL = 10;
	private final int ROW = 10;
	private final int BLOCKS = 15;

	private Dot mardDot[][];
	private Dot cat;

	public BackGroung(Context context) {
		super(context);
		getHolder().addCallback(callback);
		mardDot = new Dot[ROW][COL];
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				mardDot[i][j] = new Dot(j, i);
			}
		}
		setOnTouchListener(this);
		initGame();
	}

	private void redraw() {
		Canvas canvas = getHolder().lockCanvas();
		canvas.drawColor(Color.GRAY);

		Paint paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);

		for (int i = 0; i < ROW; i++) {
			int offset = 0;
			if (i % 2 == 0) {
				offset = WEITH / 2;
			}
			for (int j = 0; j < COL; j++) {

				Dot one = getDot(j, i);
				int status = one.getStatus();

				switch (status) {
				case Dot.STATUS_IN:
					paint.setColor(0xFFEEEEEE);
					break;
				case Dot.STATUS_OFF:
					paint.setColor(0xFFFFAA00);
					break;
				case Dot.STATUS_ON:
					paint.setColor(0xFFFF0000);
					break;
				default:
					break;
				}
				canvas.drawOval(
						new RectF(one.getX() * WEITH + offset, one.getY()
								* WEITH, (one.getX() + 1) * WEITH + offset,
								(one.getY() + 1) * WEITH), paint);
			}
		}

		getHolder().unlockCanvasAndPost(canvas);
	}

	Callback callback = new Callback() {

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			redraw();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

		}
	};

	private Dot getDot(int x, int y) {
		return mardDot[y][x];
	}

	private boolean isAtEdge(Dot dot) {
		if (dot.getX() * dot.getY() == 0 || dot.getX() + 1 == COL
				|| dot.getY() + 1 == ROW) {
			return true;
		}
		return false;
	}

	private int getDistance(Dot dot, int dir) {
		int step = 0;
		Dot oir = dot, next;
		while (true) {
			next = getNextDot(oir, dir);
			if (next.getStatus() == Dot.STATUS_ON) {
				return step * -1;
			}
			if (isAtEdge(next)) {
				step++;
				return step;
			}
			step++;
			oir = next;
		}
	}

	private void moveTo(Dot dot) {
		dot.setStatus(Dot.STATUS_IN);
		getDot(cat.getX(), cat.getY()).setStatus(Dot.STATUS_OFF);

		cat.setXY(dot.getX(), dot.getY());
	}

	private Dot getNextDot(Dot dot, int dir) {
		switch (dir) {
		case 1:
			return getDot(dot.getX() - 1, dot.getY());
		case 2:
			if (dot.getY() % 2 == 0) {
				return getDot(dot.getX(), dot.getY() - 1);
			} else {
				return getDot(dot.getX() - 1, dot.getY() - 1);
			}
		case 3:
			if (dot.getY() % 2 == 0) {
				return getDot(dot.getX() + 1, dot.getY() - 1);
			} else {
				return getDot(dot.getX(), dot.getY() - 1);
			}
		case 4:
			return getDot(dot.getX() + 1, dot.getY());
		case 5:
			if (dot.getY() % 2 == 0) {
				return getDot(dot.getX() + 1, dot.getY() + 1);
			} else {
				return getDot(dot.getX(), dot.getY() + 1);
			}
		case 6:
			if (dot.getY() % 2 == 0) {
				return getDot(dot.getX(), dot.getY() + 1);
			} else {
				return getDot(dot.getX() - 1, dot.getY() + 1);
			}

		default:
			break;
		}
		return null;
	}

	private void initGame() {
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				mardDot[i][j].setStatus(Dot.STATUS_OFF);
			}
		}
		cat = new Dot(4, 5);
		getDot(4, 5).setStatus(Dot.STATUS_IN);

		for (int i = 0; i < BLOCKS;) {
			int x = (int) ((Math.random() * 1000) % COL);
			int y = (int) ((Math.random() * 1000) % ROW);
			if (getDot(x, y).getStatus() == Dot.STATUS_OFF) {
				getDot(x, y).setStatus(Dot.STATUS_ON);
				i++;
				System.out.println("block" + i);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_UP) {
			int x, y;
			y = (int) (e.getY() / WEITH);
			if (y % 2 == 0) {
				x = (int) ((e.getX() - WEITH / 2) / WEITH);
			} else {
				x = (int) (e.getX() / WEITH);
			}
			if (y + 1 > ROW || x + 1 > COL) {
				initGame();
				// getNextDot(cat, k).setStatus(Dot.STATUS_IN);
				// k++;
				// redraw();

				// for (int i = 1; i < 7; i++) {
				// System.out.println(i +"---"+ getDistance(cat, i) );
				// }
			} else {
				getDot(x, y).setStatus(Dot.STATUS_ON);
			}
			redraw();
		}
		return true;
	}

}
