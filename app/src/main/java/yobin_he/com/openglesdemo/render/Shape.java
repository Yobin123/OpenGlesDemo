package yobin_he.com.openglesdemo.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.View;

/**
 * @author : yobin_he
 * @package: yobin_he.com.openglesdemo.render
 * @fileName: Shape
 * @Date : 2018/12/26  17:58
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public abstract class Shape implements GLSurfaceView.Renderer {
    protected View mView;
    public Shape(View view) {
        this.mView = view;
    }

    public int loadShader(int type ,String shaderCode){
        //根据type创建顶点着色器或者片元着色器
        int shader  = GLES20.glCreateShader(type);
        //将资源加入到着色器中，并编译
        GLES20.glShaderSource(shader,shaderCode);
        GLES20.glCompileShader(shader);
        return  shader;
    }
}
