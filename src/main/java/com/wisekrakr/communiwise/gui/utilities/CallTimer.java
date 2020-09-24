package com.wisekrakr.communiwise.gui.utilities;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CallTimer extends Thread {
    private final DateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");
    private boolean isRunning = false;
    private boolean isPause = false;
    private boolean isReset = false;
    private long startTime;
    private long pauseTime;

    private final JLabel time;

    public CallTimer(JLabel time) {
        this.time = time;
    }

    public void run() {
        isRunning = true;

        startTime = System.currentTimeMillis();

        while (isRunning) {
            try {
                if (!isPause) {
                    time.setText(toTimeString());

                } else {
                    pauseTime += 100;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (isReset) {
                    time.setText("00:00:00");
                    isRunning = false;
                    break;
                }
            }
        }
    }


    /**
     * Reset counting to "00:00:00"
     */
    public void reset() {
        isReset = true;
        isRunning = false;
    }

    public void pauseTimer() {
        isPause = true;
    }

    public void resumeTimer() {
        isPause = false;
    }

    /**
     * Generate a String for time counter in the format of "HH:mm:ss"
     *
     * @return the time counter
     */
    private String toTimeString() {
        long now = System.currentTimeMillis();
        Date current = new Date(now - startTime - pauseTime);
        dateFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
        return " Call duration:   " + dateFormater.format(current);
    }
}
