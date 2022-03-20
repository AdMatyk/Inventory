package com.example.inventory


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.Item
import com.example.inventory.data.getFormattedPrice
import com.example.inventory.databinding.ItemListItemBinding

class ItemListAdapter(private val onItemClicked: (Item) -> Unit): ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback) {
    class ItemViewHolder(var binding:  ItemListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item){
            binding.itemName.text = item.itemName
            binding.itemPrice.text = item.getFormattedPrice()
            binding.itemQuantity.text = item.quantityInStock.toString()
        }

    }
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.itemName == newItem.itemName

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemListItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener{
            onItemClicked(current)
        }
        holder.bind(current)

    }


}