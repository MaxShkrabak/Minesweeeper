#version 430 core

uniform sampler2D TEX_SAMPLER;  // Texture uniform for tiles
uniform vec4 rectangleColor;  // Color uniform for the rectangle

in vec2 fTexCoords;
out vec4 color;

void main() {
    if (rectangleColor != vec4(0.0f, 0.0f, 0.0f, 1.0f)) {
        color = rectangleColor;  // Use the color if the texture isn't applied
    } else {
        color = texture(TEX_SAMPLER, fTexCoords);  // Otherwise, use the texture
    }


}
