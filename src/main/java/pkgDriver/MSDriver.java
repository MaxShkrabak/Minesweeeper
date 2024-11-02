package pkgDriver;

import pkgSlRenderer.MSPingPong;

public class MSDriver {
    public static void main(String[] args) {
        MSPingPong pingPongA = new MSPingPong(16,16,0,9); // 16x16 0-9

        System.out.println("Initial board:");
        pingPongA.printArray();

        MSPingPong newPingPong = new MSPingPong(16,16);  // Another board with 0 - 1 elements

        System.out.println("\nNew board:");
        newPingPong.printArray();

        newPingPong.countNN(); // Count nearest neighbors
        newPingPong.swapArray(); // Swap arrays

        System.out.println("\nNearest Neighbors Array:");
        newPingPong.printArray();

        newPingPong.swapArray(); // Retrieve original Array
        newPingPong.countNNN();  // Next nearest neighbors
        newPingPong.swapArray(); // Swap

        System.out.println("\nNext-Nearest Neighbors Array:");
        newPingPong.printArray();
    }
}
