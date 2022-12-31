package com.ezatpanah.githubclient.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ezatpanah.githubclient.R
import com.ezatpanah.githubclient.databinding.ItemRepositoryBinding
import com.ezatpanah.githubclient.response.RepositoryResponse

class RepositoryAdapter : PagingDataAdapter<RepositoryResponse, RepositoryAdapter.ViewHolder>(differCallback) {

    lateinit var binding: ItemRepositoryBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRepositoryBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.setIsRecyclable(false)
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root), View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        @SuppressLint("SetTextI18n")
        fun bind(item: RepositoryResponse) {
            binding.apply {
                tvNameRepo.text = item.name
                tvLang.text = item.language
                tvStar.text = item.stargazers_count.toString()
                tvFork.text = item.forks_count.toString()
                tvUpdateDate.text = "Updated on : " + item.updated_at.trim().substringBefore("T")
                when (item.isPrivate) {
                    false -> imgVisibility.load(R.drawable.ic_twotone_lock_open_24)
                    true -> {
                        imgVisibility.load(R.drawable.ic_twotone_lock_24)
                        imgMore.visibility = View.GONE
                    }
                }
                imgMore.setOnClickListener(this@ViewHolder)
            }
        }

        override fun onClick(p0: View?) {
            showPopMenu(p0!!)
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            return when (item!!.itemId) {
                R.id.menuShare -> {
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "text/plain"
                    val shareBody = getItem(position)!!.html_url
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Your Repository")
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                    context.startActivity(Intent.createChooser(sharingIntent, "Share via"))
                    true
                }
                R.id.menuOpen -> {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getItem(position)!!.html_url))
                    context.startActivity(browserIntent)
                    true
                }
                else -> false
            }
        }

        private fun showPopMenu(view: View) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.menu_popup)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.show()
        }

    }

}

val differCallback = object : DiffUtil.ItemCallback<RepositoryResponse>() {
    override fun areItemsTheSame(oldItem: RepositoryResponse, newItem: RepositoryResponse): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: RepositoryResponse, newItem: RepositoryResponse): Boolean = oldItem == newItem
}