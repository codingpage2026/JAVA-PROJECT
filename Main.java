import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalTime inputTime = null;

        String filePath = "JavaAlarm.wav";

        // Ask alarm time
        while (inputTime == null) {

            try {

                System.out.print("Enter an alarm time (HH:MM:SS): ");

                inputTime =
                        LocalTime.parse(scanner.nextLine(), formatter);

            } catch (DateTimeParseException e) {

                System.out.println(
                    "Invalid format! Use HH:MM:SS"
                );
            }
        }

        // Current date and time
        LocalDateTime now = LocalDateTime.now();

        // Today's alarm
        LocalDateTime alarmDateTime =
                LocalDateTime.of(now.toLocalDate(), inputTime);

        // If time has passed, schedule for tomorrow
        if (!alarmDateTime.isAfter(now)) {

            alarmDateTime = alarmDateTime.plusDays(1);

            System.out.println(
                "Time already passed today."
            );

            System.out.println(
                "Alarm scheduled for tomorrow at " + inputTime
            );

        } else {

            System.out.println(
                "Alarm set for " + alarmDateTime
            );
        }

        // Create alarm
        AlarmClock alarmClock =new AlarmClock(alarmDateTime, filePath);

        Thread alarmThread =
                new Thread(alarmClock);

        alarmThread.start();

        // Wait until alarm starts
        while (!alarmClock.isRinging()) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }

        // Alarm is now ringing
        System.out.println(
            "\n ALARM IS RINGING "
        );

        System.out.println(
            "Press ENTER to stop the alarm."
        );

        // Main thread waits for ENTER
        scanner.nextLine();

        // Stop alarm
        alarmClock.stopAlarm();

        System.out.println(
            "Alarm stopped."
        );

        scanner.close();
    }
}