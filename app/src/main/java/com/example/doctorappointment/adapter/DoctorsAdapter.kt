package com.example.doctorappointment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.doctorappointment.databinding.ViewholderDoctorBinding
import com.example.doctorappointment.domen.DoctorsModel

class DoctorsAdapter(val onDoctorClicked: (DoctorsModel) -> Unit) :
    RecyclerView.Adapter<DoctorsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderDoctorBinding) :
        RecyclerView.ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<DoctorsModel>() {
        override fun areItemsTheSame(oldItem: DoctorsModel, newItem: DoctorsModel): Boolean {
            return oldItem.Id == newItem.Id
        }

        override fun areContentsTheSame(oldItem: DoctorsModel, newItem: DoctorsModel): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorsAdapter.ViewHolder {
        val binding =
            ViewholderDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: DoctorsAdapter.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding.apply {
            Glide.with(holder.itemView).load(item.Picture).apply {
                RequestOptions().transform(CenterCrop())
            }.into(ivDoctor)
            tvRating.text = item.Rating.toString()
            tvName.text = item.Name
            tvSpecial.text = item.Special
            ratingBar.rating = item.Rating!!.toFloat()
            tvRating.text = item.Rating.toString()
            MakeAppointmentBtn.setOnClickListener {
                onDoctorClicked(item)
            }
            if (item.TopDoctor == true) {
                tvDegree.text = "Professional"
            } else {
                tvDegree.text = "Fresh"
            }
        }
    }
}