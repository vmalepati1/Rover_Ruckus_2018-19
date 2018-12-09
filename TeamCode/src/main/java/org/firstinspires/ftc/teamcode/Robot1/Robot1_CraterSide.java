package org.firstinspires.ftc.teamcode.Robot1;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware.Robot1_Hardware;
import org.firstinspires.ftc.teamcode.Vision.Detectors.GoldAlignDetector;

// Will delete eventually

@Autonomous(name="Robot 1 Crater Side", group="Linear Opmode")
public class Robot1_CraterSide extends LinearOpMode {
    private Robot1_Hardware hardware;
    private Robot1_Auto auto;

    public void runOpMode() throws InterruptedException {
        // Setup auto
        hardware = new Robot1_Hardware(hardwareMap, gamepad1, true);
        hardware.initHardware();
        auto = new Robot1_Auto(hardware);
        auto.setupMineralDetector(hardwareMap);
        waitForStart();

        telemetry.addLine("Turn to gold");
        telemetry.update();
        auto.turnToGold();

        telemetry.addLine("Driving forward to hit mineral");
        telemetry.update();
        hardware.drivetrain.driveDistance(1, 30, 0.6);

        telemetry.addLine("Parking in crater");
        telemetry.update();
        hardware.drivetrain.driveDistance(1, 10, 0.8);

        hardware.mineralDetector.disable();
    }
}
