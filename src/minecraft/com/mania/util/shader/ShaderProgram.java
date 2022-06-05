package com.mania.util.shader;

import org.lwjgl.opengl.GL20;

import com.mania.MCHook;
import com.mania.util.FileUtil;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram implements MCHook {
	
	private final int shaderProgramID;
	
	protected ShaderProgram(String shaderName) {
		final int shaderProgramID = GL20.glCreateProgram();
        final String vertexSource = FileUtil.readFile("shader/vertex.vert");
        final int vertexShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        { // compiles vertex shader
            GL20.glShaderSource(vertexShaderID, vertexSource);
            GL20.glCompileShader(vertexShaderID);
            if (GL20.glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            	System.out.println(glGetShaderInfoLog(vertexShaderID, 4096));
                throw new IllegalStateException("Unable to decompile vertex shader: " + GL_VERTEX_SHADER);
            }
        }
        // compiles frag or GLSL shader
        final String fragmentSource = FileUtil.readFile("shader/" + shaderName + ".frag");
        final int fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentSource);
        glCompileShader(fragmentShaderID);
        if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
    		System.out.println(glGetShaderInfoLog(fragmentShaderID, 4096));
            throw new IllegalStateException("Unable to decompile shader: " + shaderName + GL_VERTEX_SHADER);
        }
        glAttachShader(shaderProgramID, vertexShaderID);
        glAttachShader(shaderProgramID, fragmentShaderID);
        glLinkProgram(shaderProgramID);
        this.shaderProgramID = shaderProgramID;
	}
	
	protected abstract void doRender();
	
	protected final int getShaderProgramID() {
		return this.shaderProgramID;
	}
	protected void setShaderUniformI(String name, int... args) {
        int loc = glGetUniformLocation(getShaderProgramID(), name);
        if (args.length > 1) {
            glUniform2i(loc, args[0], args[1]);
        } else {
            glUniform1i(loc, args[0]);
        }
    }


    protected void setShaderUniform(String name, float... args) {
        int loc = glGetUniformLocation(shaderProgramID, name);
        if (args.length > 1) {
            if (args.length > 2) {
                if (args.length > 3) {
                    GL20.glUniform4f(loc, args[0], args[1], args[2], args[3]);
                } else {
                    GL20.glUniform3f(loc, args[0], args[1], args[2]);
                }
            } else {
                GL20.glUniform2f(loc, args[0], args[1]);
            }
        } else {
            GL20.glUniform1f(loc, args[0]);
        }
    }

}
