package frc.robot;

//import com.ctre.phoenix.CANifier;

import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.CannonAimSetPercentOutputWithController;
import frc.robot.commands.CannonFiringSolenoidSetState;
import frc.robot.commands.CannonLoadingSolenoidSetState;
import frc.robot.commands.CannonRevolveSetPercentOutput;
import frc.robot.commands.DrivetrainArcadeDrive;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.CannonAngleAdjust;
import frc.robot.subsystems.CannonRevolve;
import frc.robot.subsystems.Drivetrain;

public class RobotContainer {
  private CommandXboxController mXbox = new CommandXboxController(0);
  
  private final Cannon mCannon = new Cannon();
  private final CannonRevolve mCannonRevolve = new CannonRevolve();
  private final CannonAngleAdjust mCannonAngleAdjust = new CannonAngleAdjust();
  private final Drivetrain mDrivetrain = new Drivetrain();

  public RobotContainer() {
    mCannon.setDefaultCommand(new CannonFiringSolenoidSetState(mCannon, false));
    mCannonRevolve.setDefaultCommand(new CannonRevolveSetPercentOutput(mCannonRevolve, 0.0));
    mCannonAngleAdjust.setDefaultCommand(new CannonAimSetPercentOutputWithController(mCannonAngleAdjust, mXbox));
    mDrivetrain.setDefaultCommand(new DrivetrainArcadeDrive(mDrivetrain, mXbox));

    configureButtonBindings();

    UsbCamera cam0 = CameraServer.startAutomaticCapture(0);
		cam0.setResolution(320, 240);
    cam0.setFPS(10);
  }

  private void configureButtonBindings() {
    mXbox.a().whileTrue(new CannonFiringSolenoidSetState(mCannon, true));
    mXbox.b().onTrue(new CannonLoadingSolenoidSetState(mCannon, true));
    mXbox.y().onTrue(new CannonLoadingSolenoidSetState(mCannon, false));

    mXbox.leftBumper().whileTrue(new CannonRevolveSetPercentOutput(mCannonRevolve, -0.75));
    mXbox.rightBumper().whileTrue(new CannonRevolveSetPercentOutput(mCannonRevolve, 0.75));

    // xButtonA.whenPressed(new ConditionalCommand(new ConditionalCommand(cannonFire(), new CannonRevolveSpin(mCannonRevolve, 1, -0.4), () -> mCannonRevolve.getRevolveLimitSwitch()), new InstantCommand(), () -> mCannon.getFiringTankPressure() >= MIN_FIRING_PSI));
    // xButtonB.whenPressed(new CannonRevolveSpin(mCannonRevolve, 8, 1.0));
    // xButtonX.whenPressed(new CannonRevolveSpin(mCannonRevolve, 8, -1.0));

    // xButtonLeftBumper.whenPressed(new CannonRevolveSpin(mCannonRevolve, 1, -0.4));
    // xButtonRightBumper.whenPressed(new CannonRevolveSpin(mCannonRevolve, 1, 0.4));
  }

  /* public Command cannonReloading() { // ERROR: If statement is only checked once on init
    if (mCannon.getFiringTankPressure() <= MIN_FIRING_PSI) {
      Command mCommand = new SequentialCommandGroup(
        new ParallelRaceGroup(
          new RGBSetColor(mRGBController, Color.Black),
          new CannonFiringSolenoidSetState(mCannon, false).withTimeout(0.5)
        ),
        new CannonLoadingSolenoidSetState(mCannon, true).withTimeout(0.5)
      );

      return mCommand;
    } else if (mCannon.getFiringTankPressure() >= MAX_FIRING_PSI) {
      return new CannonLoadingSolenoidSetState(mCannon, false).withTimeout(0.5);
    } else {
      return new RGBSetColor(mRGBController, Color.Red).withTimeout(0.1);
    }
  }

  public Command cannonFire() {
    Color[] redFlashing = {Color.Red, Color.Black};

    Command mCommand = new SequentialCommandGroup(
      new ParallelRaceGroup(
        new RGBSetColor(mRGBController, redFlashing, 0.2),
        new CannonLoadingSolenoidSetState(mCannon, false).withTimeout(1.0)
      ),
      new ParallelRaceGroup(
        new RGBSetColor(mRGBController, Color.White),
        new CannonFiringSolenoidSetState(mCannon, true).withTimeout(0.5)
      ),
      new RGBSetColor(mRGBController, Color.Black).withTimeout(0.5),
      new CannonRevolveSpin(mCannonRevolve, 1, 0.4)
    );

    return mCommand;
  } */

  /*public RGBController getRGBController() {
    return mRGBController;
  }*/
}
