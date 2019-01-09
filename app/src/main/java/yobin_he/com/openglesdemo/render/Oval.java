package yobin_he.com.openglesdemo.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author : yobin_he
 * @package: yobin_he.com.openglesdemo.render
 * @fileName: Oval
 * @Date : 2019/1/7  18:34
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class Oval extends Shape {
    private FloatBuffer vertexBuffer;
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;"+
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private int mProgram;
    static final  int COORDS_PER_VERTEX = 3; //每三个为一个坐标

    private int mPositionHandle;
    private int mColorHandle;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    //顶点之间的偏移量
    private final  int vertexStride = 0; //每个顶点四个字节
    private int mMatrixHandler;

    private float radius = 1.0f;

    private int n = 360; // 切割份数
    private float[] shapePos;
    private float height = 0.0f;

    float color[] = {0.0f,1.0f,0.0f,0.0f};


    public Oval(View view) {
        this(view,0.0f);
    }

    public Oval(View view,float height) {
        super(view);
        this.height = height;
        shapePos = createPosition();
        ByteBuffer bb = ByteBuffer.allocateDirect(shapePos.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(shapePos);
        vertexBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);

        //创建一个空的OpengleEs程序
        mProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到程序
        GLES20.glAttachShader(mProgram,vertexShader);
        //将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram,fragmentShader);
        //连接着色程序
        GLES20.glLinkProgram(mProgram);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,7);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix,0,0,0,7.0f,0f,0f,0f,0f,1.0f,0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        //将程序加入到OpenGles2.0环境
        GLES20.glUseProgram(mProgram);
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0);
        //获取顶点着色器的vPosition 的成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");
        //启用三角形顶点句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle,COORDS_PER_VERTEX,GLES20.GL_FLOAT,false,vertexStride,vertexBuffer);

        //获取片元主色器的VColor的成员句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram,"vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle,1,color,0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,shapePos.length / COORDS_PER_VERTEX);
        //禁止顶点数组句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    //改变矩阵
    public void setMatrix(float[] matrix){
        this.mMVPMatrix = matrix;
    }

    /**
     * 创建
     * @return
     */
    private float[] createPosition(){
        ArrayList<Float> data = new ArrayList<>();
        data.add(0.0f); //设置圆心坐标
        data.add(0.0f);
        data.add(height);
        float angDegSpan = 360f/ n;

        for (float i = 0; i < 360 + angDegSpan; i+= angDegSpan) {
            data.add((float) (radius * Math.sin(i * Math.PI / 180f))); //弧度角
            data.add((float) (radius * Math.cos(i * Math.PI / 180f))); //弧度角
            data.add(height);

        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }
}
