package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Cannon;

public class CannonSetSolenoidStates extends CommandBase {
  private final Cannon mCannon;
  private final boolean mLoadingIsOpen;
  private final boolean mFiringIsOpen;

  public CannonSetSolenoidStates(Cannon cannon, boolean loadingIsOpen, boolean firingIsOpen) {
    mCannon = cannon;
    mLoadingIsOpen = loadingIsOpen;
    mFiringIsOpen = firingIsOpen;

    addRequirements(mCannon);
  }

  @Override
  public void initialize() {
    mCannon.setLoadingSolenoidState(mLoadingIsOpen);
    mCannon.setFiringSolenoidState(mFiringIsOpen);
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
