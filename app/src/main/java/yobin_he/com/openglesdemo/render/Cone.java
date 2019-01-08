package yobin_he.com.openglesdemo.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;
import android.view.ViewDebug;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import yobin_he.com.openglesdemo.utils.ShaderUtils;

/**
 * @author : yobin_he
 * @package: yobin_he.com.openglesdemo.render
 * @fileName: Cone
 * @Date : 2019/1/8  18:32
 * @describe : 圆锥
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class Cone extends Shape{

    private int mProgram;
    private Oval oval;
    private FloatBuffer vertexBuffer;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private int n = 360; //切割份数
    private float height = 2.0f;//圆锥高度
    private float radius = 1.0f; // 圆锥底部半径
    private float[] colors = {1.0f,1.0f,1.0f,1.0f};

    private int vSize;

    public Cone(View view) {
        super(view);
        oval  = new Oval(view);
        ArrayList<Float> pos = new ArrayList<>();
        pos.add(0.0f);
        pos.add(0.0f);
        pos.add(height);
        float angDegSpan = 360f / n;
        for (int i = 0; i < 360 + angDegSpan; i+=angDegSpan) {
            pos.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            pos.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            pos.add(0.0f);
        }
        float[] d = new float[pos.size()];
        for (int i = 0; i < d.length; i++) {
            d[i] = pos.get(i);
        }
        vSize = d.length / 3;

        ByteBuffer buffer = ByteBuffer.allocateDirect(d.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        vertexBuffer = buffer.asFloatBuffer();
        vertexBuffer.put(d);
        vertexBuffer.position(0);

    }

    /**
     * 设置圆锥纵深
     * @param height
     */
    public void setHeight(float height){
        this.height = height;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST); //立体的就应该是加入深度
        mProgram = ShaderUtils.createProgram(mView.getResources(),"vshader/Cone.sh","fshader/Cone.sh");
        oval.onSurfaceCreated(gl,config);//绘制圆形
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //计算宽高比
        float ratio =(float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,20);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 1.0f, -10.0f, -4.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //设置变换矩阵
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        int mMatrix = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        GLES20.glUniformMatrix4fv(mMatrix,1,false,mMVPMatrix,0);
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,0,vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,vSize);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        oval.setMatrix(mMVPMatrix);
        oval.onDrawFrame(gl);

    }
}
