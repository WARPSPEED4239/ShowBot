package frc.robot.tools;

import java.util.Timer;
import java.util.TimerTask;

import com.ctre.phoenix6.controls.SolidColor;
import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.signals.RGBWColor;

public class RGBController {
	private CANdle mRGB;
	private Timer mTimer;
	private static boolean timerOn = false;
	
	public enum Color {
		Red, Black, White, Green, Blue, Purple, RedDim, WhiteDim, GreenDim, PurpleDim
	}

	class ColorTask extends TimerTask {
		private Color[] mColors;
		private RGBController mController;
		private int index;

		public ColorTask(Color[] colors, RGBController controller) {
			mColors = colors;
			mController = controller;
			index = 0;
		}

		public void run() {
			if (mColors == null) {
				return;
			}
			mController.setColorImpl(mColors[index]);
			index = (index + 1) % mColors.length;
		}
	}

	public RGBController(CANdle rgb) {
		mRGB = rgb;
	}

	public synchronized void setColors(Color[] colors, double cycleTime) {
        if (timerOn) {
            mTimer.cancel();
        }
        timerOn = true;
        mTimer = new Timer();
        mTimer.schedule(new ColorTask(colors, this), 0, (long) (cycleTime * 1000));
    }

	public synchronized void setColor(Color color) {
		if (timerOn) {
			mTimer.cancel();
		}
		timerOn = false;
		setColorImpl(color);
	}

	public void setColorImpl(Color color) {
		final int START_INDEX = 0;
		final int END_INDEX = 399;

		switch (color) {
		case Red:
			mRGB.setControl(new SolidColor(START_INDEX, END_INDEX).withColor(new RGBWColor(153, 0, 0)));
			break;
		case Black:
			mRGB.setControl(new SolidColor(START_INDEX, END_INDEX).withColor(new RGBWColor(0, 0, 0)));
			break;
		case White:
			mRGB.setControl(new SolidColor(START_INDEX, END_INDEX).withColor(new RGBWColor(128, 128, 128)));
			break;
		case Green:
			mRGB.setControl(new SolidColor(START_INDEX, END_INDEX).withColor(new RGBWColor(0, 128, 0)));
			break;
		case Blue:
			mRGB.setControl(new SolidColor(START_INDEX, END_INDEX).withColor(new RGBWColor(0, 0, 128)));
			break;
		case Purple:
			mRGB.setControl(new SolidColor(START_INDEX, END_INDEX).withColor(new RGBWColor(255, 0, 255)));
			break;
		case RedDim:
			mRGB.setControl(new SolidColor(START_INDEX, END_INDEX).withColor(new RGBWColor(25, 0, 0)));
			break;
		case WhiteDim:
			mRGB.setControl(new SolidColor(START_INDEX, END_INDEX).withColor(new RGBWColor(25, 25, 25)));
			break;
		case GreenDim:
			mRGB.setControl(new SolidColor(START_INDEX, END_INDEX).withColor(new RGBWColor(0, 25, 0)));
			break;
		case PurpleDim:
			mRGB.setControl(new SolidColor(START_INDEX, END_INDEX).withColor(new RGBWColor(25, 0, 25)));
			break;
		}
	}
}
