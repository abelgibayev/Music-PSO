import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws MidiUnavailableException, InterruptedException, InvalidMidiDataException, IOException {
        ChordGenerator go = new ChordGenerator();
        int[][] chords = go.PSO0();
        NotesGenerator n = new NotesGenerator(chords);
        int notes[] = n.PSO1();
        new MidiFileWriter().writeToMidiFile("Test.midi", chords, notes, 2); //output generated melody to MIDI file
//        Player pl = new Player();                                                      //play generated melody
//        pl.play(notes, chords);
    }
}
