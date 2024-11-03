package pkgDriver;

import pkgSlRenderer.MSPingPong;

public class MSDriver {
    public static void main(String[] args) {
        MSPingPong newPingPong = new MSPingPong(5,5);  // Another board with 0 - 1 elements

        System.out.println("\nNew board:");
        newPingPong.printArray();

        newPingPong.countNNN();  // Next nearest neighbors
        newPingPong.swapArray(); // Swap

        System.out.println("\nNext-Nearest Neighbors Array:");
        newPingPong.printArray();
    }
}
