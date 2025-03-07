#version 430 core

uniform vec4 rectangleColor;  // New color uniform for rectangle
uniform sampler2D TEX_SAMPLER; // Texture uniform for tiles

in vec2 fTexCoords;
out vec4 color;

void main() {
    if (gl_FragCoord.x > 2.0 && gl_FragCoord.x < 672.0 && gl_FragCoord.y > 673.0 && gl_FragCoord.y < 732.0) {
        // This is where the rectangle is being rendered
        color = rectangleColor;
    } else {
        // This is where the tiles are being rendered
        color = texture(TEX_SAMPLER, fTexCoords);
    }
}
