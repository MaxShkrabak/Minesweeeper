package pkgUtils;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Icon {
    public static void setIcon(long winID, String iconPath) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // Load image using STB
        ByteBuffer image = STBImage.stbi_load(iconPath, width, height, channels, 4);
        if (image == null) {
            throw new RuntimeException("Failed to load icon: " + STBImage.stbi_failure_reason());
        }

        // Create a GLFWImage
        GLFWImage icon = new GLFWImage(image);
        icon.width(width.get(0));
        icon.height(height.get(0));
        icon.pixels(image);

        // Create an icon buffer with the icon
        GLFWImage.Buffer iconBuffer = GLFWImage.create(1);
        iconBuffer.put(0, icon);

        // Set the window icon
        GLFW.glfwSetWindowIcon(winID, iconBuffer);

        // Free the image after setting the icon
        STBImage.stbi_image_free(image);
    }
}
