package me.cortex.voxy.client.core.gl.shader;


import net.caffeinemc.mods.sodium.client.gl.shader.ShaderConstants;
import net.caffeinemc.mods.sodium.client.gl.shader.ShaderParser;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL20C;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShaderLoader {
    private static final Pattern GLSL_VERSION_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)");

    public static String parse(String id) {
        return "#version " + resolveSupportedGlslVersion() + " core\n" + ShaderParser.parseShader("\n#import <" + id + ">\n//beans", ShaderConstants.builder().build()).src().replaceAll("\r\n", "\n").replaceFirst("\n#version .+\n", "\n");
        //return me.jellysquid.mods.sodium.client.gl.shader.ShaderLoader.getShaderSource(new Identifier(id));
    }

    private static int resolveSupportedGlslVersion() {
        // Keep a high baseline while allowing systems that top out at GLSL 4.50 to run.
        String glslVersion = GL11C.glGetString(GL20C.GL_SHADING_LANGUAGE_VERSION);
        if (glslVersion == null) {
            return 450;
        }

        Matcher matcher = GLSL_VERSION_PATTERN.matcher(glslVersion);
        if (!matcher.find()) {
            return 450;
        }

        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        int numericVersion = major * 100 + minor * 10;

        return Math.max(330, Math.min(460, numericVersion));
    }
}
