package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

public class Robot1_SimpleAuto extends LinearOpMode{
    Robot1_Hardware hardware;

    // Our Vuforia key
    private static final String VUFORIA_KEY = "AdwaKe7/////AAAAmVQWX/gUQE/gnK+olEmSWA5FCaxNrdY/EyKFLO2afR1IQD4gbnThc6LcCHIJ64hyC2i3n5VRiIRAMGxtKqjI7meHCphQAPrXpH9GomENr/fSXjVUhQao+Zw0/MLQEuTaqNYnp5EI/4oo6LTm/YPgYKOSPaP+tijaydiwNQn4A8zXPfDhkD/q6RTYMzS3UtpOR7WBZJPUBxW9XKim5ekHbYd1Hk2cFTTFAsL0XwycIWhuvHYpVlnZMqWwEnkTqp0o+5TE1FLkAfJ4OOUEfB8sP9kMEcged2/tczAh3GOcjOudp1S9F5xjPFZQX00OLV+QUCPzmT5kkqFBwiS30YR6L8urW2mJG4quq6NnrNYwzn47";

    // Since ImageTarget trackables use mm to specify their dimensions, we must use mm for all the physical dimension.
    private static final float mmPerInch        = 25.4f;
    private static final float mmFTCFieldWidth  = (12*6) * mmPerInch; // width of the FTC field (from the center point to the outer panels)
    private static final float mmTargetHeight   = (6) * mmPerInch; // height of the center of the target image above the floor

    // The camera on the RC that we are using (FRONT or BACK)
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK; // for now, for testing

    VuforiaLocalizer vuforia;

    ArrayList<VuforiaTrackable> navigationTargets = new ArrayList<VuforiaTrackable>();


    public void runOpMode() {
        hardware = new Robot1_Hardware(hardwareMap);
        hardware.initHardware();

        double[] currPos;
        setupNavigationTracker();


        while (checkForTargets() == null) {
            hardware.leftDrive.setPower(1.0);
            hardware.rightDrive.setPower(1.0);
        }
        currPos = checkForTargets();

        // go to depot
        // red alliance depot (x, y) = (60, -60)
        // blue alliance depot (x, y) = (-60, 60)

    }


    // Sets up Vuforia for tracking navigation targets
    private void setupNavigationTracker() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CAMERA_CHOICE;

        // Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the data sets for the trackable navigation targets
        VuforiaTrackables targetsRoverRuckus = vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName("Back-Space");

        // Store the navigation targets in a list
        navigationTargets.addAll(targetsRoverRuckus);

        OpenGLMatrix blueRoverLocationOnField = OpenGLMatrix
                .translation(0, mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0));
        blueRover.setLocation(blueRoverLocationOnField);

        OpenGLMatrix redFootprintLocationOnField = OpenGLMatrix
                .translation(0, -mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180));
        redFootprint.setLocation(redFootprintLocationOnField);

        OpenGLMatrix frontCratersLocationOnField = OpenGLMatrix
                .translation(-mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90));
        frontCraters.setLocation(frontCratersLocationOnField);

        OpenGLMatrix backSpaceLocationOnField = OpenGLMatrix
                .translation(mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90));
        backSpace.setLocation(backSpaceLocationOnField);

        // We need to change these depending on where the robot is (these are in mm)
        final int CAMERA_FORWARD_DISPLACEMENT  = 0;   // eg: Camera is 0 mm in front of robot center
        final int CAMERA_VERTICAL_DISPLACEMENT = 0;   // eg: Camera is 0 mm above ground
        final int CAMERA_LEFT_DISPLACEMENT     = 0;     // eg: Camera is ON the robot's center line

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                        CAMERA_CHOICE == FRONT ? 90 : -90, 0, 0));

        /**  Let all the trackable listeners know where the phone is.  */
        for (VuforiaTrackable trackable : navigationTargets) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        }

        targetsRoverRuckus.activate();
    }


    // Checks for targets, when a target is found, returns vector (x, y, z, angle (degrees))
    private double[] checkForTargets() {
        boolean targetVisible = false;
        OpenGLMatrix lastLocation = null;

        double[] positionVector = new double[4]; // x, y, z, yaw

        for (VuforiaTrackable trackable : navigationTargets) {
            if (targetIsVisible(trackable)) {
                targetVisible = true;

                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
                break; // Exit the for loop if we've found one of the nav targets
            }
        }

        if (targetVisible && lastLocation != null) {
            // express position (translation) of robot in inches.
            VectorF translation = lastLocation.getTranslation();
            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            positionVector[0] = translation.get(0) / mmPerInch;
            positionVector[1] = translation.get(1) / mmPerInch;
            positionVector[2] = translation.get(2) / mmPerInch;
            positionVector[3] = rotation.thirdAngle;
            return positionVector;
        }

        return null;

    }

    // Checks if a specific target is visible
    private boolean targetIsVisible(VuforiaTrackable trackable) {
        return ((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible();
    }

}