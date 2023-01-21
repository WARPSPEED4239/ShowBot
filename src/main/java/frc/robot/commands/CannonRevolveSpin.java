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
  }

  @Override
  public void execute() {
    SmartDashboard.putNumber("Revolve Target Position", (Constants.SRX_COUNTS_PER_REV / 8) * mTargetNumberOfBarrels);
    mCannonRevolve.setPosition(mTargetNumberOfBarrels);
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
