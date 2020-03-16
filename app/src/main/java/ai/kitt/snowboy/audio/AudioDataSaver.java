package ai.kitt.snowboy.audio;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ai.kitt.snowboy.Constants;
import android.util.Log;

public class AudioDataSaver implements AudioDataReceivedListener {
    

    private File saveFile;
    private DataOutputStream dataOutputStreamInstance = null;

    public AudioDataSaver() {
        saveFile = new File(Constants.SAVE_AUDIO);
        Log.e(Constants.SAVE_AUDIO,"1");
    }

    @Override
    public void start() {
        if (null != saveFile) {
            if (saveFile.exists()) {
                saveFile.delete();
            }
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                Log.e( "IO Exc on creat af", saveFile.toString());
            }

            try {
                BufferedOutputStream bufferedStreamInstance = new BufferedOutputStream(
                        new FileOutputStream(this.saveFile));
                dataOutputStreamInstance = new DataOutputStream(bufferedStreamInstance);
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("Cannot Open File", e);
            }
        }
    }

    @Override
    public void onAudioDataReceived(byte[] data, int length) {
        try {
            if (null != dataOutputStreamInstance) {
                dataOutputStreamInstance.write(data, 0, length);
            }
        } catch (IOException e) {
            Log.e( "IO Exc on saving af", saveFile.toString());
        }
    }

    @Override
    public void stop() {
        if (null != dataOutputStreamInstance) {
            try {
                dataOutputStreamInstance.close();
            } catch (IOException e) {
                Log.e( "IO Ex on finish savi af", saveFile.toString());
            }
            Log.e("Recording saved to " + saveFile.toString(),"1");
        }
    }
}
