package tirth.billbot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.BaseKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class PlaybackActivity extends AppCompatActivity {

    private static String LOG_TAG = "PlaybackActivity";

    private static String audioFilePath = null;

    private static String scriptName = null;
    private static int numRecordings = 1;
    private static int currentRecording = 1;

    TextView recordingProgressText = null;

    private MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        player = new MediaPlayer();

        Intent intent = getIntent();

        scriptName = intent.getStringExtra("scriptName");
        numRecordings = intent.getIntExtra("numRecordings", 1);
        currentRecording = 1;

        // recording title
        TextView title = (TextView) findViewById(R.id.playback_title);
        title.setText(scriptName);

        // recording progress
        recordingProgressText = (TextView) findViewById(R.id.playback_progress);
        String progressText = currentRecording + "/" + numRecordings;
        recordingProgressText.setText(progressText);

        audioFilePath = getFilesDir().getAbsolutePath() + "/";
    }

    public void playbackNext(View view) {
        if (currentRecording > numRecordings) return;

        String progressText = currentRecording + "/" + numRecordings;
        recordingProgressText.setText(progressText);

        startPlaying(currentRecording);

        currentRecording++;
    }

    public void playbackPrevious(View view) {
        if (currentRecording < 1) return;

        currentRecording--;

        String progressText = currentRecording + "/" + numRecordings;
        recordingProgressText.setText(progressText);

        startPlaying(currentRecording);
    }

    private void startPlaying(int recordingNumber) {
        if (player.isPlaying())
            stopPlaying();

        String recordingFilename = audioFilePath + scriptName + "-" + recordingNumber + ".3gp";

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

//            button.setText(playStopText);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Couldn't prepare :( - " + e.getMessage());
        }
    }

    private void stopPlaying() {
        player.stop();
        player.reset();
    }

    public void pause(View view) {
        if (player.isPlaying()) {
            player.pause();
        }
        else {
            try {
                player.start();
            }
            catch (Exception e) {
                Log.e(LOG_TAG, "Couldn't resume");
            }
        }
    }

    public void delete(View view) {
        AlertDialog areYouSure = new AlertDialog.Builder(this)
                .setTitle("Delete " + scriptName)
                .setMessage("You sure bro?")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String path = getFilesDir().getAbsolutePath();
                        File allFiles = new File(path);
                        File[] fileList = allFiles.listFiles();

                        for (File file : fileList){
                            String name = file.getName().split("-")[0];

                            if (name.equals(scriptName))
                                file.delete();
                        }

                        dialog.dismiss();

                        BackToMenu();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        areYouSure.show();
    }

    public void BackToMenu() {
        Intent intent = new Intent(this, LibraryActivity.class);

        startActivity(intent);
    }

    public void menu(View view) {
        BackToMenu();
    }
}
