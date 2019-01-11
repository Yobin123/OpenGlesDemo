package yobin_he.com.openglesdemo.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author : yobin_he
 * @package: yobin_he.com.openglesdemo.render
 * @fileName: TriangleWithCamera
 * @Date : 2018/12/28  13:01
 * @describe : TODO
 * @org scimall
 * @email he.yibin@scimall.org.cn
 * GlES20.glDrawArrays: GL_POINTS //将传入的顶点坐标作为单独的点绘制
 *                      GL_LINES //将传入的坐标作为单独线条绘制 ，abcdefg六个顶点，绘制ab cd ef 三条线
 *                      GL_LINE_STRIP //将传入的顶点作为折线绘制，ABCD四个顶点，绘制AB ,BC ,CD三条线
 *                      GL_LINE_LOOP // 将传入的顶点作为闭合折线绘制ABCD四个顶点，绘制AB,BC,CD,DA四条线
 *                      GL_TRIANGLES //将传入的顶点作为单独的三角形绘制，ABCDEF绘制ABC,ACD，ADE,AEF三角形
 *                      GL_TRIANGLE_FAN //将传入的顶点作为扇面绘制，ABCDEF绘制ABC ,ACD ,ADE,AEF四个三角形
 *                      GL_TRIANGLE_STRIP //将传入的顶点作为三角形条带绘制，ABCDEF绘制ABC,BCD,CDE,DEF 四个三角形。
 *
 *
 */

public class TriangleWithCamera extends Shape {
    private FloatBuffer vertexBuffer;
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" + //矩阵乘以坐标向量。
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private int mProgram;
    static final int COORD_PER_VERTEX = 3;
    static float triangleCoords[] = {
            0f, 0.5f, 0.0f, //top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f //bottom right
    };

    private int mPositionHandler;
    private int mColorHandler;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    //顶点个数
    private final int vertexCount = triangleCoords.length / COORD_PER_VERTEX;
    //顶点之间的偏移量
    private final int vertexStride = COORD_PER_VERTEX * 4;// 每个顶点四个字节

    private int mMatrixHandler;

    //设置颜色，为rgba
    float color[] = {1.0f,1.0f,1.0f,1.0f};

    public TriangleWithCamera(View view) {
        super(view);

        //分配缓冲buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        //顶点缓冲buffer
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);

        //创建一个空的OpenGLEs程序
        mProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到程序
        GLES20.glAttachShader(mProgram,vertexShader);
        //将片元着色器加入到程序
        GLES20.glAttachShader(mProgram,fragmentShader);
        //链接着色器程序
        GLES20.glLinkProgram(mProgram);



    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //计算宽高比
        float ratio =(float)width / height;

        //设置透视投影
//        Matrix.frustumM (float[] m,//接收透视投影的变换矩阵
//        int mOffset,        //变换矩阵的起始位置（偏移量）
//        float left,         //相对观察点近面的左边距
//        float right,        //相对观察点近面的右边距
//        float bottom,       //相对观察点近面的下边距
//        float top,          //相对观察点近面的上边距
//        float near,         //相对观察点近面距离
//        float far)          //相对观察点远面距离

        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,7);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, // 接收相机变换矩阵
                0, //变换矩阵的起始位置
                0,0, 7.0f, //观测点位置
                0f,0f, 0f, //相机位置
                0f,1f,0f); //up向量在xyz上的分量。

        //计算变换矩阵，实际的变换矩阵。
        Matrix.multiplyMM(mMVPMatrix, //接收相乘结果
                0, //接收矩阵的起始位置（偏移量）
                mProjectMatrix, //左矩阵
                0,  //左矩阵起始位置
                mViewMatrix, //右矩阵
                0);//右矩阵起始位置。
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //将程序加入到OpenGlEs2环境
        GLES20.glUseProgram(mProgram);
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0);

        //获取定点着色器的vPosition成员句柄
        mPositionHandler = GLES20.glGetAttribLocation(mProgram,"vPosition");

        //启用三角形顶点句柄
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandler,COORD_PER_VERTEX,GLES20.GL_FLOAT,false,vertexStride, vertexBuffer);

        //获取片元着色器的VColor成员的句柄
        mColorHandler = GLES20.glGetUniformLocation(mProgram,"vColor");
        //设置绘制三角形颜色
        GLES20.glUniform4fv(mColorHandler,1,color,0);


        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandler);



    }
}
