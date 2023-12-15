package frc.robot;

import com.ctre.phoenix.led.CANdle;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.CannonAimSetPercentController;
import frc.robot.commands.CannonRevolveSetPercent;
import frc.robot.commands.CannonRevolveSpinVelocity;
import frc.robot.commands.DrivetrainArcadeDrive;
import frc.robot.commands.automated.CannonFireRevolve;
import frc.robot.commands.automated.CannonReloading;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.CannonAngleAdjust;
import frc.robot.subsystems.CannonRevolve;
import frc.robot.subsystems.Drivetrain;
import frc.robot.tools.RGBController;

public class RobotContainer {
  private final CommandXboxController mXbox = new CommandXboxController(0);
  private SendableChooser<Constants.Environment> mEnvirChooser = new SendableChooser<>();
  
  private final Cannon mCannon = new Cannon();
  private final CannonAngleAdjust mCannonAngleAdjust = new CannonAngleAdjust();
  private final CannonRevolve mCannonRevolve = new CannonRevolve();
  private final Drivetrain mDrivetrain = new Drivetrain();
  private final RGBController mRGBController = new RGBController(new CANdle(Constants.CANDLE));

  public RobotContainer() {
    mEnvirChooser.setDefaultOption("Inside", Constants.Environment.Inside);
    mEnvirChooser.addOption("Outside", Constants.Environment.Outside);
    mEnvirChooser.addOption("Parade", Constants.Environment.Parade);
    SmartDashboard.putData(mEnvirChooser);

    mCannon.setDefaultCommand(new CannonReloading(mCannon, mEnvirChooser, mRGBController));
    mCannonAngleAdjust.setDefaultCommand(new CannonAimSetPercentController(mCannonAngleAdjust, mXbox));
    mCannonRevolve.setDefaultCommand(new CannonRevolveSetPercent(mCannonRevolve, 0.0));
    mDrivetrain.setDefaultCommand(new DrivetrainArcadeDrive(mDrivetrain, mXbox));

    configureButtonBindings();

    UsbCamera cam0 = CameraServer.startAutomaticCapture(0);
		cam0.setResolution(320, 240);
    cam0.setFPS(10);
  }

  private void configureButtonBindings() {
    mXbox.leftBumper().onTrue(new CannonRevolveSpinVelocity(mCannonRevolve, -1).withTimeout(3.5));
    mXbox.rightBumper().onTrue(new CannonRevolveSpinVelocity(mCannonRevolve, 1).withTimeout(3.5));
    mXbox.x().onTrue(new CannonRevolveSpinVelocity(mCannonRevolve, -8).withTimeout(15.0));
    mXbox.b().onTrue(new CannonRevolveSpinVelocity(mCannonRevolve, 8).withTimeout(15.0));
    mXbox.povLeft().whileTrue(new CannonRevolveSetPercent(mCannonRevolve, -Constants.MAX_ROTATION_SPEED)); // TODO: 1) Find velocity where all barrels rotate by changing percent output. Record said velocity
    mXbox.povRight().whileTrue(new CannonRevolveSetPercent(mCannonRevolve, Constants.MAX_ROTATION_SPEED));

    mXbox.a().onTrue(new CannonFireRevolve(mCannon, mCannonRevolve, true, mRGBController).withTimeout(8.0));
  }

  public RGBController getRGBController() {
    return mRGBController;
  }
}
