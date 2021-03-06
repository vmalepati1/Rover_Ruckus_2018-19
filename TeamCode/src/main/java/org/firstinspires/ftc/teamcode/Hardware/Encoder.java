/**
 * Encoder class; properties are motor, type of encoder (Neverest/Tetrix), and wheel diameter
 *
 * Should be instantiated in opmodes that use encoders for more effective implementation.
 */

package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.AutonomousData;

public class Encoder {
    private DcMotor motor; // motor
    private int type; // Either "Neverest" or "Tetrix"
    private double wheelDiameter; // wheel diameter in inches

    // Constants for ticks per revolution of motors we use
    final static int NEVEREST_TICKS_PER_REV = 1120;
    final static int TETRIX_TICKS_PER_REV = 1440;

    public Encoder(DcMotor motor, int type, double diam) {
        this.motor = motor;
        this.type = type;
        wheelDiameter = diam;
    }

    public int ticksPerRev() {
        if (type == AutonomousData.NEVEREST_ENCODER)
            return NEVEREST_TICKS_PER_REV;
        else if (type == AutonomousData.TETRIX_ENCODER)
            return TETRIX_TICKS_PER_REV;
        else
            return NEVEREST_TICKS_PER_REV; // default
    }

    public int getEncoderCount() {
        return motor.getCurrentPosition();
    }

    public double motorRotations() {
        return (double) getEncoderCount() / ticksPerRev();
    }

    public double linDistance() {
        return motorRotations() * getWheelCircumference();
    }


    // Shortcut methods for resetting, run without, run to position, and setting target position
    public void reset() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void runWith() {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void setup() {
        reset();
        runWith();
    }
    public void runWithout() {
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void runToPosition() {
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void setTarget(double linDistance) {
        int encoderPos = (int) (linDistance * ticksPerRev() / getWheelCircumference());
        motor.setTargetPosition(encoderPos);
    }
    public void setEncoderTarget(int encoderTicks) {
        motor.setTargetPosition(encoderTicks);
    }


    // Accessor methods
    public DcMotor getMotor() {
        return motor;
    }
    public int getType() {
        return type;
    }
    public double getWheelDiameter() {
        return wheelDiameter;
    }
    public double getWheelCircumference() {
        return Math.PI * wheelDiameter;
    }

}
