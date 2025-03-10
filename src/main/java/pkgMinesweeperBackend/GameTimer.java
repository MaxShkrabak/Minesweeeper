package pkgMinesweeperBackend;

public class GameTimer {
    private boolean running = false;
    private long startTime = 0;
    private long currentTIme = 0;

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
        return currentTIme;
    }

    public void resetTimer() {
        running = false;
        startTime = 0;
        currentTIme = 0;
    }

    public void stopTimer() {
        if (running) {
            currentTIme = (System.currentTimeMillis() - startTime) / 1000;
            running = false;
        }
    }
}
