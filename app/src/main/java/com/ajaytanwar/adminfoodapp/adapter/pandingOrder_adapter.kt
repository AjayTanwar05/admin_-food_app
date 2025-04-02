package com.ajaytanwar.adminfoodapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ajaytanwar.adminfoodapp.databinding.PandingItemBinding
import com.bumptech.glide.Glide

class pandingOrder_adapter(
                    private val context : Context,
                        private val customerName:MutableList<String>,
                        private val Qunatity: MutableList<String>,
                        private val foodimage: MutableList<String>,
                        private val itemClicked: OnItemClicked,
): RecyclerView.Adapter<pandingOrder_adapter.PandingViewHolder>() {

   interface OnItemClicked{
    fun onItemClickListener(position: Int)
    fun onItemAcptedClickListener(position: Int)
    fun onItemDispatchClickListener(position: Int)

}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PandingViewHolder {
        val binding = PandingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PandingViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PandingViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int = customerName.size


    inner class PandingViewHolder(private val binding: PandingItemBinding):RecyclerView.ViewHolder(binding.root){
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                Name.text= customerName[position]
                quantity.text = Qunatity[position]
                val uriString  = foodimage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(fooditemimage)


                orderedbutton.apply {
                    if(!isAccepted){
                        text = "Accept"
                    } else{
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        if(!isAccepted) {
                            text = "Dispatch"
                            isAccepted = true
                            showToast("order is accepted")
                            itemClicked.onItemAcptedClickListener(position)

                        } else{
                            customerName.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("order Id dispatched")
                            itemClicked.onItemDispatchClickListener(position)

                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickListener(position)
                }
            }

        }
       private fun showToast(message: String){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }


    }
}