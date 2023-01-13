package com.laputa.zeej.logo

import android.content.Context
import android.graphics.*
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class LogoAnimationHelper(
    context: Context,
    private val coroutineScope: CoroutineScope,
    logoResId: Int,
    private val count: Int = DEFAULT_COUNT,
    private val pixel: Pair<Int, Int> = DEFAULT_PIXEL,
) {
    private val context: Context = context.applicationContext
    private val _logoBitmap = BitmapFactory.decodeResource(context.resources, logoResId)
    private var _index = 0
    private val _rect = Rect()
    private val _matrix = Matrix()
    private var _job: Job? = null
    private val _paint = Paint().apply {
        setColor(Color.WHITE)
        this.strokeWidth = 1f
        this.textSize = 120f
        this.isDither = true
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
    }
    private val _logoPaint = Paint().apply {
        this.strokeWidth = 10f
//        this.isDither = true
//        style = Paint.Style.FILL
//        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
//        setShadowLayer(60f, 0f, 10f, Color.parseColor("#ff9988"))//阴影
//        setMaskFilter( BlurMaskFilter(10f, BlurMaskFilter.Blur.OUTER))
    }

    private val _logoLastPaint = Paint().apply {
        this.strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    // 当前帧画的logo
    private var _bitmap: Bitmap? = null

    // 当前logo的x
    private var _tx: Int = -1

    // 当前logo的y
    private var _ty: Int = -1

    // 左右移动
    private var _moveLeft = Random.nextBoolean()

    // 上下移动
    private var _moveTop = Random.nextBoolean()

    // 拖影缓存
    private val _deque0: java.util.ArrayDeque<Pair<Int, Int>> = java.util.ArrayDeque()
    private val _deque1: java.util.ArrayDeque<Pair<Int, Int>> = java.util.ArrayDeque()
    private val _deque2: java.util.ArrayDeque<Pair<Int, Int>> = java.util.ArrayDeque()
    private val _deque3: java.util.ArrayDeque<Pair<Int, Int>> = java.util.ArrayDeque()
    private val _deque4: java.util.ArrayDeque<Pair<Int, Int>> = java.util.ArrayDeque()

    fun start(block: (Bitmap) -> Unit) {
        _job = coroutineScope.launch {
            while (isActive && _index < count * INDEX_STEP) {
                _index++
                _bitmap?.recycle()
                val bitmap = withContext(Dispatchers.IO) {
                    drawAndCreateBitmap("$_index")
                }
                _bitmap = bitmap
                block.invoke(bitmap)
                delay(50)
            }
            _index = 0
            _bitmap?.recycle()
            _bitmap = null
        }
    }

    fun produce(dirName: String = "file4") {
        _job = coroutineScope.launch {
            Log.d(TAG, "开始")
            while (isActive && _index <= count * INDEX_STEP) {
                _index += INDEX_STEP
                _bitmap?.recycle()
                withContext(Dispatchers.IO) {
                    val bitmap = drawAndCreateBitmap("$_index")
                    //save2local
                    val name = generateName(_index)
                    val file = generateFile(context, dirName, name)
                    Log.d(TAG, file.absolutePath)
                    saveBitmap(bitmap, file)
                    _bitmap = bitmap
                    delay(RUN_INTERNAL_MS)
                }
            }
            Log.d(TAG, "结束")
            _index = 0
            _bitmap?.recycle()
            _bitmap = null
        }
    }

    private fun drawAndCreateBitmap(text: String): Bitmap {
        // init
        val w = pixel.first
        val h = pixel.second
        val logoHeight = _logoBitmap.height
        val logoWidth = _logoBitmap.width
        val scale = 1.0f * logoHeight / pixel.second

        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        // 背景
        canvas.drawColor(Color.BLACK)
        val lastLogo = 10 // 最后几帧数据
        if (_index >= count - lastLogo) {
            _matrix.reset()
            val newScale = scale + (lastLogo - (count - _index)) / 50f
            val logoScaleHeight = logoHeight * newScale
            val logoScaleWidth = logoWidth * newScale
            _matrix.postScale(newScale, newScale)
            val logoTX = (w - logoScaleWidth) / 2f
            val logoTY = (h - logoScaleHeight) / 2f
            Log.d(TAG, "over! $logoTX $logoTY")
            _matrix.postTranslate(logoTX, logoTY)
            _logoPaint.alpha = 255
            canvas.drawBitmap(_logoBitmap, _matrix, _logoPaint)
            return bitmap
        }


        // 文字
        _paint.color = Color.BLUE
        _paint.alpha = 255
        _paint.getTextBounds(text, 0, text.length, _rect)
        canvas.drawText(text, (w - _rect.width()) / 2f, (h + _rect.height()) / 2f, _paint)
        // 横线
        _paint.color = Color.YELLOW
        _paint.alpha = 150
        for (row in 0 until h / UNIT_Y) {
            if (row % 4 == 0) {
                val startX = 0f
                val startY = row * UNIT_Y * 1.0f
                val endX = w * 1.0f
                val endY = row * UNIT_Y * 1.0f
                canvas.drawLine(startX, startY, endX, endY, _paint)
            }
        }
        // 竖线
        for (col in 0 until w / UNIT_X) {
            if (col % 4 == 0) {
                val startX = col * UNIT_X * 1.0f
                val startY = 0f
                val endX = col * UNIT_X * 1.0f
                val endY = h * 1.0f
                canvas.drawLine(startX, startY, endX, endY, _paint)
            }
        }

        // logo
        _matrix.reset()
        _matrix.postScale(scale, scale)
        val logoScaleHeight = logoHeight * scale
        val logoScaleWidth = logoWidth * scale
        val maxW = w - logoScaleWidth.toInt()
        val maxH = h - logoScaleHeight.toInt()

        if (_tx < 0 || _ty < 0) {
            _tx = Random.nextInt(0, maxW)
            _ty = Random.nextInt(0, maxH)
        } else {
//            if (index%10==0){
//                _moveLeft = Random.nextBoolean()
//                _moveTop = Random.nextBoolean()
//            }
//            val leftStep = Random.nextInt(MAX_STEP)
            val leftStep = MAX_STEP
//            val rightStep = Random.nextInt(MAX_STEP)
            val rightStep = MAX_STEP
            if (_moveLeft) {
                _tx -= leftStep
            } else {
                _tx += leftStep
            }
            if (_moveTop) {
                _ty -= rightStep
            } else {
                _ty += rightStep
            }
        }

        if (_tx <= 0) {
            _tx = 0
            _moveLeft = false
        } else if (_tx >= maxW) {
            _tx = maxW
            _moveLeft = true
        }
        if (_ty <= 0) {
            _ty = 0
            _moveTop = false
        } else if (_ty > maxH) {
            _ty = maxH
            _moveTop = true
        }
        _matrix.postTranslate(_tx.toFloat(), _ty.toFloat())
        _logoPaint.alpha = 255
        canvas.drawBitmap(_logoBitmap, _matrix, _logoPaint)
        _matrix.postTranslate(5f, 5f)
        _logoPaint.alpha = 100
        canvas.drawBitmap(_logoBitmap, _matrix, _logoPaint)
        // 拖影
        _deque0.addFirst(_tx to _ty)
        _deque1.addFirst(_tx to _ty)
        _deque2.addFirst(_tx to _ty)
        _deque3.addFirst(_tx to _ty)
        _deque4.addFirst(_tx to _ty)

        canvas.tail(_matrix, _deque0, 2, scale)
        canvas.tail(_matrix, _deque1, 4, scale)
        canvas.tail(_matrix, _deque2, 6, scale)
        canvas.tail(_matrix, _deque3, 8, scale)
        canvas.tail(_matrix, _deque4, 10, scale)

        return bitmap
    }

    private fun Canvas.tail(
        curMatrix: Matrix,
        deque: java.util.ArrayDeque<Pair<Int, Int>>,
        max: Int,
        scale: Float,
    ) {
        curMatrix.reset()
        if (deque.size >= max) {
            val takeLast = deque.pollLast()
            if (takeLast != null) {
                curMatrix.postScale(scale, scale)
                val tx = takeLast.first.toFloat()
                val ty = takeLast.second.toFloat()
                _logoLastPaint.alpha = 255 / max + 20
                curMatrix.postTranslate(tx, ty)
                this.drawBitmap(_logoBitmap, curMatrix, _logoLastPaint)
            }
        }
    }

    fun cancel() {
        _job?.cancel()
        _deque0.clear()
        _deque1.clear()
        _deque2.clear()
        _deque3.clear()
        _deque4.clear()
        _logoBitmap?.recycle()
        _bitmap?.recycle()
    }

    companion object {
        private val DEFAULT_PIXEL = 1024 to 600
        private const val DEFAULT_COUNT = 200
        private const val UNIT_Y = 10
        private const val UNIT_X = 8
        private const val MAX_STEP = 8 //*2 // 每次移动的像素
        private const val RUN_INTERNAL_MS = 100L // 间隔时间
        private const val INDEX_STEP = 1
        private const val TAG = "logo"

        private fun generateFile(context: Context, fileDir: String, fileName: String): File {
            val file: File =
                // File(context.cacheDir.absolutePath + File.separator + fileDir + File.separator + fileName)
                File(Environment.getExternalStorageDirectory().absolutePath + File.separator + fileDir + File.separator + fileName)

            if (file.parentFile?.exists() == false) {
                file.parentFile?.mkdir()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            return file
        }

        private fun generateName(index: Int): String {
            return String.format("%05d.jpg", index)
        }

        private fun saveBitmap(bitmap: Bitmap, file: File) {
            try {
                val fileOutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
                Log.d(TAG, "save success")
            } catch (e: Throwable) {
                e.printStackTrace()
            } finally {
            }
        }
    }
}