#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location =1) in vec4 aColor;
layout (location =2) in vec2 aTexCoords;
layout (location =3) in float aTexID;


uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexID;
void main()
{
    fTexCoords = aTexCoords;
    fTexID = aTexID;
    fColor = aColor;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);

}

#type fragment
#version 330 core
//uniform float uTime;
//uniform sampler2D TEX_SAMPLER;


in vec4 fColor;
in vec2 fTexCoords;
in float fTexID;
out vec4 color;

uniform sampler2D uTextures[8];

void main()
{
    if(fTexID > 0){
        int id = int(fTexID);
        //float noise = fract(sin(dot(fColor.xy, vec2(12.9898, 78.233))) * 43758.5453);
        //color = texture(TEX_SAMPLER, fTexCoords);
        color = fColor * texture(uTextures[id], fTexCoords);
    } else {
        color = fColor;
    }

}
