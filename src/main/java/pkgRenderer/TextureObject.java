package pkgRenderer;

import org.lwjgl.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;

public class TextureObject {
    private final String texFilepath;
    private final int texID;

    public TextureObject(String filepath) {
        this.texFilepath = filepath;
        texID = glGenTextures();
        bind_texture();
        // If we don't enable blending, the transparent pixels in the texture
        // will render as dark pixels:
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        loadImageToTexture();
    }

    public void bind_texture() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }
    public void unbind_texture() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void loadImageToTexture() {
        IntBuffer texWidth = BufferUtils.createIntBuffer(1);
        IntBuffer texHeight = BufferUtils.createIntBuffer(1);
        IntBuffer texChannels = BufferUtils.createIntBuffer(1);

        ByteBuffer texImage = TextureLoader.loadImage(texFilepath, texWidth, texHeight, texChannels);

        int format = (texChannels.get(0) == 4) ? GL_RGBA : GL_RGB;

        glTexImage2D(GL_TEXTURE_2D, 0, format, texWidth.get(0), texHeight.get(0), 0, format, GL_UNSIGNED_BYTE, texImage);

        stbi_image_free(texImage);
    }
}
