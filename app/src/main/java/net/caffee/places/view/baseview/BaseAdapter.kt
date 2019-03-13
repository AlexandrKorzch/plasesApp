package net.caffee.places.view.baseview

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class BaseAdapter<B : ViewDataBinding,  I : Any>
(private var list: MutableList<I>?)
    : RecyclerView.Adapter<Holder<B>>() {

    abstract fun getLayoutId(): Int

    override fun getItemCount() = list?.size ?: 0

    abstract fun bindItem(binding: B, item: I)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder<B> {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = DataBindingUtil.inflate<B>(
                inflater, getLayoutId(), parent, false)
        return Holder<B>(itemBinding)
    }

    override fun onBindViewHolder(holder: Holder<B>, position: Int) {
        list.let {
            bindItem(holder.binding, list!![position])
            holder.binding.executePendingBindings()
        }
    }

    fun setItems(items: List<Any>) {
        val newList = ArrayList<I>()
        items.forEach({newList.add(it as I)})
        list = newList
        notifyDataSetChanged()
    }
}

class Holder<B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)