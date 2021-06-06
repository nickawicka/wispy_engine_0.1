package renderer;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
	
	private int shader_program_id;
	
	private String vertex_source;
	private String fragment_source;
	private String file_path;
	
	public Shader(String file_path) {
		this.file_path = file_path;
		try {
			String source = new String(Files.readAllBytes(Paths.get(file_path)));
			String[] split_string = source.split("(#type)( )+([a-zA-Z]+)");
			
			// Find the first pattern after #type 'pattern'
			int index = source.indexOf("#type") + 6;
			int eol = source.indexOf("\r\n", index);
			String first_pattern = source.substring(index, eol).trim();
			
			// Find the second pattern after #type 'pattern'
			index = source.indexOf("#type", eol) + 6;
			eol = source.indexOf("\r\n", index);
			String second_pattern = source.substring(index, eol).trim();
			
			if (first_pattern.equals("vertex")) {
				vertex_source = split_string[1];
			} else if (first_pattern.equals("fragment")) {
				fragment_source = split_string[1];
			} else {
				throw new IOException("Unexpected token '" + first_pattern + "'");
			}
			
			if (second_pattern.equals("vertex")) {
				vertex_source = split_string[2];
			} else if (second_pattern.equals("fragment")) {
				fragment_source = split_string[2];
			} else {
				throw new IOException("Unexpected token '" + second_pattern + "'");
			}
		} catch (IOException e) {
			e.printStackTrace();
			assert false : "Error: Could not open file for shader: '" + file_path + "'";
		}
	}
	
	public void compile() {
		/*
		 *  Compile and link shader's
		 */
		
		int vertex_id, fragment_id;
		
		// First load and compile the vertex shader
		vertex_id = glCreateShader(GL_VERTEX_SHADER);
		// Pass the shader source to the GPU
		glShaderSource(vertex_id, vertex_source);
		glCompileShader(vertex_id);
		
		// Check for errors in compilation
		int success = glGetShaderi(vertex_id, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(vertex_id, GL_INFO_LOG_LENGTH);
			System.out.println("Error: '" + file_path + "'\n\tVertex shader compilation failed.");
			System.out.println(glGetShaderInfoLog(vertex_id, len));
			assert false : "";
		}
		
		// First load and compile the fragment shader
		fragment_id = glCreateShader(GL_FRAGMENT_SHADER);
		// Pass the shader source to the GPU
		glShaderSource(fragment_id, fragment_source);
		glCompileShader(fragment_id);
				
		// Check for errors in compilation
		success = glGetShaderi(fragment_id, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragment_id, GL_INFO_LOG_LENGTH);
			System.out.println("Error: '" + file_path + "'\n\tFragment shader compilation failed.");
			System.out.println(glGetShaderInfoLog(fragment_id, len));
			assert false : "";
		}
		
		// Link the shader's and check for errors
		shader_program_id = glCreateProgram();
		glAttachShader(shader_program_id, vertex_id);
		glAttachShader(shader_program_id, fragment_id);
		glLinkProgram(shader_program_id);
		
		// Check for linking errors
		success = glGetProgrami(shader_program_id, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			int len = glGetProgrami(shader_program_id, GL_INFO_LOG_LENGTH);
			System.out.println("Error: '" + file_path + "'\n\tLinking pf shaders failed.");
			System.out.println(glGetProgramInfoLog(shader_program_id, len));
			assert false : "";
		}		
	}
	
	public void use() {
		// Bind shader program
		glUseProgram(shader_program_id);
	}
	
	public void detach() {
		glUseProgram(0);
	}
	
	public void uploadMat4f(String var_name, Matrix4f mat4) {
		int var_location = glGetUniformLocation(shader_program_id, var_name);
		FloatBuffer mat_buffer = BufferUtils.createFloatBuffer(16);
		mat4.get(mat_buffer);
		glUniformMatrix4fv(var_location, false, mat_buffer);
	}
}
