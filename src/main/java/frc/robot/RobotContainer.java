package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.CannonAimSetPercentController;
import frc.robot.commands.CannonRevolveSetPercent;
import frc.robot.commands.CannonSetSolenoidStates;
import frc.robot.commands.DrivetrainArcadeDrive;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.CannonAngleAdjust;
import frc.robot.subsystems.CannonRevolve;
import frc.robot.subsystems.Drivetrain;

public class RobotContainer {
  private final CommandXboxController mXbox = new CommandXboxController(0);
  
  private final Cannon mCannon = new Cannon();
  private final CannonAngleAdjust mCannonAngleAdjust = new CannonAngleAdjust();
  private final CannonRevolve mCannonRevolve = new CannonRevolve();
  private final Drivetrain mDrivetrain = new Drivetrain();

  public RobotContainer() {
    mCannon.setDefaultCommand(new CannonSetSolenoidStates(mCannon, false, false));
    mCannonAngleAdjust.setDefaultCommand(new CannonAimSetPercentController(mCannonAngleAdjust, mXbox));
    mCannonRevolve.setDefaultCommand(new CannonRevolveSetPercent(mCannonRevolve, 0.0));
    mDrivetrain.setDefaultCommand(new DrivetrainArcadeDrive(mDrivetrain, mXbox));

    configureButtonBindings();

    UsbCamera cam0 = CameraServer.startAutomaticCapture(0);
		cam0.setResolution(320, 240);
    cam0.setFPS(10);
  }

  private void configureButtonBindings() {
    mXbox.povLeft().whileTrue(new CannonRevolveSetPercent(mCannonRevolve, -Constants.MAX_ROTATION_SPEED));
    mXbox.povRight().whileTrue(new CannonRevolveSetPercent(mCannonRevolve, Constants.MAX_ROTATION_SPEED));

    mXbox.b().whileTrue(new CannonSetSolenoidStates(mCannon, true, false));
    mXbox.a().whileTrue(new CannonSetSolenoidStates(mCannon, false, true));
  }
}
