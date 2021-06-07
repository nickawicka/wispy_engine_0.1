	#type vertex
	#version 330 core

	layout (location=0) in vec3 a_pos;
	layout (location=1) in vec4 a_color;
	layout (location=2) in vec2 a_tex_coords;

	uniform mat4 u_proj_matrix;
	uniform mat4 u_view_matrix;

	out vec4 f_color;
	out vec2 f_tex_coords;

	void main() {
		f_color = a_color;
		f_tex_coords = a_tex_coords;
		gl_Position = u_proj_matrix * u_view_matrix * vec4(a_pos, 1.0);
	}

	#type fragment
	#version 330 core

	uniform float u_time;
	uniform sampler2D TEX_SAMPLER;

	in vec4 f_color;
	in vec2 f_tex_coords;

	out vec4 color;

	void main() {
		color = texture(TEX_SAMPLER, f_tex_coords);
	}
