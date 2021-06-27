#type vertex
#version 330 core
layout (location = 0) in vec3 a_pos;
layout (location = 1) in vec3 a_color;

out vec3 f_color;

uniform mat4 u_view;
uniform mat4 u_projection;

void main()
{
    f_color = a_color;

    gl_Position = u_projection * u_view * vec4(a_pos, 1.0);
}

#type fragment
#version 330 core
in vec3 f_color;

out vec4 color;

void main()
{
    color = vec4(f_color, 1);
} 