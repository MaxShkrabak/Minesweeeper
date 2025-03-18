package pkgRenderer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.stbi_load;

public class TextureLoader {

    public static ByteBuffer loadImage(String filepath, IntBuffer width, IntBuffer height, IntBuffer channels) {
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);
        if (image == null) {
            throw new RuntimeException("Failed to load image: " + filepath);
        }
        return image;
    }

    public static TextureObject[] loadGameTextures() {
        TextureObject[] gameArray = new TextureObject[14];

        gameArray[0] = new TextureObject("assets/images/Zero.png");
        gameArray[1] = new TextureObject("assets/images/One.png");
        gameArray[2] = new TextureObject("assets/images/Two.png");
        gameArray[3] = new TextureObject("assets/images/Three.png");
        gameArray[4] = new TextureObject("assets/images/Four.png");
        gameArray[5] = new TextureObject("assets/images/Five.png");
        gameArray[6] = new TextureObject("assets/images/Six.png");
        gameArray[7] = new TextureObject("assets/images/Seven.png");
        gameArray[8] = new TextureObject("assets/images/Eight.png");
        gameArray[9] = new TextureObject("assets/images/GameMine.png");
        gameArray[10] = new TextureObject("assets/images/Flag.png");
        gameArray[11] = new TextureObject("assets/images/Default.png");
        gameArray[12] = new TextureObject("assets/images/ResetButton.png");
        gameArray[13] = new TextureObject("assets/images/CorrectFlag.png");
        
        return gameArray;
    }
    
    public static TextureObject[] loadTimerTextures() {
        TextureObject[] timerArray = new TextureObject[10];

        timerArray[0] = new TextureObject("assets/timer/TimerZero.png");
        timerArray[1] = new TextureObject("assets/timer/TimerOne.png");
        timerArray[2] = new TextureObject("assets/timer/TimerTwo.png");
        timerArray[3] = new TextureObject("assets/timer/TimerThree.png");
        timerArray[4] = new TextureObject("assets/timer/TimerFour.png");
        timerArray[5] = new TextureObject("assets/timer/TimerFive.png");
        timerArray[6] = new TextureObject("assets/timer/TimerSix.png");
        timerArray[7] = new TextureObject("assets/timer/TimerSeven.png");
        timerArray[8] = new TextureObject("assets/timer/TimerEight.png");
        timerArray[9] = new TextureObject("assets/timer/TimerNine.png");

        return timerArray;
    }
}
