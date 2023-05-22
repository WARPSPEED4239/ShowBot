package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.CannonAimSetPercentOutputWithController;
import frc.robot.commands.CannonReloading;
import frc.robot.commands.CannonRevolveSetPercentOutput;
import frc.robot.commands.CannonRevolveSpinLimit;
import frc.robot.commands.DrivetrainArcadeDrive;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.CannonAngleAdjust;
import frc.robot.subsystems.CannonRevolve;
import frc.robot.subsystems.Drivetrain;

public class RobotContainer {
  private CommandXboxController mXbox = new CommandXboxController(0);
  
  private final Cannon mCannon = new Cannon();
  private final CannonAngleAdjust mCannonAngleAdjust = new CannonAngleAdjust();
  private final CannonRevolve mCannonRevolve = new CannonRevolve();
  private final Drivetrain mDrivetrain = new Drivetrain();
  // private final RGBController mRGBController = new RGBController(new CANdle(Constants.CANDLE));

  public RobotContainer() {
    mCannon.setDefaultCommand(new CannonReloading(mCannon/*, mRGBController*/));
    mCannonAngleAdjust.setDefaultCommand(new CannonAimSetPercentOutputWithController(mCannonAngleAdjust, mXbox));
    mCannonRevolve.setDefaultCommand(new CannonRevolveSetPercentOutput(mCannonRevolve, 0.0));
    mDrivetrain.setDefaultCommand(new DrivetrainArcadeDrive(mDrivetrain, mXbox));

    configureButtonBindings();

    UsbCamera cam0 = CameraServer.startAutomaticCapture(0);
		cam0.setResolution(320, 240);
    cam0.setFPS(10);
  }

  private void configureButtonBindings() {
    // mXbox.x().onTrue(new CannonRevolveSpinLimit(mCannonRevolve, -8, -0.4));
    // mXbox.b().onTrue(new CannonRevolveSpinLimit(mCannonRevolve, 8, 0.4));
    mXbox.x().whileTrue(new CannonRevolveSetPercentOutput(mCannonRevolve, -0.5));
    mXbox.b().whileTrue(new CannonRevolveSetPercentOutput(mCannonRevolve, 0.5));
    // mXbox.a().onTrue(new CannonFireRevolve(mCannon, mCannonRevolve));
    
    mXbox.leftBumper().onTrue(new CannonRevolveSpinLimit(mCannonRevolve, -1, -0.4).withTimeout(3.5));
    mXbox.rightBumper().onTrue(new CannonRevolveSpinLimit(mCannonRevolve, 1, 0.4).withTimeout(3.5));
  }

  /*public RGBController getRGBController() {
    return mRGBController;
  }*/
}
