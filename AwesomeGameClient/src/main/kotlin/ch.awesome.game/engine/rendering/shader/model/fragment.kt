package ch.awesome.game.engine.rendering.shader.model

import ch.awesome.game.engine.rendering.GameRenderer

//language=GLSL
val modelFragmentShaderSource = """
#version 300 es
precision mediump float;

in vec2 passTextureCoords;
in vec3 worldNormal;
in vec3 toLightVector[${GameRenderer.maxLights}];
in vec3 vecToCam;

out vec4 outColor;

uniform sampler2D modelTexture;
uniform vec3 lightColor[${GameRenderer.maxLights}];
uniform vec3 lightAttenuation[${GameRenderer.maxLights}];

const float reflectivity = 1.0;
const float shineDamper = 20.0;

void main()
{
    vec3 unitNormal = normalize(worldNormal);
    vec3 unitVecToCam = normalize(vecToCam);

    vec3 diffuse = vec3(0.0);
    vec3 specular = vec3(0.0);

    for(int i = 0; i < ${GameRenderer.maxLights}; i++) {
        float lightDistance = length(toLightVector[i]);
        float attenuation = lightAttenuation[i].x + (lightAttenuation[i].y * lightDistance) +
                            (lightAttenuation[i].z * lightDistance * lightDistance);

        vec3 unitToLightVector = normalize(toLightVector[i]);

        float diffuseDot = dot(unitNormal, unitToLightVector);
        float brightness = max(diffuseDot, 0.0);

        vec3 lightDir = -unitToLightVector;
        vec3 reflectedLightDir = reflect(lightDir, unitNormal);
        float specularBrightness = dot(reflectedLightDir, unitVecToCam);
        specularBrightness = max(specularBrightness, 0.0);
        float dampedBrightness = pow(specularBrightness, shineDamper);

        diffuse += (brightness * lightColor[i]) / attenuation;
        specular += (dampedBrightness * lightColor[i]) / attenuation;
    }

    outColor = vec4(diffuse, 1.0) * texture(modelTexture, passTextureCoords) + vec4(specular, 1.0);
}
""".trimIndent()