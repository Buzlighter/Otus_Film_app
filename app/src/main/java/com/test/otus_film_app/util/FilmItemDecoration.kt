package com.test.otus_film_app.util

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.test.otus_film_app.view.film_list_screen.FilmAdapter
import androidx.annotation.NonNull

class FilmItemDecoration: RecyclerView.ItemDecoration() {

    private var dividerPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        dividerPaint.color = Color.BLACK
        dividerPaint.style = Paint.Style.FILL
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount - 1) {
            val childView = parent.getChildAt(i)
            val bottomItemHeight = parent.layoutManager?.getBottomDecorationHeight(childView) ?: 0
            c.drawRect(Rect(childView.left, childView.bottom, childView.right,
                    childView.bottom + bottomItemHeight), dividerPaint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(0, 0, 0, 50)
    }


}