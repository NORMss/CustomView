package com.norm.mycustomview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class TestView(
    context: Context,
    attributeSet: AttributeSet,
) : View(context, attributeSet) {
    var listener: Listener? = null
    private val paintCWidth = 100f
    private val paintText = Paint()
    private val paint = Paint()
    private val paintBm = Paint()
    private val paintC = Paint()
    private val startAngle = 0f

    private var bm: Bitmap

    private var mainColor1 = Color.BLUE
    private var mainColor2 = Color.BLUE

    private val colors = listOf(
        Color.RED,
        Color.GREEN,
        Color.BLUE,
        Color.YELLOW,
        Color.BLACK,
        Color.MAGENTA,
        Color.CYAN,
        Color.WHITE
    )
    private val sweepAngle = 360f / colors.size
    private var buttonClicked = 0

    init {
        val drawable = ContextCompat.getDrawable(context, R.drawable.baseline_settings_24)
        bm = Bitmap.createBitmap(
            128,
            128,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bm)
        drawable?.setBounds(0, 0, 128, 128)
        drawable?.draw(canvas)

        mainColor1 = ContextCompat.getColor(context, R.color.main_menu_color_1)
        mainColor2 = ContextCompat.getColor(context, R.color.main_menu_color_2)

        paintText.style = Paint.Style.FILL
        paintText.color = Color.WHITE
        paintText.textSize = 36f
        paint.style = Paint.Style.STROKE
        paint.color = Color.GRAY
        paint.strokeWidth = 5f
        paintC.color = Color.RED
        paintC.strokeWidth = paintCWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCircleButton(canvas)
    }

    private fun drawCircleButton(canvas: Canvas) {
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width.coerceAtMost(height) / 2f) - paintCWidth / 2
        paintC.style = Paint.Style.STROKE
        for (i in colors.indices) {
            paintC.color = if (i == buttonClicked)
                Color.BLACK
            else mainColor1
            canvas.drawArc(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius,
                startAngle + i * sweepAngle + 1,
                sweepAngle - 2,
                false,
                paintC
            )
        }
        paintC.style = Paint.Style.FILL
        //Setting the color of the central circle by pressing the button
        paintC.color = mainColor2
        canvas.drawCircle(
            centerX,
            centerY,
            radius / 1.5f,
            paintC
        )
        drawMenuText(canvas)

        canvas.drawBitmap(
            bm,
            centerX - bm.width / 2f,
            centerY - bm.height / 2f,
            paintBm
        )
    }

    private fun drawMenuText(canvas: Canvas) {
        TextUtils.menuList.forEachIndexed { index, text ->
            val rect = Rect()
            paintText.getTextBounds(text, 0, text.length, rect)
            val angle =
                ((360f / TextUtils.menuList.size) * index) + ((360f / TextUtils.menuList.size) / 2f)
            val coordinate = getXY(angle)
            if (index in 0..3)
                canvas.rotate(270f + angle, coordinate.first, coordinate.second)
            else
                canvas.rotate(90f + angle, coordinate.first, coordinate.second)
            canvas.drawText(
                text,
                coordinate.first - rect.exactCenterX(),
                coordinate.second - rect.exactCenterY(),
                paintText
            )
            if (index in 0..3)
                canvas.rotate(-270f - angle, coordinate.first, coordinate.second)
            else
                canvas.rotate(-90f - angle, coordinate.first, coordinate.second)
            Log.d("MyLog", "Index: $index, Text: $text, Angle: $angle")
        }
    }

    private fun getXY(
        angle: Float
    ): Pair<Float, Float> {
        val centerX = (width / 2f)
        val centerY = (height / 2f)
        val radius = (width / 2f) - (paintCWidth / 2f)

        val x = centerX + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
        val y = centerY + radius * sin(Math.toRadians(angle.toDouble())).toFloat()
        return Pair(x, y)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val centerX = width / 2f
        val centerY = height / 2f
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val angle = (Math.toDegrees(
                    atan2(
                        y - centerY,
                        x - centerX
                    ).toDouble()
                ) + 360) % 360
                buttonClicked = (angle / (360 / colors.size)).toInt()
                listener?.onClick(buttonClicked)
                Log.d("MyLog", "Angle: $angle")
                invalidate()
            }
        }
        return true
    }

    interface Listener {
        fun onClick(index: Int)
    }
}