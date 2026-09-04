import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.sound.sampled.*;

public class AlarmClock implements Runnable {

    private final LocalDateTime alarmDateTime;
    private final String filePath;

    private volatile boolean ringing = false;
    private volatile boolean stopped = false;

    private Clip clip;
    private AudioInputStream audioStream;

    public AlarmClock(
            LocalDateTime alarmDateTime,
            String filePath) {

        this.alarmDateTime = alarmDateTime;
        this.filePath = filePath;
    }

    @Override
    public void run() {

        // Wait until alarm time
        while (LocalDateTime.now().isBefore(alarmDateTime)) {

            try {

                Thread.sleep(500);

                LocalDateTime now =
                        LocalDateTime.now();

                System.out.printf(
                    "\rCurrent Time: %02d:%02d:%02d",
                    now.getHour(),
                    now.getMinute(),
                    now.getSecond()
                );

            } catch (InterruptedException e) {

                return;
            }
        }

        // Alarm time reached
        startAlarm();
    }

    private void startAlarm() {

        File audioFile =
                new File(filePath);

        System.out.println(
            "\nAudio file: "
            + audioFile.getAbsolutePath()
        );

        if (!audioFile.exists()) {

            System.out.println(
                " Audio file not found!"
            );

            return;
        }

        try {

            audioStream =
                AudioSystem.getAudioInputStream(audioFile);

            clip =
                AudioSystem.getClip();

            clip.open(audioStream);

            // 🔊 CONTINUOUS LOOP
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            clip.start();

            ringing = true;

            // Keep alarm alive until ENTER
            while (!stopped) {

                Thread.sleep(100);
            }

            // Stop sound
            clip.stop();
            clip.close();
            audioStream.close();

        } catch (UnsupportedAudioFileException e) {

            System.out.println(
                " Audio format is not supported."
            );

        } catch (LineUnavailableException e) {

            System.out.println(
                " Audio is unavailable."
            );

        } catch (IOException e) {

            System.out.println(
                " Error reading audio file: "
                + e.getMessage()
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }

    public boolean isRinging() {

        return ringing;
    }

    public void stopAlarm() {

        stopped = true;

        if (clip != null) {
            clip.stop();
        }
    }
}