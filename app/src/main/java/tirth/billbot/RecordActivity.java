package tirth.billbot;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

public class RecordActivity extends AppCompatActivity {

    private static final String LOG_TAG = "RecordActivity";

    private static String audioFilePath = null;
    private static String scriptName = null;

    private static int numRecordings = 0;
    private static int currentRecording = 0;

    private static String recordStartText = null;
    private static String recordStopText = null;

    private static String playStartText = null;
    private static String playStopText = null;

    private Button recordButton = null;
    private MediaRecorder recorder = null;

    private Button playButton = null;
    private MediaPlayer player = null;

    private Chronometer chronometer = null;

    private TextView progressText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        audioFilePath = getFilesDir().getAbsolutePath() + "/";

        recordStartText = getResources().getText(R.string.record_button_start).toString();
        recordStopText = getResources().getText(R.string.record_button_stop).toString();

        playStartText = "Test";
        playStopText = "Testing";

        recordButton = (Button) findViewById(R.id.record_start_stop_button);
        playButton = (Button) findViewById(R.id.record_test_button);

        chronometer = (Chronometer)findViewById(R.id.record_chronometer);

        progressText = (TextView) findViewById(R.id.record_progress_text);

        String progress = String.format("%d / %d", currentRecording, numRecordings);
        progressText.setText(progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        currentRecording = 0;
        numRecordings = 0;
    }

    public void record(View view) {
        boolean start = recordButton.getText().toString().equals(recordStartText);

        if (start)
            startRecording(currentRecording + 1);
        else
            stopRecording();
    }

    public void play(View view) {
        boolean start = playButton.getText().toString().equals(playStartText);

        if (start)
            startPlaying(currentRecording + 1);
        else
            stopPlaying();
    }

    private void startPlaying(int recordingNumber) {
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
                    stopPlaying();
                }
            });

            playButton.setText(playStopText);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Couldn't prepare :( - " + e.getMessage());
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;

        playButton.setText(playStartText);
    }

    public void startRecording(int recordingNumber) {
        String recordingFilename = audioFilePath + scriptName + "-" + recordingNumber;

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordingFilename);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            recorder.start();

            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();

            Log.i(LOG_TAG, "Recording started");

            recordButton.setText(recordStopText);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Couldn't prepare :( - " + e.getMessage());
        }
    }

    public void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();

            chronometer.stop();

            recorder = null;
        }

        Log.i(LOG_TAG, "Recording stopped");

        recordButton.setText(recordStartText);
    }

    public void setName(View view) {
        EditText nameText = (EditText)findViewById(R.id.script_name_edittext);
        String name = nameText.getText().toString();

        Toast.makeText(getApplicationContext(), "You picked " + name + ", asshole", Toast.LENGTH_LONG).show();

        Button setButton = (Button) findViewById(R.id.set_name_button);
        setButton.setVisibility(View.GONE);
        nameText.setVisibility(View.GONE);

        TextView title = (TextView) findViewById(R.id.script_title_text);

        String titleText = "Script: " + name;

        scriptName = name;
        title.setText(titleText);
    }

    public void nextLine(View view) {
        currentRecording++;
        numRecordings++;

        chronometer.setBase(SystemClock.elapsedRealtime());

        String progress = String.format("%d / %d", currentRecording, numRecordings);
        progressText.setText(progress);
    }

    public void redoLastRecord(View view) {
    }

    public void previousRecording(View view) {
        currentRecording--;

        String progress = String.format("%d / %d", currentRecording, numRecordings);
        progressText.setText(progress);
    }

    public void nextRecording(View view) {
        currentRecording++;

        String progress = String.format("%d / %d", currentRecording, numRecordings);
        progressText.setText(progress);
    }

    public void reset(View view) {
        recreate();
    }

    public void done(View view) {
        String savedScript = scriptName;
        Intent intent = new Intent(this, PlaybackActivity.class);

        intent.putExtra("scriptName", scriptName);
        intent.putExtra("numRecordings", numRecordings);

        startActivity(intent);
    }
}
