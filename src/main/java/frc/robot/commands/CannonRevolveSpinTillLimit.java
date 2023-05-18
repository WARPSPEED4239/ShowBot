package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.CannonRevolve;

public class CannonRevolveSpinTillLimit extends CommandBase {
  private final CannonRevolve mCannonRevolve;
  private boolean mStartingLimit = false;
  private boolean mOffLimit = false;
  private boolean mEnd = false;
  private double mSpeed = 0.4;

  public CannonRevolveSpinTillLimit(CannonRevolve cannonRevolve, double speed) {
    mCannonRevolve = cannonRevolve;
    mSpeed = speed;
    
    addRequirements(mCannonRevolve);
  }

  @Override
  public void initialize() {
    mStartingLimit = false;
    mOffLimit = false;
    mEnd = false;

    if (mCannonRevolve.getRevolveLimitSwitch()) {
      mStartingLimit = true;
    }
  }

  @Override
  public void execute() {
    if (mStartingLimit) {
      if (!mCannonRevolve.getRevolveLimitSwitch()) {
        mOffLimit = true;
      }

      if (mOffLimit && mCannonRevolve.getRevolveLimitSwitch()) {
        mEnd = true;
      }
    } else {
      if (mCannonRevolve.getRevolveLimitSwitch()) {
        mEnd = true;
      }
    }

    mCannonRevolve.setPercentOutput(mSpeed);
  }

  @Override
  public void end(boolean interrupted) {
    mCannonRevolve.setPercentOutput(0.0);
  }

  @Override
  public boolean isFinished() {
    return mEnd;
  }
}
