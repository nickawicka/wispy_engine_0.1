	#type vertex
	#version 330 core

	layout (location=0) in vec3 aPos;
	layout (location=1) in vec4 aColor;

	uniform mat4 u_proj_matrix;
	uniform mat4 u_view_matrix;

	out vec4 fColor;

	void main() {
		fColor = aColor;
		gl_Position = u_proj_matrix * u_view_matrix * vec4(aPos, 1.0);
	}

	#type fragment
	#version 330 core

	in vec4 fColor;
	out vec4 color;

	void main() {
		color = fColor;
	}
