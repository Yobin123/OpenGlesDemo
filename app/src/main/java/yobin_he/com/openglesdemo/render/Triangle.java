package yobin_he.com.openglesdemo.render;

import android.opengl.GLES20;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author : yobin_he
 * @package: yobin_he.com.openglesdemo.render
 * @fileName: Triangle
 * @Date : 2018/12/28  10:08
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 * <p>
 * 1.加载顶点和片元着色器
 * 2.确定需要绘制图形坐标和颜色数据
 * 3.创建program对象，连接顶点和片元着色器，链接program对象
 * 4设置视图窗口（viewPort）
 * 5.将坐标数据颜色数据传入OpenGl Es 程序
 * 6.使颜色缓冲区的内容显示在屏幕上。
 */

public class Triangle extends Shape {
    private FloatBuffer vertexBuffer;


    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    private int mProgram;

    static final int COORD_PER_VERTEX = 3;
    static float triangleCoord[] = {
            0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f,// bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };


    private int mPositionHandle; //坐标句柄
    private int mColorHandle; // 颜色句柄
    private float[] mViewMatrix = new float[16];

    //顶点个数
    private final int vertexCount = triangleCoord.length / COORD_PER_VERTEX;
    //顶点之间的偏移量
    private final int vertexStride = vertexCount * 4; //每个顶点四个字节

    private int mMatrixHandler;

    //设置颜色，rgba
    float color[] = {
            1.0f, 1.0f, 1.0f, 1.0f
    };

    public Triangle(View view) {
        super(view);

        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoord.length * 4);//分配字节
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoord);
        vertexBuffer.position(0);

        //设置着色器代码，即GLE语言
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        //创建一个空的OpenGl Es程序
        mProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到程序中
        GLES20.glAttachShader(mProgram, vertexShader);
        //将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram, fragmentShader);
        //链接着色器程序
        GLES20.glLinkProgram(mProgram);


    }


    //继承render重写里面的方法
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //将程序加入到OpenGlEs2.0环境
        GLES20.glUseProgram(mProgram);

        //获取顶点着色器的vPosition 成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形定点句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORD_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        //获取片元着色器颜色句柄
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");
        //绘制三角形颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);


    }
}
