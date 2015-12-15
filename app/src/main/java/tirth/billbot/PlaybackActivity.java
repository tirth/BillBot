package tirth.billbot;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class PlaybackActivity extends AppCompatActivity {

    private static String LOG_TAG = "PlaybackActivity";

    private static String audioFilePath = null;

    private static String scriptName = null;
    private static int numRecordings = 0;
    private static int currentRecording = 0;

    private static String playStartText = null;
    private static String playStopText = null;

    TextView currentRecordingText = null;

    private Button playNextButton = null;
    private Button playPrevButton = null;
    private MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        Intent intent = getIntent();

        scriptName = intent.getStringExtra("scriptName");
        numRecordings = intent.getIntExtra("numRecordings", 0);
        currentRecording = 0;

        TextView numRecordingsText = (TextView) findViewById(R.id.playback_numRecordings);
        String numText = "Lines: " + numRecordings;
        numRecordingsText.setText(numText);

        currentRecordingText = (TextView) findViewById(R.id.playback_current);
        String currentText = "Current: " + currentRecording;
        currentRecordingText.setText(currentText);

        playStartText = "Playback start";
        playStopText = "Playback stop";

        audioFilePath = getFilesDir().getAbsolutePath() + "/";
        playNextButton = (Button)findViewById(R.id.playback_button_next);
        playPrevButton = (Button)findViewById(R.id.playback_button_last);
    }

    public void playbackNext(View view) {
        currentRecording++;

        String currentText = "Current: " + currentRecording;
        currentRecordingText.setText(currentText);

        startPlaying(currentRecording, playNextButton);
    }

    public void playbackPrevious(View view) {
        currentRecording--;

        String currentText = "Current: " + currentRecording;
        currentRecordingText.setText(currentText);

        startPlaying(currentRecording, playPrevButton);
    }

    private void startPlaying(int recordingNumber, final Button button) {
        player = new MediaPlayer();

        String recordingFilename = audioFilePath + scriptName + "-" + recordingNumber;

        try {
            player.setDataSource(recordingFilename);
            player.prepare();
            player.start();

            Log.i(LOG_TAG, "Playing");

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer player) {
                    stopPlaying(button);
                }
            });

            button.setText(playStopText);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Couldn't prepare :( - " + e.getMessage());
        }
    }

    private void stopPlaying(Button button) {
        player.release();
        player = null;

        button.setText(playStartText);
    }
}
