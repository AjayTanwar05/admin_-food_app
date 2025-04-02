package com.ajaytanwar.adminfoodapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajaytanwar.adminfoodapp.databinding.OrderDetailItemsBinding
import com.bumptech.glide.Glide

class OrderDetailsAdapter(private val context: Context,
                          private var foodName :ArrayList<String>,
                          private var foodImage :ArrayList<String>,
                          private var foodQuantity :ArrayList<Int>,
                          private var foodPrice :ArrayList<String>
) :RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding = OrderDetailItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int =foodName.size

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
       holder.bind(position)
    }
  inner  class OrderDetailsViewHolder (private val binding: OrderDetailItemsBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {

                orderFoodName.text =foodName[position]
                orderFoodquantitys.text =foodQuantity[position].toString()

                val uriString = foodImage[position]
                val  uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(fooditemimage)
                orderFoodprice.text=foodPrice[position]
            }

        }

    }

}
