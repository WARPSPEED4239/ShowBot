package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drivetrain extends SubsystemBase {
  private final int CURRENT_LIMIT = 45;
	private final double RAMP_RATE = 0.05;
	
	private CANSparkMax leftMotor1 = new CANSparkMax(Constants.DRIVETRAIN_MOTOR_LEFT_1, MotorType.kBrushless);
	private CANSparkMax leftMotor2 = new CANSparkMax(Constants.DRIVETRAIN_MOTOR_LEFT_2, MotorType.kBrushless);
	private CANSparkMax rightMotor1 = new CANSparkMax(Constants.DRIVETRAIN_MOTOR_RIGHT_1, MotorType.kBrushless);
	private CANSparkMax rightMotor2 = new CANSparkMax(Constants.DRIVETRAIN_MOTOR_RIGHT_2, MotorType.kBrushless);
  
  private DifferentialDrive drive = new DifferentialDrive(leftMotor1, rightMotor1);
	
	public Drivetrain() {
    leftMotor1.restoreFactoryDefaults();
    leftMotor2.restoreFactoryDefaults();
    rightMotor1.restoreFactoryDefaults();
    rightMotor2.restoreFactoryDefaults();

    rightMotor1.setInverted(true);
    rightMotor2.setInverted(true);

		leftMotor2.follow(leftMotor1);
		rightMotor2.follow(rightMotor1);
    
    leftMotor1.setIdleMode(IdleMode.kBrake);
    leftMotor2.setIdleMode(IdleMode.kBrake);
    rightMotor1.setIdleMode(IdleMode.kBrake);
    rightMotor2.setIdleMode(IdleMode.kBrake);

    leftMotor1.setSmartCurrentLimit(CURRENT_LIMIT);
    leftMotor2.setSmartCurrentLimit(CURRENT_LIMIT);
    rightMotor1.setSmartCurrentLimit(CURRENT_LIMIT);
    rightMotor2.setSmartCurrentLimit(CURRENT_LIMIT);

    leftMotor1.setOpenLoopRampRate(RAMP_RATE);
    leftMotor2.setOpenLoopRampRate(RAMP_RATE);
    rightMotor1.setOpenLoopRampRate(RAMP_RATE);
    rightMotor2.setOpenLoopRampRate(RAMP_RATE);

    leftMotor1.burnFlash();
    leftMotor2.burnFlash();
    rightMotor1.burnFlash();
    rightMotor2.burnFlash();
	}
	
	public void arcadeDrive(double move, double rotate) {
		final double MIN_MOVE_THRESHOLD = 0.05;
		final double MIN_ROTATE_THRESHOLD = 0.20;
		if (Math.abs(move) < MIN_MOVE_THRESHOLD) {
			move = 0.0;
    }
		if (Math.abs(rotate) < MIN_ROTATE_THRESHOLD) {
			rotate = 0.0;
    }
		
    drive.arcadeDrive(move, rotate);
	}
}
