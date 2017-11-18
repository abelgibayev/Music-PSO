import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class MidiFileWriter {

    public static void writeToMidiFile(String fileName, int[][] chords, int[] notes, int duration) throws InvalidMidiDataException, IOException {

        File outputFile = new File(fileName);
        Sequence sequence;
        sequence = new Sequence(Sequence.PPQ, 1);

        Track track = sequence.createTrack();
        int start = 0;
        for (int i = 0; i < 32; i += 2) {
            //Turning on chord notes
            for (int j = 0; j < 3; j++) {
                track.add(MidiFileWriter.createNoteOnEvent(chords[j][i / 2], start));
            }
            //Turning on and turning off first note
            track.add(MidiFileWriter.createNoteOnEvent(notes[i], start));
            track.add(MidiFileWriter.createNoteOffEvent(notes[i], start + duration / 2));
            //Turning on and turning off second note
            track.add(MidiFileWriter.createNoteOnEvent(notes[i + 1], start + duration / 2));
            track.add(MidiFileWriter.createNoteOffEvent(notes[i + 1], start + duration));
            //Turning on chord notes
            for (int j = 0; j < 3; j++) {
                track.add(MidiFileWriter.createNoteOffEvent(chords[j][i / 2], start + duration));
            }
            start += duration;
        }

        MidiSystem.write(sequence, 0, outputFile);
    }

    private static MidiEvent createNoteOnEvent(int nKey, long lTick) throws InvalidMidiDataException {
        return createNoteEvent(ShortMessage.NOTE_ON, nKey, 85, lTick);
    }

    private static MidiEvent createNoteOffEvent(int nKey, long lTick) throws InvalidMidiDataException {
        return createNoteEvent(ShortMessage.NOTE_OFF, nKey, 0, lTick);
    }

    private static MidiEvent createNoteEvent(int nCommand, int nKey, int nVelocity, long lTick) throws InvalidMidiDataException {
        ShortMessage message = new ShortMessage();
        message.setMessage(nCommand, 0, nKey, nVelocity);
        MidiEvent event = new MidiEvent(message, lTick);
        return event;
    }

}