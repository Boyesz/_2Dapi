package core;


import org.urish.openal.ALException;
import org.urish.openal.OpenAL;
import org.urish.openal.Source;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SoundSource {
    private static OpenAL openAL = null;
    private Source source;
    public SoundSource () {
        if(openAL == null) {
            try {
                openAL = new OpenAL();

            } catch (ALException e) {
                e.printStackTrace();
            }
        }
    }

    public void LoadFile (String path){
        try {
            source = openAL.createSource(new File(path));
        } catch (ALException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public void Play (){
        try {
            source.play();
        } catch (ALException e) {
            e.printStackTrace();
        }
    }
}