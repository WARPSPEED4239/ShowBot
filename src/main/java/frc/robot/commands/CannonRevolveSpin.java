package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.CannonRevolve;

public class CannonRevolveSpin extends CommandBase {
  private final CannonRevolve mCannonRevolve;
  private final int mTargetNumberOfBarrels;

  public CannonRevolveSpin(CannonRevolve cannonRevolve, int targetNumberOfBarrels) {
    mCannonRevolve = cannonRevolve;
    mTargetNumberOfBarrels = targetNumberOfBarrels;
    addRequirements(mCannonRevolve);
  }

  @Override
  public void initialize() {
    int ticksPerBarrel = Constants.SRX_COUNTS_PER_REV / 8;
    double currentBarrelPosition = ticksPerBarrel * Math.round(mCannonRevolve.getPosition(false) / ticksPerBarrel);
    int targetToPrint = (int) currentBarrelPosition + (ticksPerBarrel * mTargetNumberOfBarrels);
    SmartDashboard.putNumber("Revolve Target Position", targetToPrint);

    mCannonRevolve.setPosition(mTargetNumberOfBarrels);
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
    mCannonRevolve.setPercentOutput(0.0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
