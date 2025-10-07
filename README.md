# ShowBot

The ShowBot is our revolver-style t-shirt cannon robot. This project controls all aspects of the robot, including driving, aiming, firing, and reloading for this robot.

https://github.com/user-attachments/assets/296484f0-1624-4d5e-95ab-ad278bdd4e4d

![ShowBotImage](https://github.com/user-attachments/assets/7ea7aaa1-5dce-41f3-ad94-f2b931f2e360)

## Features

- **Automated Commands:** Includes automated firing and reloading routines that controls the robot by default.
- **Cannon:** Controls the firing mechanism and air tanks for the t-shirt cannon.
- **Cannon Angle Adjust:** Adjusts the angle of the cannon for targeting.
- **Cannon Revolve:** Rotates the revolver to select the next firing chamber.
- **Drivetrain:** Arcade drive for movement.
- **Logging:** Custom logger for debugging and diagnostics.
- **RGB LED Control:** Visual feedback using onboard LEDs.

## Branches

- **main:** Uses velocity control and limit switches.
- **develop:** Currently the same as main.
- **manualControl:** Only can control solenoids and motors manually, no RGBs.
