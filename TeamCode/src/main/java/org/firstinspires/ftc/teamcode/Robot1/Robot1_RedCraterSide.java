package org.firstinspires.ftc.teamcode.Robot1;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FieldMapping.FieldElement;
import org.firstinspires.ftc.teamcode.FieldMapping.FieldMap;
import org.firstinspires.ftc.teamcode.Hardware.Robot1_Hardware;

@Autonomous(name="Robot 1 Red Crater Side", group="Linear Opmode")
public class Robot1_RedCraterSide extends LinearOpMode {
    private Robot1_Hardware hardware;
    private Robot1_Auto auto;
    private FieldMap fieldMap = new FieldMap();

    public void runOpMode() throws InterruptedException {
        // Setup auto
        hardware = new Robot1_Hardware(hardwareMap, gamepad1, true);
        hardware.initHardware();
        auto = new Robot1_Auto(hardware);

        auto.setupMineralDetector(hardwareMap);
        waitForStart();

        auto.landOnField();
        auto.setStartPosition(3);

        // Sample minerals
        telemetry.addLine("Sampling Minerals");
        telemetry.update();
        auto.performMineralSampling(3, false, true);
        hardware.mineralDetector.disable();

        // Go to navigation target
        hardware.drivetrain.goTo(fieldMap.get(FieldElement.FRONT_OF_RED_FOOTPRINT), 0.6);
        auto.updateWithNavTarget();

        // Go to depot
        telemetry.addLine("Going to Depot");
        telemetry.update();
        hardware.drivetrain.goTo(fieldMap.get(FieldElement.RED_DEPOT), 0.6);

        // Dropping off marker
        telemetry.addLine("Releasing Marker");
        telemetry.update();
        auto.releaseMarker("red");

        telemetry.addLine("Driving to Crater");
        telemetry.update();
        auto.driveToCrater("red");

        // If completing mineral sampling for partner, sample minerals
        //hardware.drivetrain.face(fieldMap.get(FieldElement.RED_DEPOT_MIDDLE_MINERAL));
        //auto.performMineralSampling(4, true, true);
    }
}