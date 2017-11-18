import java.util.Random;

import static java.lang.Math.abs;

public class Particle {
    public double[] position = new double[16];
    public double[] velocity = new double[16];

    public Particle() {
        for (int i = 0; i < position.length; i++) {
            position[i] = abs(new Random().nextInt() % 12) + 48 + abs(new Random().nextInt() % 3) * 12;
            velocity[i] = new Random().nextInt() % 3;
        }
    }
}