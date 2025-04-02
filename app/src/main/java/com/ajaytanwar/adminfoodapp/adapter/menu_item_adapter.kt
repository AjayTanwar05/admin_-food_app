package com.ajaytanwar.adminfoodapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajaytanwar.adminfoodapp.databinding.ItemMenuBinding
import com.ajaytanwar.adminfoodapp.modlle.all_menu
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference

class menu_item_adapter(
    private val context: Context,
    private val menuList: ArrayList<all_menu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener :(position : Int) ->Unit
): RecyclerView.Adapter<menu_item_adapter.AddItemViewHolder>() {

    private val itemQuantities = IntArray(menuList.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }


    override fun getItemCount(): Int = menuList.size
    inner class AddItemViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                fooditemname.text = menuItem.foodname
                foodPrice.text = menuItem.foodprice
                Glide.with(context).load(uri).into(fooditemImage)
                orderCount.text = quantity.toString()


                minusButton.setOnClickListener {
                    deceaseQunatitiy(position)
                }

                plusButton.setOnClickListener {
                    increseQunatities(position)
                }

                transhButton.setOnClickListener {
                  onDeleteClickListener(position)
                }

            }

        }


        private fun increseQunatities(position: Int) {
            if (itemQuantities[position]<10) {
                itemQuantities[position]++
                binding.orderCount.text = itemQuantities[position].toString()
            }
        }

        private fun deceaseQunatitiy(position: Int) {
            if (itemQuantities[position]>1){
                itemQuantities[position]--
                binding.orderCount.text= itemQuantities[position].toString()
            }


        }

        private fun deleteitem(position: Int) {
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,menuList.size)

        }

    }
}




