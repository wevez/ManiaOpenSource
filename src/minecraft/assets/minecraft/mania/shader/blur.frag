#version 120

uniform sampler2D currentTexture;
uniform vec2 texelSize;
uniform vec2 coords;
uniform float blurRadius;
uniform float blursigma;
uniform vec3 rectcolor;
//https://stackoverflow.com/questions/11282394/what-kind-of-blurs-can-be-implemented-in-pixel-shaders
//https://thebookofshaders.com/glossary/?search=exp


float CalcGauss(float x, float sigma)
{

    float sigmaMultiplication =  ((blursigma * blursigma));

    if (blursigma < 1) {
        return (exp(-.5 * x * x) * .4);
    } else {

        return (exp(-.5 * x * x / (sigmaMultiplication))/ blursigma) * .4;//bisschen umgeschrieben von der eigendlichen methode, da die eigendliche für einen full solid blur ist
        // (exp(-.5) -> Color correction
    }
}

vec3 adjustExposure(vec4 color, float value) {
    return (1.0 + value) * color.rgb;
}

void main() {

    vec3 color = vec3(0.0);

    vec4 color2 = texture2D(currentTexture, gl_TexCoord[0].xy);

    for (float radiusF = -blurRadius; radiusF <= blurRadius; radiusF++) {
        color += texture2D(currentTexture, gl_TexCoord[0].st + radiusF * texelSize * coords).rgb * CalcGauss(radiusF, blurRadius);
    }


    color += vec4(rectcolor.rgb, .06).rgb;

    float gamma = 1F;

    gl_FragColor = vec4(pow(color.rgb, vec3(1.0/gamma)), 1.0);
}