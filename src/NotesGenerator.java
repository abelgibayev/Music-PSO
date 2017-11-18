import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class NotesGenerator {

    int[][] chords;

    public NotesGenerator(int[][] ch) {
        this.chords = ch;                                       //Chords of the melody
    }

    double n = 0.3, s1 = 0.7, s2 = 0.9;

    Particle pbest[] = new Particle[4000];
    Particle gbest = new Particle();
    Particle swarm[] = new Particle[4000];

    public void initialization() {                              //Init the swarm
        gbest = new Particle();
        pbest = new Particle[4000];
        swarm = new Particle[4000];
        for (int i = 0; i < swarm.length; i++) {
            swarm[i] = new Particle();
            pbest[i] = swarm[i];
            if (fitnessFunction(pbest[i]) < fitnessFunction(gbest)) gbest = pbest[i];
        }
    }

    public void jump(int i, int j) {
        while (swarm[i].position[j] < chords[2][j] && swarm[i].position[j] + 12 < 96) {
            swarm[i].position[j] += 12;
        }
    }

    int steps = 0;

    Particle generate() {
        boolean success = false;
        int count;

        while (!success) {
            initialization();
            count = 0;
            while (fitnessFunction(gbest) != 0) {
                if (count > 20) break;
                else {
                    steps++;
                    for (int i = 0; i < swarm.length; i++) {
                        for (int j = 0; j < swarm[i].position.length; j++) {
                            double r1 = abs(new Random().nextDouble());
                            double r2 = abs(new Random().nextDouble());
                            swarm[i].velocity[j] = (n * swarm[i].velocity[j] +
                                    s1 * r1 * (pbest[i].position[j] - swarm[i].position[j]) +
                                    s2 * r2 * (gbest.position[j] - swarm[i].position[j]));
                        }

                        for (int j = 0; j < swarm[j].position.length; j++) {
                            swarm[i].position[j] += swarm[i].velocity[j];
                            jump(i, j);
                        }

                        if (fitnessFunction(swarm[i]) < fitnessFunction(pbest[i]))
                            pbest[i] = swarm[i];

                        if (fitnessFunction(pbest[i]) < fitnessFunction(gbest))
                            gbest = pbest[i];
                    }
                    count++;
                }
            }
            if (fitnessFunction(gbest) == 0) success = true;
        }
        return gbest;
    }

    int[] PSO1() {
        Particle n = generate();
        Particle n1 = generate();
        int[] notes = new int[32];                  //Array of notes
        for (int i = 0; i < notes.length; i+=2) {
            notes[i] = (int)n.position[i/2];
            notes[i + 1] = (int)n1.position[i/2];
        }
        System.out.println(steps);
        return notes;
    }

    int fitnessFunction(Particle particle) {
        int error = 0;
        for (int i = 0; i < 16; i++) {
            int dif1 = abs((int)particle.position[i] % 12 - chords[0][i] % 12);
            int dif2 = abs((int)particle.position[i] % 12 - chords[1][i] % 12);
            int dif3 = abs((int)particle.position[i] % 12 - chords[2][i] % 12);
            error += min(dif1, min(dif2, dif3));
            if (i > 0) {
                if (abs(particle.position[i] - particle.position[i-1]) > 12) error += 100;
            }
        }
        return error;
    }
}