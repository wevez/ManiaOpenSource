package wtf.mania.util.shader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL20.*;

public class GLSLSandboxShader {
    private final int programId;
    private final int timeUniform;
    private final int mouseUniform;
    private final int resolutionUniform;

    public GLSLSandboxShader(String vertexIn, String fragmentIn) {
        int program = glCreateProgram();
        glAttachShader(program, createShader(this.getClass().getResourceAsStream("/assets/minecraft/shader/" + vertexIn), GL_VERTEX_SHADER));
        glAttachShader(program, createShader(this.getClass().getResourceAsStream("/assets/minecraft/shader/" + fragmentIn), GL_FRAGMENT_SHADER));
        glLinkProgram(program);
        int linked = glGetProgrami(program, GL_LINK_STATUS);

        if (linked == 0) {
            System.err.println(glGetProgramInfoLog(program, glGetProgrami(program, GL_INFO_LOG_LENGTH)));
            throw new IllegalStateException("Shader failed to link");
        }

        this.programId = program;
        glUseProgram(program);
        this.timeUniform = glGetUniformLocation(program, "time");
        this.mouseUniform = glGetUniformLocation(program, "mouse");
        this.resolutionUniform = glGetUniformLocation(program, "resolution");
        glUseProgram(0);
    }

    public void useShader(int width, int height, float mouseX, float mouseY, float time) {
        glUseProgram(this.programId);
        glUniform2f(this.resolutionUniform, width, height);
        glUniform2f(this.mouseUniform, mouseX / width, 1.0f - mouseY / height);
        glUniform1f(this.timeUniform, time);
    }

    private int createShader(InputStream inputStream, int shaderType) {
        int shader = glCreateShader(shaderType);
        glShaderSource(shader, readStreamToString(inputStream));
        glCompileShader(shader);
        int compiled = glGetShaderi(shader, GL_COMPILE_STATUS);

        if (compiled == 0) {
            System.err.println(glGetShaderInfoLog(shader, glGetShaderi(shader, GL_INFO_LOG_LENGTH)));
            throw new IllegalStateException("Failed to compile shader");
        }

        return shader;
    }

    private String readStreamToString(InputStream inputStream) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            byte[] buffer = new byte[512];
            int read;

            while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }
}