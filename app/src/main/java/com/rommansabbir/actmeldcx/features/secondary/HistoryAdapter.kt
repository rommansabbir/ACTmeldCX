package com.rommansabbir.actmeldcx.features.secondary

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rommansabbir.actmeldcx.base.History
import com.rommansabbir.actmeldcx.databinding.ContentItemHistoryBinding
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class HistoryAdapter @Inject constructor(@ActivityContext private val context: Context) :
    RecyclerView.Adapter<HistoryViewHolder>() {
    fun getContext() = context

    private val diffUtilsCallBack =
        object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(
                oldItem: History,
                newItem: History
            ): Boolean {
                return oldItem.dateTime == newItem.dateTime
            }

            override fun areContentsTheSame(
                oldItem: History,
                newItem: History
            ): Boolean {
                return oldItem.dateTime == newItem.dateTime
            }
        }
    private var differ: AsyncListDiffer<History> =
        AsyncListDiffer(this, diffUtilsCallBack)

    @SuppressLint("NotifyDataSetChanged")
    fun clearDataSet() {
        differ.submitList(null)
        this.notifyDataSetChanged()
    }

    fun submitDataSet(list: MutableList<History>) {
        differ.submitList(list)
    }

    fun getCurrentDataSet(): MutableList<History> =
        ArrayList(differ.currentList.toMutableList()).toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder =
        HistoryViewHolder(
            ContentItemHistoryBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            ),
            this
        )

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bindView(position)
    }

    override fun getItemCount(): Int = differ.currentList.size

    var deleteCallback: (itemToDelete: History) -> Unit = {}
    var browseCallback: (itemToDelete: History) -> Unit = {}
}

class HistoryViewHolder(
    private val binding: ContentItemHistoryBinding,
    private val adapter: HistoryAdapter
) : RecyclerView.ViewHolder(binding.root) {
    fun bindView(position: Int) {
        val model = adapter.getCurrentDataSet()[position]
        binding.model = model
        binding.cihBtnDelete.setOnClickListener { adapter.deleteCallback.invoke(model) }
        binding.cihIvWeb.setOnClickListener { adapter.browseCallback.invoke(model) }
        binding.cihTvUrl.setOnClickListener { adapter.browseCallback.invoke(model) }
        binding.executePendingBindings()
    }
}