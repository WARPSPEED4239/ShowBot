ShowBot Code Notes:  
1) There is RGB code already in the code but not tested. It is in the following files:  
Robot.Java  
CannonReloading.Java  
CannonFireRevolve.Java  
All that should be needed to be done here is uncomment and test once you get the CANdle wired and configured. Should be CAN ID 7.  
  
2) The rotation code is not the best at the moment. Dustin was manually rotating the barrels for the most part at the parade. There is an encoder on the barrel rotation shaft, but due to backlash and the motor having to be mounted loosely, I have not had success with position control. We have been using the limit switch readings for rotation, which works well, but the power it takes to rotate the barrel is not consistent.  
Where I think we go from here is doing velocity control with the current limit switch rotation control. This will have the benefits of using our current rotation logic plus a constant rotation speed regardless of friction (in theory). I can help you with this next time you guys work on the ShowBot if you would like.  
  
3) There are currently 4 branches in the repo:  
main: uses code we used for parade and limit switch rotation  
develop: I started to implement encoder rotation here. This is where you will develop velocity control  
automatedLimit: The same as main currently  
manualControl: Only can control solenoids and motors manually, no RGBs.  
  