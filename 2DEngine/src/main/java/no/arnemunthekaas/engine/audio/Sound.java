package no.arnemunthekaas.engine.audio;

import no.arnemunthekaas.utils.GameConstants;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound {

    private int bufferID;
    private int sourceID;
    private String filepath;

    private boolean isPlaying;

    /**
     *
     * @param filepath
     * @param loops
     */
    public Sound(String filepath, boolean loops) {
        this.filepath = filepath;

        // Allocate space for stb return info
        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer);

        if (rawAudioBuffer == null) {
            System.out.println("Could not load sound: '" + filepath + "'");
            stackPop();
            stackPop();
            return;
        }

        // Retrieve extra info stored in buffers
        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        // Free
        stackPop();
        stackPop();

        // Find correct openAL format
        int format = -1;

        if (channels == 1)
            format = AL_FORMAT_MONO16;
        else if ( channels == 2)
            format = AL_FORMAT_STEREO16;


        bufferID = alGenBuffers();
        alBufferData(bufferID, format, rawAudioBuffer, sampleRate);

        // Generate source
        sourceID = alGenSources();


        alSourcei(sourceID, AL_BUFFER, bufferID);
        alSourcei(sourceID, AL_LOOPING, loops ? 1 : 0);
        alSourcei(sourceID, AL_POSITION, 0);
        alSourcef(sourceID, AL_GAIN, GameConstants.gain); // TODO: change gain for different sounds? / sound settings

        // Free stb raw audio buffer
        free(rawAudioBuffer);

    }

    /**
     * Delete sound
     */
    public void delete() {
        alDeleteSources(sourceID);
        alDeleteBuffers(bufferID);
    }

    /**
     * Plays a sound
     */
    public void play() {
        int state = alGetSourcei(sourceID, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
            alSourcei(sourceID, AL_POSITION, 0);
        }

        if (!isPlaying) {
            alSourcePlay(sourceID);
            isPlaying = true;
        }
    }

    /**
     * Stops a sound
     */
    public void stop() {
        if(isPlaying) {
            alSourceStop(sourceID);
            isPlaying = false;
        }
    }

    /**
     * Get filepath of sound
     * @return
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * Get if sound is playing
     * @return
     */
    public boolean isPlaying() {
        int state = alGetSourcei(sourceID, AL_SOURCE_STATE);
        if (state == AL_STOPPED)
            isPlaying = false;
        return isPlaying;
    }
}
