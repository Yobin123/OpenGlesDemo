package yobin_he.com.openglesdemo.render;

import android.opengl.GLES20;
import android.view.View;
import android.widget.MediaController;

import java.lang.reflect.Constructor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author : yobin_he
 * @package: yobin_he.com.openglesdemo.render
 * @fileName: FGLRender
 * @Date : 2018/12/26  17:57
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class FGLRender extends Shape {

    private Shape shape;
    private Class<? extends Shape> clazz = Cube.class; // 设置为cube

    public FGLRender(View view) {
        super(view);
    }

    public void setShape(Class<? extends Shape> shape){
        this.clazz = shape;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //将背景设置为灰色。
        GLES20.glClearColor(0.5f,0.5f,0.5f,1f);
        try {
            Constructor constructor = clazz.getDeclaredConstructor(View.class);
            constructor.setAccessible(true);
            shape = (Shape) constructor.newInstance(mView);
        } catch (Exception e) {
            e.printStackTrace();
            shape = new Cube(mView);
        }
        shape.onSurfaceCreated(gl,config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        shape.onSurfaceChanged(gl,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        shape.onDrawFrame(gl);
    }
}
