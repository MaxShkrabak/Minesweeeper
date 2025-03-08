package pkgMinesweeperBackend;

public class GameTimer {
    private boolean running = false;
    private long startTime = 0;

    public void startTimer() {
       if (!running) {
           running = true;
           startTime = System.currentTimeMillis();
       }
    }

    public long getElapsedTime() {
        if (running) {
            return (System.currentTimeMillis() - startTime) / 1000;
        }
        return 0;
    }

    public void resetTimer() {
        running = false;
        startTime = 0;
    }
}
