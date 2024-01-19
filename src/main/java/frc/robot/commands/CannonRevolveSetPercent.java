package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CannonRevolve;

public class CannonRevolveSetPercent extends Command {
  private final CannonRevolve mCannonRevolve;
  private final double mPercentOutput;

  public CannonRevolveSetPercent(CannonRevolve cannonRevolve, double percentOutput) {
    mCannonRevolve = cannonRevolve;
    mPercentOutput = percentOutput;
    
    addRequirements(mCannonRevolve);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    mCannonRevolve.setPercentOutput(mPercentOutput);
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
