uniform vec2 resolution;
uniform float time;
uniform vec2 mouse;

void main() {
    vec2 uv = -1.0 + 2.0 * gl_FragCoord.xy / resolution.xy;
    uv.x *= resolution.x / resolution.y;
    vec2 ms = (mouse.xy / resolution.xy);

    vec3 color = vec3(0.9);
    for(int i = 0; i < 100; i++)
    {
    }

    color *= sqrt(1.5 - 0.5 * length(uv));

    gl_FragColor = vec4(color,1.0);
}