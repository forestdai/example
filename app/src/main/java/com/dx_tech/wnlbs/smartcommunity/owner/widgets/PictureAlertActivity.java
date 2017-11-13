package com.dx_tech.wnlbs.smartcommunity.owner.widgets;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dx_tech.wnlbs.smartcommunity.owner.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
/**
 * Created by 戴圣伟 on 0016 2017/10/16.
 * 全局图片浏览界面
 * 传入本地地址 putExtra("image_path", path)
 */

public class PictureAlertActivity extends Activity {
    private String mPath;
    private ProgressBar mProgressBar;

    private RelativeLayout mRootView;
    private int mPointerNum = 0;

    private Bitmap mBitmap;

    private ImageView touchImageView;
    private float x_down = 0;
    private float y_down = 0;
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float oldRotation = 0;//第二个手指放下时的两点的旋转角度
    private float rotation = 0;//旋转角度差值
    private float newRotation = 0;
    private float Reset_scale = 1;
    private Matrix matrix = new Matrix();
    private Matrix matrix1 = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private GestureDetector gestureDetector;

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private boolean isFangda = false;//双击放大还是缩小
    boolean reset = false;
    private int widthScreen;
    private int heightScreen;
    boolean isCheckTopAndBottom, isCheckRightAndLeft;

    private Drawable mBackgroundDrawable;

    private final int SET_BACKGROUND = 0;
    private final int SHOW_IMAGE = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SET_BACKGROUND:
                    if(Build.VERSION.SDK_INT >= 17){
                        mRootView.setBackground(mBackgroundDrawable);
                        AlphaAnimation aplha = new AlphaAnimation(0.5f, 1.0f);
                        aplha.setDuration(400);
                        aplha.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mHandler.sendEmptyMessage(SHOW_IMAGE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        mRootView.setAnimation(aplha);
                        aplha.startNow();
                    }else{
                        init();
                    }
                    break;
                case SHOW_IMAGE:
                    init();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent it = getIntent();
        if (it != null) {
            mPath = it.getStringExtra("image_path");
        }
        setContentView(R.layout.picture_alert_activity_layout);
        mRootView = (RelativeLayout)findViewById(R.id.root);
        touchImageView = (ImageView) findViewById(R.id.image);
        mProgressBar = (ProgressBar) findViewById(R.id.refresh_progress);
        mProgressBar.setVisibility(View.VISIBLE);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean onTouchEvent(MotionEvent event) {

        /**
         * 在这里调用手势的方法
         * 这是手势和触摸事件同时使用的方法
         */
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        } else {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mPointerNum += 1;
                    reset = false;
                    mode = DRAG;
                    x_down = event.getX(0);
                    y_down = event.getY(0);
                    /**
                     * 单个手指放下，首先保存图片的缩放矩阵到savedMatrix
                     */
                    savedMatrix.set(matrix);
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mPointerNum += 1;
                    if(mPointerNum > 2){
                        return false;
                    }
                    mode = ZOOM;
                    /**
                     * 第二个手指刚放下时
                     * 计算两个手指间的距离
                     */
                    oldDist = spacing(event);
                    /**
                     * 第二个手指刚放下时
                     * 计算两个手指见的旋转角度
                     */
                    oldRotation = rotation(event);
                    savedMatrix.set(matrix);
                    /**
                     * 第二个手指刚放下时
                     * 计算两个手指见的中间点坐标，并存在mid中
                     */
                    midPoint(mid, event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(mPointerNum > 2){
                        return false;
                    }
                    if (mode == ZOOM) {
                        matrix1.set(savedMatrix);
                        /**
                         * 两个手指开始移动
                         * 计算移动后旋转角度
                         */
                        newRotation = rotation(event);
                        /**
                         * 两个角度之差
                         * 即是图片的旋转角度
                         */
                        rotation = newRotation - oldRotation;
                        /**
                         * 计算移动后两点间的中间点
                         */
                        float newDist = spacing(event);
                        /**
                         * 两个中间点的商即时放大倍数
                         */
                        float scale = newDist / oldDist;
                        /**
                         * 放大倍数的倒数即是还原图片原来大小的倍数
                         */
                        Reset_scale = oldDist / newDist;
                        matrix1.postScale(scale, scale, mid.x, mid.y);// 縮放
                        matrix1.postRotate(rotation, mid.x, mid.y);// 旋轉
                        matrix.set(matrix1);

                        RectF f = getMatrixRectF();
                        float w = f.width();
                        float h = f.height();

                        if (w < widthScreen/4) {
                            reset = true;
                        }
                        touchImageView.setImageMatrix(matrix);

                    } else if (mode == DRAG) {
                        matrix1.set(savedMatrix);
                        float tx = event.getX(0) - x_down;
                        float ty = event.getY(0) - y_down;
                        /**
                         * 单个手指移动后的距离大于20 才算作移动
                         */
                        if (Math.sqrt(tx * tx + ty * ty) > 20f) {
                            /**
                             * 设置图片宽高与屏幕宽高的大小的boolean类型的值
                             */
                            isCheckRightAndLeft = isCheckTopAndBottom = true;
                            /**
                             * 得到目前图片的宽高
                             */
//                            RectF rectF = getMatrixRectF();
//                            /**
//                             * 图片宽度小于屏幕大小
//                             * 不移动
//                             */
//                            if (rectF.width()<widthScreen) {
//                                tx = 0;
//                                isCheckRightAndLeft = false;
//                            }
//                            /**
//                             * 图片高度小于屏幕高度
//                             * 不移动
//                             */
//                            if (rectF.height()<heightScreen) {
//                                ty = 0;
//                                isCheckTopAndBottom = false;
//                            }
                            matrix1.postTranslate(tx, ty);// 平移
                            /**
                             * 如果想在拖动图片的同时检测图片边缘是否
                             * 到达屏幕的边缘，则取消下面的注释
                             */
//                            matrix.set(matrix1);
//                            checkDxDyBounds();
                            matrix.set(matrix1);
                            touchImageView.setImageMatrix(matrix);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    if(mPointerNum > 2){
                        mPointerNum -= 1;
                        return false;
                    }
                    mPointerNum -= 1;
                    if (mode == ZOOM) {
                        /**
                         * 双手放开，停止图片的旋转和缩放
                         * Reset_scale还原图片的缩放比例
                         */
                        if (reset) {
                            matrix1.postScale(Reset_scale, Reset_scale, mid.x, mid.y);
                        }
                        reset = false;
                        /**
                         * 双手放开，停止缩放、旋转图片，此时根据已旋转的角度
                         * 计算还原图片的角度，最终的效果是把图片竖直或横平方正。
                         */
                        setRotate();
                        matrix.set(matrix1);
                        /**
                         * 将图片放在屏幕中间位置
                         */
                        center(true, true);
                        touchImageView.setImageMatrix(matrix);
                        matrix1.reset();
                    } else if (mode == DRAG) {
                        /**
                         * 单手拖动图片，放开手指，停止拖动
                         * 此时检测图片是否已经偏离屏幕边缘
                         * 如果偏离屏幕边缘，则图片回弹
                         */
//                        checkDxDyBounds();
//                        matrix.set(matrix1);
                        touchImageView.setImageMatrix(matrix);
                        matrix1.reset();
                    }
                    mode = NONE;
                    break;
            }
            return true;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            mPath = intent.getStringExtra("image_path");
        }
    }

    private void init() {
        if(mPath == null || mPath.length() < 1){
            return;
        }
        File file = new File(mPath);
        /**
         * 使用图片的矩阵类型进行图片的设置，必须设置
         * setScaleType(ScaleType.MATRIX);
         * 网上有说，设置了该类型之后，怎么设置图片在中心位置上？
         * 我的解决方法就是通过代码控制，将图片设置在屏幕中心
         * 具体代码请参看 center(true,true);方法
         */

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //获取屏幕的宽和高
        widthScreen = dm.widthPixels - (int)dm.density*10;
        heightScreen = dm.heightPixels - (int)dm.density*10;
        //初始化图片的矩阵
        if (mBitmap != null) {
            mBitmap.recycle();
        }
//        mBitmap = BitmapFactory.decodeFile(mPath);
        mBitmap = getDegreeImageBitmapWithSize(file, widthScreen, heightScreen, false);
        touchImageView.setImageBitmap(mBitmap);
        touchImageView.setScaleType(ImageView.ScaleType.MATRIX);
        matrix.set(touchImageView.getImageMatrix());

        /**
         * 初始化手势
         * 单击  双击 长按
         * 这三个均是手势起作用
         * 如果想要在这三种手势中进行何种操作，
         * 将代码放在对应的方法中即可。
         */
        gestureDetector = new GestureDetector(
                PictureAlertActivity.this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);
                    }
                });
        gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }

            /**
             * 双击手势的时候 会触发该方法一次
             * 所以双击手势对应的代码应放在这里
             */
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                mid.x = e.getX(0);
                mid.y = e.getY(0);
                matrix1.set(savedMatrix);

                RectF f = getMatrixRectF();
                float w = f.width();
                float h = f.height();

                float scaleW = ((float) widthScreen) / w;
                float scaleY = ((float) heightScreen) / h;

                float scale = Math.min(scaleW, scaleY);

                matrix1.postScale(scale, scale, mid.x, mid.y);
//                if (isFangda) {
//                    matrix1.postScale(0.5f, 0.5f, mid.x, mid.y);//缩小
//                    isFangda = false;
//                }else {
//                    matrix1.postScale(2f, 2f, mid.x, mid.y);// 放大
//                    isFangda = true;
//                }
                matrix.set(matrix1);
                center(true, true);
                touchImageView.setImageMatrix(matrix);
                return true;
            }

            /**
             * 单击手势 会触发该方法一次
             * 单击对应的代码应放在这里
             */
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //点击图片 销毁activity
                PictureAlertActivity.this.finish();
                return false;
            }
        });
        /**
         * 初始化 将图片放在屏幕中心位置
         */
        RectF f = getMatrixRectF();
        float w = f.width();
        float h = f.height();
//        float w = mBitmap.getWidth();
//        float h = mBitmap.getHeight();

        if(w == widthScreen || h == heightScreen){
            /**
             * 设置ImageView的触摸事件
             */
            touchImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    onTouchEvent(event);
                    return true;
                }
            });
            mProgressBar.setVisibility(View.GONE);
        }else{
            float scaleW = ((float) widthScreen) / w;
            float scaleY = ((float) heightScreen) / h;
//        scaleW = ((float) widthScreen) / w;
//        scaleY = ((float) heightScreen) / h;
//        if (w >= h) {
//        } else {
//        }

            float scale = Math.min(scaleW, scaleY);
            matrix.postScale(scale, scale, mid.x, mid.y);
            center(true, true);
            /**
             * 图片设置中心之后，重新设置图片的缩放矩阵
             */
            touchImageView.setImageMatrix(matrix);
            /**
             * 设置ImageView的触摸事件
             */
            touchImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    onTouchEvent(event);
                    return true;
                }
            });
            mProgressBar.setVisibility(View.GONE);
        }


    }

    public static Bitmap getDegreeImageBitmapWithSize(File file, int width, int height, boolean degree){
        Bitmap bitmap = null;
        if(file.exists())
        {
            FileInputStream temp = null;

            try {
                temp = new FileInputStream(file);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(temp, null, options);
                temp.close();
                int i = 0;
                while (true) {
                    if ((options.outWidth >> i <= width)
                            && (options.outHeight >> i <= height)) {
                        temp = new FileInputStream(file);
                        options.inSampleSize = (int) Math.pow(2.0D, i);
                        options.inJustDecodeBounds = false;
                        Bitmap newbit = BitmapFactory.decodeStream(temp, null, options);
                        if(degree){
                            bitmap = rotateBitmap(newbit, readPictureDegree(file.getPath()));
                        }else{
                            bitmap =  newbit;
                        }
                        break;
                    }
                    i += 1;
                }
            } catch (IOException e) {
                if(temp != null){
                    try {
                        temp.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotateBitmap(Bitmap b, int degrees) {
        if (degrees == 0) {
            return b;
        }
        if (b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth(),
                    (float) b.getHeight());
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                        b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle(); // Android开发网再次提示Bitmap操作完应该显示的释放
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // Android123建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。.
                return b;
            }
        }
        return b;
    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     * 这里获取的是当前显示的图片的大小。
     * 图片放大后，获取的就是图片放大后的图片的大小。
     * 图片缩小后，获取的就是图片缩小后的图片的大小。
     * <p/>
     * 这个大小与图片的大小是有区别的。
     * 下面是固定用法，记住即可。
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix m = matrix;
        RectF rect = new RectF();
        Drawable d = touchImageView.getDrawable();
        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            m.mapRect(rect);
        }
        //第二种方法  （两种方法均可）
//        Matrix m = new Matrix();
//        m.set(matrix);
//        RectF rect = new RectF(0, 0, gintama.getWidth(), gintama.getHeight());
//        m.mapRect(rect);
        return rect;
    }

    /**
     * 横向、纵向 图片居中
     */
    protected void center(boolean horizontal, boolean vertical) {
        RectF rect = getMatrixRectF();
        float deltaX = 0, deltaY = 0;
        float height = rect.height();
        float width = rect.width();

        if (vertical) {
            /**
             *  图片小于屏幕大小，则居中显示。
             *  大于屏幕，如果图片上方留空则往上移，
             *  图片下方留空则往下移
             */
            int screenHeight = heightScreen;
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = touchImageView.getHeight() - rect.bottom;
            }
        }

        if (horizontal) {
            int screenWidth = widthScreen;
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right;
            }
        }
        matrix.postTranslate(deltaX, deltaY);

    }

    // 触碰两点间距离
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // 取手势中心点
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);

        point.set(x / 2, y / 2);
    }

    // 取旋转角度
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        /**
         * 反正切函数
         * 计算两个坐标点的正切角度
         */
        double radians = Math.atan2(delta_y, delta_x);
        return (float) (Math.toDegrees(radians));
    }

    /**
     * 手指松开，确定旋转的角度
     */
    private void setRotate() {
        if (rotation <= -315) {
            matrix1.postRotate(-360 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= -270) {
            matrix1.postRotate(-270 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= -225) {
            matrix1.postRotate(-270 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= -180) {
            matrix1.postRotate(-180 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= -135) {
            matrix1.postRotate(-180 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= -90) {
            matrix1.postRotate(-90 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= -45) {
            matrix1.postRotate(-90 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= 0) {
            matrix1.postRotate(0 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= 45) {
            matrix1.postRotate(0 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= 90) {
            matrix1.postRotate(90 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= 135) {
            matrix1.postRotate(90 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= 180) {
            matrix1.postRotate(180 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= 225) {
            matrix1.postRotate(180 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= 270) {
            matrix1.postRotate(270 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= 315) {
            matrix1.postRotate(270 - rotation, mid.x, mid.y);// 旋轉
        } else if (rotation <= 360) {
            matrix1.postRotate(360 - rotation, mid.x, mid.y);// 旋轉
        }
    }

    /**
     * 检测图片偏离屏幕两边的距离
     * 然后平移，是图片边缘在屏幕边，
     * 使图片周围没有空白
     */
    private void checkDxDyBounds() {
        RectF rectF = getMatrixRectF();
        float dx = 0.0f, dy = 0.0f;
        /**
         * 如果图片的左侧大于零，说明图片左侧向右
         * 偏离了左侧屏幕，则左移偏离的距离.
         * rectF.left的值，是基于左侧坐标计算的。
         * 图片正常情况下，该值为0.
         * 当图片向右侧拖动以后，该值大于0.
         * 当图片向左侧拖动以后，该值小于0.
         */
        if (rectF.left > 0 && isCheckRightAndLeft) {
            dx = -rectF.left;
        }
        /**
         * 如果图片的右侧偏离屏幕的右侧，则
         * 图片右移图片的宽度与图片显示的宽度的差.
         *
         * rectF.right的值，是基于左侧计算的，图片没有缩放旋转情况下，
         * 该值==touchImageView.getWidth()图片的宽度。
         * 当拖动图片以后，该值变化，等于显示的图片的宽度
         */
        if (rectF.right < touchImageView.getWidth() && isCheckRightAndLeft) {
            dx = touchImageView.getWidth() - rectF.right;
        }
        /**
         * 当图片顶部大于0，说明图片向下偏离屏幕顶部，
         * 则图片向上回弹偏离的距离。
         *
         * rectF.top的值基于顶部坐标，
         * 图片正常情况下，该值=0.
         */
        if (rectF.top > 0 && isCheckTopAndBottom) {
            dy = -rectF.top;
        }
        /**
         * 当图片底部小于图片高度时，图片偏离屏幕底部
         * 则图片回弹图片的高度与显示的图片的高度之差。
         *
         * rectF.bottom的值，基于顶部坐标。
         * 图片正常情况下，该值=图片的高度。
         */
        if (rectF.bottom < touchImageView.getHeight() && isCheckTopAndBottom) {
            dy = touchImageView.getHeight() - rectF.bottom;
        }
        /**
         * 计算后，设置图片回弹
         */
        matrix1.postTranslate(dx, dy);
    }
}
