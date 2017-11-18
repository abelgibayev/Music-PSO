import javax.sound.midi.*;

public class Player {
    private MidiChannel[] channels = null;
    private Synthesizer synth = null;

    public void play(int notes[], int[][]chords) throws InterruptedException, MidiUnavailableException, InvalidMidiDataException {
        synth = MidiSystem.getSynthesizer();
        synth.open();

        channels = synth.getChannels();
        channels[0].programChange(1);
        for (int i = 0; i < 32; i += 2) {
            int notePair[] = {notes[i], notes[i+1]};
            int chord1 = chords[0][i/2];
            int chord2 = chords[1][i/2];
            int chord3 = chords[2][i/2];
            playSound(0, 1000, 70, notePair, chord1, chord2, chord3);
        }
    }

    public void playSound(int channel, int duration, int volume, int[] note, int... chord) throws InterruptedException {
        for (int n: chord) {
            channels[channel].noteOn(n, volume);
        }

        channels[channel].noteOn(note[0], volume);
        Thread.sleep(duration/2);
        channels[channel].noteOff(note[0]);

        channels[channel].noteOn(note[1], volume);
        Thread.sleep(duration/2);
        channels[channel].noteOff(note[1]);

        for (int n: chord) {
            channels[channel].noteOff(n);
        }
    }

}