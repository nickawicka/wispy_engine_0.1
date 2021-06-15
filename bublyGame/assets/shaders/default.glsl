	#type vertex
	#version 330 core

	layout (location=0) in vec3 a_pos;
	layout (location=1) in vec4 a_color;
	layout (location=2) in vec2 a_tex_coords;
	layout (location=3) in float a_tex_id;

	uniform mat4 u_proj_matrix;
	uniform mat4 u_view_matrix;

	out vec4 f_color;
	out vec2 f_tex_coords;
	out float f_tex_id;

	void main() {
		f_color = a_color;
		f_tex_coords = a_tex_coords;
		f_tex_id = a_tex_id;

		gl_Position = u_proj_matrix * u_view_matrix * vec4(a_pos, 1.0);
	}

	#type fragment
	#version 330 core

	in vec4 f_color;
	in vec2 f_tex_coords;
	in float f_tex_id;

	uniform sampler2D u_textures[8];

	out vec4 color;

	void main() {
		if (f_tex_id > 0) {
			int id = int(f_tex_id);
			color = f_color * texture(u_textures[id], f_tex_coords);
			//color = vec4(f_tex_coords, 0, 1);
		} else {
			color = f_color;
		}
	}
