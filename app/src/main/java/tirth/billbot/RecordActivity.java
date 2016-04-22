package tirth.billbot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DialerFilter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Objects;

public class RecordActivity extends AppCompatActivity {

    private static final String LOG_TAG = "RecordActivity";

    private static String audioFilePath = null;
    private static String scriptName = null;

    private static int numRecordings = 1;
    private static int currentRecording = 1;

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

        Log.i(LOG_TAG, "Audio file path: " + audioFilePath);

        recordStartText = getResources().getText(R.string.record_button_start).toString();
        recordStopText = getResources().getText(R.string.record_button_stop).toString();

        playStartText = "Test";
        playStopText = "Testing";

        recordButton = (Button) findViewById(R.id.record_start_stop_button);
        playButton = (Button) findViewById(R.id.record_test_button);

        chronometer = (Chronometer) findViewById(R.id.record_chronometer);

        progressText = (TextView) findViewById(R.id.record_progress_text);

        String progress = String.format("%d / %d", currentRecording, numRecordings);
        progressText.setText(progress);

        // set title
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Script Name");

        final EditText input = new EditText(this);
        input.setHint("Enter script name");

        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = input.getText().toString();
                setName(value);
            }
        });

        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        currentRecording = 1;
        numRecordings = 1;
    }

    public void record(View view) {
        boolean start = recordButton.getText().toString().equals(recordStartText);

//        // set name
//        setName(view);

        if (start)
            startRecording(currentRecording);
        else
            stopRecording();
    }

    public void play(View view) {
        boolean start = playButton.getText().toString().equals(playStartText);

        if (start)
            startPlaying(currentRecording);
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
        String recordingFilename = audioFilePath + scriptName + "-" + recordingNumber + ".3gp";

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

    public void setName(String name) {
        if (name.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Script Name Can't Be Empty");

            final EditText input = new EditText(this);
            input.setHint("Enter script name");

            builder.setView(input);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String value = input.getText().toString();
                    setName(value);
                }
            });

            builder.create().show();
        }

        TextView titleText = (TextView) findViewById(R.id.record_title);
        titleText.setText(name);

        Toast.makeText(getApplicationContext(), "Script name: " + name, Toast.LENGTH_LONG).show();
        scriptName = name;
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
        if (currentRecording < 1) return;

        currentRecording--;

        String progress = String.format("%d / %d", currentRecording, numRecordings);
        progressText.setText(progress);
    }

    public void nextRecording(View view) {
        if (currentRecording == numRecordings) return;

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
