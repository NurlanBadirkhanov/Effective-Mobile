package com.effective.courses.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.effective.courses.R
import com.effective.courses.bookmarks.BookmarkChecker
import com.effective.courses.bookmarks.BookmarkToggler
import com.effective.courses.databinding.ItemCourseBinding
import com.effective.courses.model.Course

class HomeAdapters(
    private val isBookmarked: BookmarkChecker,
    private val toggleBookmark: BookmarkToggler,
    private val onClick: (Course) -> Unit = {}
) : ListAdapter<Course, HomeAdapters.Holder>(Diff) {

    object Diff : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(old: Course, new: Course) = old.id == new.id
        override fun areContentsTheSame(old: Course, new: Course) = old == new
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, onClick, isBookmarked, toggleBookmark)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(
        private val binding: ItemCourseBinding,
        private val onClick: (Course) -> Unit,
        private val isBookmarked: BookmarkChecker,
        private val toggleBookmark: BookmarkToggler
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Course) = with(binding) {
            tvTitle.text = item.title
            tvText.text = item.text
            tvPrice.text = "${item.price} ₽"
            tvRating.text = item.rate.toString()
            tvDate.text = item.publishDate

            Glide.with(ivThumb.context)
                .load(R.drawable.cov)
                .centerCrop()
                .into(ivThumb)

            applyBookmarkTint(item.id)

            ivBookmark.setOnClickListener {
                toggleBookmark.toggle(item.id)
                applyBookmarkTint(item.id)
            }

            root.setOnClickListener { onClick(item) }
        }

        private fun applyBookmarkTint(id: String) = with(binding) {
            val ctx = ivBookmark.context
            val green = ContextCompat.getColor(ctx, R.color.em_green_500)
            val white = ContextCompat.getColor(ctx, R.color.em_white)
            ivBookmark.imageTintList = ColorStateList.valueOf(
                if (isBookmarked.isBookmarked(id)) green else white
            )
        }
    }
}
