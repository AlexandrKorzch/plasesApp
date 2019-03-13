package net.caffee.places.view.baseview

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup


abstract class BaseTwoHolderAdapter<F : ViewDataBinding, S : ViewDataBinding, I : Any>
(private var list: MutableList<I>?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract fun getType(obj: I): Int
    abstract fun getFirstLayoutId(): Int
    abstract fun getSecondLayoutId(): Int
    abstract fun bindFirstTypeItem(binding: F, item: I)
    abstract fun bindSecondTypeItem(binding: S, item: I)

    override fun getItemCount() = list?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return getType(list!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> FirstHolder(DataBindingUtil.inflate(inflater, getFirstLayoutId(), parent, false))
            else -> SecondHolder(DataBindingUtil.inflate(inflater, getSecondLayoutId(), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        list.let {
            when (holder) {
                is FirstHolder -> {
                    bindFirstTypeItem(holder.binding as F, list!![position] as I)
                    holder.binding.executePendingBindings()
                }
                is SecondHolder -> {
                    bindSecondTypeItem(holder.binding as S, list!![position] as I)
                    holder.binding.executePendingBindings()
                }
            }
        }
    }

    fun setItems(items: List<Any>) {
        val newList = ArrayList<I>()
        items.forEach({ newList.add(it as I) })
        list = newList
        notifyDataSetChanged()
    }
}

class FirstHolder(val binding: ViewDataBinding?) : RecyclerView.ViewHolder(binding?.root)
class SecondHolder(val binding: ViewDataBinding?) : RecyclerView.ViewHolder(binding?.root)

interface TypeHolder{
    var type: Int
}
