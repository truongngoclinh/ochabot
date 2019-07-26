package vn.ochabot.seaconnect.core

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View

class GridDividerItemDecoration(var span: Int, var dividerWitdh: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        var itemPosition = parent.getChildLayoutPosition(view)
        var col = itemPosition % span
        outRect.top = if (itemPosition < span) 0 else dividerWitdh;
        outRect.bottom = 0;
        outRect.right = if (col < span - 1) dividerWitdh / 2 else 0
        outRect.left = if (col > 0) dividerWitdh / 2 else 0
    }
}