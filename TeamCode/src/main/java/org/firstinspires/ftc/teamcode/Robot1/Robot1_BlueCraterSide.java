package org.firstinspires.ftc.teamcode.Robot1;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FieldMapping.FieldElement;
import org.firstinspires.ftc.teamcode.FieldMapping.FieldMap;
import org.firstinspires.ftc.teamcode.Hardware.Robot1_Hardware;

public class Robot1_BlueCraterSide extends LinearOpMode {
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
        auto.setStartPosition(1);

        // Sample minerals
        telemetry.addLine("Sampling Minerals");
        telemetry.update();
        auto.performMineralSampling(1, false, true);
        hardware.mineralDetector.disable();

        // Go to navigation target
        hardware.drivetrain.goTo(fieldMap.get(FieldElement.FRONT_OF_BLUE_ROVER), 0.6);
        auto.updateWithNavTarget();

        // Go to depot
        telemetry.addLine("Going to Depot");
        telemetry.update();
        hardware.drivetrain.goTo(fieldMap.get(FieldElement.BLUE_DEPOT), 0.6);

        // Dropping off marker
        telemetry.addLine("Releasing Marker");
        telemetry.update();
        auto.releaseMarker("blue");

        telemetry.addLine("Driving to Crater");
        telemetry.update();
        auto.driveToCrater("blue");

        // If completing mineral sampling for partner, sample minerals
        //hardware.drivetrain.face(fieldMap.get(FieldElement.RED_DEPOT_MIDDLE_MINERAL));
        //auto.performMineralSampling(4, true, true);
    }
}
