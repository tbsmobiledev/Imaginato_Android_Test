package com.tbs.imaginato.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tbs.imaginato.R
import com.tbs.imaginato.data.local.UserModel
import kotlinx.android.synthetic.main.row_user_info.view.*

class UserAdapter(private val userList: List<UserModel>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_user_info, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beHeard: UserModel = userList[position]
        holder.bindItems(beHeard)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(user: UserModel) {
            itemView.mTvUserID.text = user.userId
            itemView.mTvUserName.text = user.userName
        }
    }
}