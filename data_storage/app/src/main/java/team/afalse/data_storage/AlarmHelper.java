package team.afalse.data_storage;

import android.media.AudioManager;
import android.media.ToneGenerator;

/**
 * Created by Kyle on 2/22/2017.
 */

public class AlarmHelper implements Runnable {

    private static final int ALARM_RING_BURST = 3;
    private static final int ALARM_RING_SETS = 10;
    private static final int ALARM_BURST_WAIT = 115;
    private static final int ALARM_SET_WAIT = 750;

    private volatile Thread alarmThread;
    private static volatile AlarmHelper instance;
    private static volatile ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

    public static synchronized AlarmHelper getInstance() {
        if (instance == null) instance = new AlarmHelper();
        return instance;
    }

    public void start() {
        if (!getThread().isAlive()) {
            getThread().run();
        } else {
            System.out.println("Trying to sound alarm while alarm is already running!");
        }
    }

    public void stop() {
        if (getThread().isAlive()) {
            getThread().interrupt();
        }
    }

    private Thread getThread() {
        if (alarmThread == null) alarmThread = new Thread(this);
        return alarmThread;
    }

    private synchronized void soundAlarm(){
        tone.startTone(ToneGenerator.TONE_PROP_BEEP, 200);
    }

    @Override
    public void run() {
        for (int i = 0; i < ALARM_RING_SETS; i++) {
            if (Thread.interrupted()) break;
            for (int j = 0; j < ALARM_RING_BURST; j++) {
                soundAlarm();
                sleepThread(ALARM_BURST_WAIT);
            }
            sleepThread(ALARM_SET_WAIT);
        }
    }

    private synchronized void sleepThread(long ms) {
        try { Thread.sleep(ms); }
        catch (Exception e) { }
    }
}
