package tirth.billbot;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    private static String audioFileName = null;

    private static String recordStartText = null;
    private static String recordStopText = null;

    private static String playStartText = null;
    private static String playStopText = null;

    private Button recordButton = null;
    private MediaRecorder recorder = null;

    private Button playButton = null;
    private MediaPlayer player = null;

    private Chronometer chronometer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioFileName = getFilesDir().getAbsolutePath() + "/recorded.3gp";

        Log.i(LOG_TAG, "Files saved to " + audioFileName);

        recordStartText = getResources().getText(R.string.record_button_start).toString();
        recordStopText = getResources().getText(R.string.record_button_stop).toString();

        playStartText = getResources().getText(R.string.play_button_start).toString();
        playStopText = getResources().getText(R.string.play_button_stop).toString();

        recordButton = (Button) findViewById(R.id.record_button);
        playButton = (Button) findViewById(R.id.play_button);

        chronometer = (Chronometer)findViewById(R.id.chronometer);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(LOG_TAG, "ACTIVITY PAUSED");

//        if (recorder != null) {
//            recorder.release();
//            recorder = null;
//        }
//
//        if (player != null) {
//            player.release();
//            player = null;
//        }
    }

//    public void record(View view) {
//        boolean start = recordButton.getText().toString().equals(recordStartText);
//
//        if (start)
//            startRecording();
//        else
//            stopRecording();
//    }
//
//    public void play(View view) {
//        boolean start = playButton.getText().toString().equals(playStartText);
//
//        if (start)
//            startPlaying();
//        else
//            stopPlaying();
//    }
//
//    private void startPlaying() {
//        player = new MediaPlayer();
//
//        try {
//            player.setDataSource(audioFileName);
//            player.prepare();
//            player.start();
//
//            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer player) {
//                    stopPlaying();
//                }
//            });
//
//            playButton.setText(playStopText);
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Couldn't prepare :( - " + e.getMessage());
//        }
//    }
//
//    private void stopPlaying() {
//        player.release();
//        player = null;
//
//        playButton.setText(playStartText);
//    }
//
//    public void startRecording() {
//        recorder = new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        recorder.setOutputFile(audioFileName);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//
//        try {
//            recorder.prepare();
//            recorder.start();
//
//            chronometer.setBase(SystemClock.elapsedRealtime());
//            chronometer.start();
//
//            Log.i(LOG_TAG, "Recording started");
//
//            recordButton.setText(recordStopText);
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Couldn't prepare :( - " + e.getMessage());
//        }
//    }
//
//    public void stopRecording() {
//        if (recorder != null) {
//            recorder.stop();
//            recorder.release();
//
//            chronometer.stop();
//
//            recorder = null;
//            }
//
//        Log.i(LOG_TAG, "Recording stopped");
//
//        recordButton.setText(recordStartText);
//    }
}
