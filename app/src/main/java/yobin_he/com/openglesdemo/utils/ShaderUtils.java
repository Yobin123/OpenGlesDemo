package yobin_he.com.openglesdemo.utils;

import android.content.res.Resources;
import android.icu.util.Freezable;
import android.opengl.GLES20;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author : yobin_he
 * @package: yobin_he.com.openglesdemo.utils
 * @fileName: ShaderUtils
 * @Date : 2019/1/8  18:51
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class ShaderUtils {


    public static void checkGLError(String op){
        Log.e("hyb",op);
    }

    public static int loadShader(int shaderType,String source){
        int shader = GLES20.glCreateShader(shaderType);
        if(0!= shader){
            GLES20.glShaderSource(shader,source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader,GLES20.GL_COMPILE_STATUS,compiled,0);
            if(compiled[0] == 0){
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public static int loadShader(Resources res,int shaderType,String resName){
        return loadShader(shaderType,loadFromAssetsFile(resName,res));
    }

    public static int createProgram(String vertexSource,String fragmentSource){
        int vertex = loadShader(GLES20.GL_VERTEX_SHADER,vertexSource);
        if(vertex == 0) return 0;
        int fragment = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentSource);
        if(fragment == 0) return 0;
        int program = GLES20.glCreateProgram();
        checkGLError("program ==" + program);
        if(program != 0){
            GLES20.glAttachShader(program,vertex);
            checkGLError("Attach Vertex Shader");
            GLES20.glAttachShader(program,fragment);
            checkGLError("Attach fragment shader");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program,GLES20.GL_LINK_STATUS,linkStatus,0);
            if(linkStatus[0] != GLES20.GL_TRUE){
                GLES20.glDeleteProgram(program);
                program = 0;
            }

        }

        return program;
    }

    public static int createProgram(Resources res,String vertexRes,String fragmentRes){
        return createProgram(loadFromAssetsFile(vertexRes,res),loadFromAssetsFile(fragmentRes,res));
    }

    public static String loadFromAssetsFile(String fileName,Resources res){
        StringBuilder result = new StringBuilder();
        try {
            InputStream is = res.getAssets().open(fileName);
            int ch;
            byte[] buffer = new byte[1024];
            while (-1 != (ch = is.read(buffer))){
                result.append(new String(buffer,0,ch));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result.toString().replaceAll("\\r\\n","\n");
    }
}
