package net.caffee.places.ui.place.adapters

import android.animation.ValueAnimator
import android.databinding.ObservableBoolean
import android.view.LayoutInflater
import net.caffee.places.databinding.FragmentPlaceBinding
import net.caffee.places.databinding.ItemPlaceReviewBinding
import net.caffee.places.repo.remote.model.AdminComment
import net.caffee.places.repo.remote.model.Comment

class PlaceReviewAdapter(private val binding: FragmentPlaceBinding,
                         private val layoutInflater: LayoutInflater) {

   private val animDuration = 500L

    fun addItemsWithScroll(list: List<Comment>) {
        Thread {
            list.forEach {
                binding.root.post {
                    val itemBinding = ItemPlaceReviewBinding.inflate(layoutInflater)
                    itemBinding?.commentItem = CommentItem(it)
                    binding.llReviews.addView(itemBinding?.root)
                    val animator = ValueAnimator.ofFloat(0f, 1f)
                    animator.duration = animDuration
                    animator.addUpdateListener {
                        itemBinding?.root?.alpha = (it.animatedValue as Float)
                    }
                    animator.start()
                    binding.scroll.smoothScrollTo(0, binding.scroll.maxScrollAmount)
                }
                Thread.sleep(animDuration)
            }
        }.start()
    }

    fun setItems(comments: List<Comment>){
        Thread {
            comments.forEach {
                binding.root.post {
                    val itemBinding = ItemPlaceReviewBinding.inflate(layoutInflater)
                    itemBinding?.commentItem = CommentItem(it)
                    binding.llReviews.addView(itemBinding?.root)
                }
            }
        }.start()
    }
}

class CommentItem(val comment: Comment) {
    val isOpened = ObservableBoolean(comment.childs.isNotEmpty())
    var adminComment : AdminComment?
            = if(comment.childs.isNotEmpty()) comment.childs.first() else null
}
