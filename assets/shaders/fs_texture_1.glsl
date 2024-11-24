#version 430 core

uniform vec4 COLOR_FACTOR;
uniform sampler2D TEX_SAMPLER;

in vec2 fTexCoords;
out vec4 color;

void main()
{
    color = texture(TEX_SAMPLER, fTexCoords);
}
