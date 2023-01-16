package frc.robot;

import com.ctre.phoenix.CANifier;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.CannonAimSetPercentOutputWithController;
import frc.robot.commands.CannonFireRevovle;
import frc.robot.commands.CannonReloading;
import frc.robot.commands.CannonRevolveSetPercentOutput;
import frc.robot.commands.CannonRevolveSpin;
import frc.robot.commands.DrivetrainArcadeDrive;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.CannonAngleAdjust;
import frc.robot.subsystems.CannonRevolve;
import frc.robot.subsystems.Drivetrain;
import frc.robot.tools.RGBController;

public class RobotContainer {
  private CommandXboxController mXbox = new CommandXboxController(0);
  
  private final Cannon mCannon = new Cannon();
  private final CannonAngleAdjust mCannonAngleAdjust = new CannonAngleAdjust();
  private final CannonRevolve mCannonRevolve = new CannonRevolve();
  private final Drivetrain mDrivetrain = new Drivetrain();
  private final RGBController mRGBController = new RGBController(new CANifier(Constants.CANIFIER));

  public RobotContainer() {
    mCannon.setDefaultCommand(new CannonReloading(mCannon, mRGBController));
    mCannonAngleAdjust.setDefaultCommand(new CannonAimSetPercentOutputWithController(mCannonAngleAdjust, mXbox));
    mCannonRevolve.setDefaultCommand(new CannonRevolveSetPercentOutput(mCannonRevolve, 0.0));
    mDrivetrain.setDefaultCommand(new DrivetrainArcadeDrive(mDrivetrain, mXbox));

    configureButtonBindings();

    UsbCamera cam0 = CameraServer.startAutomaticCapture(0);
		cam0.setResolution(320, 240);
    cam0.setFPS(10);
  }

  private void configureButtonBindings() {
    mXbox.a().onTrue(new CannonFireRevovle(mCannon, mCannonRevolve));
    mXbox.b().onTrue(new CannonRevolveSpin(mCannonRevolve, 8, 1.0));
    mXbox.x().onTrue(new CannonRevolveSpin(mCannonRevolve, 8, -1.0));

    mXbox.leftBumper().whileTrue(new CannonRevolveSpin(mCannonRevolve, 1, -0.75));
    mXbox.rightBumper().whileTrue(new CannonRevolveSpin(mCannonRevolve, 1, 0.75));
  }

  public RGBController getRGBController() {
    return mRGBController;
  }
}
