import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class ChordGenerator {

    int steps = 0;
    int tonality = 0;
    int majority = 1;
    double m[] = {0.45, 0.3, 0.3};
    double c1[] = {0.45, 0.7, 0,7};
    double c2[] = {0.88, 0.9, 0.9};                               //Constants for generate
    final int SWARM_SIZE = 4000;

    Particle pbest[] = new Particle[SWARM_SIZE];                        //Swarm of best solutions
    Particle gbest = new Particle();                              //global best
    Particle swarm[] = new Particle[SWARM_SIZE];                        //Swarm of particles
    int offset1 = 3 + majority;                                   //3 for minor, 4 for major
    int offset2 = 7;                                              //always 7

    public void initialization(int flag) {                        //Initialization of all particles
        gbest = new Particle();
        pbest = new Particle[SWARM_SIZE];
        swarm = new Particle[SWARM_SIZE];
        for (int i = 0; i < swarm.length; i++) {
            swarm[i] = new Particle();
            pbest[i] = swarm[i];
            if (fitnessFuncction(pbest[i], flag) < fitnessFuncction(gbest, flag)) gbest = pbest[i];
        }
    }

    public void jump(int i, int j) {                             //Checking if the value is out of bounds
        if (swarm[i].position[j] < 48) {
            swarm[i].position[j] += 12;
        }
        if (swarm[i].position[j] > 76) {
            swarm[i].position[j] -= 12;
        }
    }

    public void differ(int i, int j) {                           //Differing lines of 4 and more chords
        if (swarm[i].position[j] + 12 < 84) {
            swarm[i].position[j] += 12;
        } else
        if (swarm[i].position[j] - 12 >= 48) {
            swarm[i].position[j] -= 12;
        }
    }

    Particle generate(int flag) {                                //Find the first note of the chord
        boolean success = false;
        int count;

        while (!success) {
            initialization(flag);
            count = 0;
            while (fitnessFuncction(gbest, flag) != 0) {
                if (count > 20) break;
                else {
                    ++steps;
                    for (int i = 0; i < swarm.length; i++) {
                        for (int j = 0; j < swarm[i].position.length; j++) {
                            double r1 = abs(new Random().nextDouble());
                            double r2 = abs(new Random().nextDouble());
                            swarm[i].velocity[j] = (m[flag] * swarm[i].velocity[j] +
                                    c1[flag] * r1 * (pbest[i].position[j] - swarm[i].position[j]) +
                                    c2[flag] * r2 * (gbest.position[j] - swarm[i].position[j]));
                        }

                        for (int j = 0; j < swarm[j].position.length; j++) {
                            swarm[i].position[j] += swarm[i].velocity[j];
                            if (flag == 0) {
                                jump(i, j);
                                if (j > 3 && (swarm[i].position[j] == swarm[i].position[j-1] &&
                                    swarm[i].position[j-1] == swarm[i].position[j-2] &&
                                    swarm[i].position[j-2] == swarm[i].position[j-3])) {
                                   differ(i, j);
                            }
                            }
                        }

                        if (fitnessFuncction(swarm[i], flag) < fitnessFuncction(pbest[i], flag))
                            pbest[i] = swarm[i];

                        if (fitnessFuncction(pbest[i], flag) < fitnessFuncction(gbest, flag))
                            gbest = pbest[i];
                    }
                    count++;
                }
            }
            if (fitnessFuncction(gbest, flag) == 0) success = true;
        }
        return gbest;
    }

    Particle chords[] = new Particle[3];

    int[][] PSO0() {
        int [][] list = new int[3][16];                             //Completed chords
        for (int u = 0; u < 3; u++) {
            chords[u] = generate(u);
            for (int i = 0; i < chords[u].position.length; i++) {
                list[u][i] = (int) chords[u].position[i];
            }
        }
        System.out.print("@" +steps + "  ");                        //Amount of iterations to get ideal chords sequence
        return list;
    }

    int fitnessFuncction(Particle particle, int flag) {
        if (flag == 0) {
            int error = 0;
            for (int i = 0; i < particle.position.length; i++) {
                int t = (int) (particle.position[i] % 12);
                int s = t + 5;
                int d = t + 7;
                int dif1 = min(abs(t - (12 + tonality)), abs(t - tonality));
                int dif2 = min(abs(s - (12 + tonality)), abs(s - tonality));
                int dif3 = min(abs(d - (12 + tonality)), abs(d - tonality));
                error += min(dif1, min(dif2, dif3));
                if (i > 0) {
                    if (abs(particle.position[i] - particle.position[i - 1]) > 12) error += 10;
                }
            }
            return error;
        } else
        if (flag == 1) {
            int error = 0;
            for (int i = 0; i < particle.position.length; i++) {
                int a = (int)chords[0].position[i];
                int b = (int)particle.position[i];
                error += abs(b - (a + offset1));
            }
            return error;
        } else {
            int error = 0;
            for (int i = 0; i < particle.position.length; i++) {
                int a = (int)chords[0].position[i];
                int b = (int)particle.position[i];
                error += abs(b - (a + offset2));
            }
            return error;
        }
    }

}