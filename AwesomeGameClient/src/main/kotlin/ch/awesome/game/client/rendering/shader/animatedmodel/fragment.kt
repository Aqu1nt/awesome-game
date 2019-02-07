package ch.awesome.game.client.rendering.shader.animatedmodel

import ch.awesome.game.client.rendering.renderer.GameRenderer

//language=GLSL
val animatedModelFragmentShaderSource = """
#version 300 es
precision mediump float;

in vec2 passTextureCoords;
in vec3 worldNormal;
in vec3 vecToLight[${GameRenderer.MAX_LIGHTS}];
in vec3 vecToDirLight;
in vec3 vecToCam;
in float visibility;

out vec4 outColor;

uniform sampler2D modelTexture;
uniform sampler2D lightMap;

uniform vec3 lightColor[${GameRenderer.MAX_LIGHTS}];
uniform vec3 lightAttenuation[${GameRenderer.MAX_LIGHTS}];
uniform float ambientLight;
uniform vec3 directionalLightColor;
uniform vec3 skyColor;

uniform float reflectivity;
uniform float shineDamper;
uniform float useLightMap;

void main() {
    vec3 unitNormal = normalize(worldNormal);
    vec3 unitVecToCam = normalize(vecToCam);

    vec3 diffuse = vec3(0.0);
    vec3 specular = vec3(0.0);

    for(int i = 0; i < ${GameRenderer.MAX_LIGHTS}; i++) {
        float lightDistance = length(vecToLight[i]);
        float attenuation = lightAttenuation[i].x + (lightAttenuation[i].y * lightDistance) +
                            (lightAttenuation[i].z * lightDistance * lightDistance);

        vec3 unitVecToLight = normalize(vecToLight[i]);

        float diffuseDot = dot(unitNormal, unitVecToLight);
        float brightness = max(diffuseDot, 0.0);

        vec3 lightDir = -unitVecToLight;
        vec3 reflectedLightDir = reflect(lightDir, unitNormal);
        float specularBrightness = dot(reflectedLightDir, unitVecToCam);
        specularBrightness = max(specularBrightness, 0.0);
        float dampedBrightness = pow(specularBrightness, shineDamper);

        diffuse += (brightness * lightColor[i]) / attenuation;
        specular += (dampedBrightness * lightColor[i]) / attenuation;
    }

    diffuse = max(diffuse, ambientLight);

//    vec3 unitvecToDirLight = normalize(vecToDirLight);
//    float diffuseDot = dot(unitNormal, unitvecToDirLight);
//    float brightness = max(diffuseDot, 0.0);
//    diffuse += brightness * directionalLightColor;

//    vec3 lightDir = -vecToDirLight;
//    vec3 reflectedLightDir = reflect(lightDir, unitNormal);
//    float specularBrightness = dot(reflectedLightDir, unitVecToCam);
//    specularBrightness = max(specularBrightness, 0.0);
//    float dampedBrightness = pow(specularBrightness, shineDamper);
//    specular += dampedBrightness * directionalLightColor;

    if (useLightMap > 0.5) {
        vec4 mapData = texture(lightMap, passTextureCoords);
        specular *= mapData.r;
        if(mapData.y >= 0.5) {
            diffuse = vec3(1.0);
        }
    }

    outColor = vec4(diffuse, 1.0) * texture(modelTexture, passTextureCoords) + vec4(specular, 1.0);
    outColor = mix(vec4(skyColor, 1.0), outColor, visibility);
}
""".trimIndent()