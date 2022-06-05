package com.mania.util.render;

import org.lwjgl.opengl.ARBShaderObjects;

public class AbstractShader {
	
	private final int shaderProgram;
	
	protected AbstractShader() {
		this.shaderProgram = ARBShaderObjects.glCreateProgramObjectARB();
	}
	
	private void createShaderObject(String name, int type) {
		final int shaderID = ARBShaderObjects.glCreateShaderObjectARB(type);
		ARBShaderObjects.glShaderSourceARB(shaderID, name);
		ARBShaderObjects.glCompileShaderARB(shaderID);
		if (ARBShaderObjects.glGetObjectParameterfARB(shaderID, 35713) != 0) {
			ARBShaderObjects.glAttachObjectARB(this.shaderProgram, shaderID);
		} else {
			ARBShaderObjects.glDeleteObjectARB(shaderID);
		}
	}
	
	protected final void setShader(String name, String name2) {
		createShaderObject(name, 35633);
	    createShaderObject(name2, 35632);
	    ARBShaderObjects.glLinkProgramARB(this.shaderProgram);
	    if (ARBShaderObjects.glGetObjectParameteriARB(this.shaderProgram, 35714) != 0) {
	    	ARBShaderObjects.glValidateProgramARB(this.shaderProgram);
	    }
	}
	
	protected final void bindShaderProgram() {
		ARBShaderObjects.glUseProgramObjectARB(this.shaderProgram);
	}
	
	protected final int getUniform(String paramString) {
		return ARBShaderObjects.glGetUniformLocationARB(this.shaderProgram, paramString);
	}
	
	protected final void unbindCurrentShaderProgram() {
		ARBShaderObjects.glUseProgramObjectARB(0);
	}

}
