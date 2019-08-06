package edgecom.tech.athenasdk.ui.adapter

import android.view.ViewGroup

interface IAdapterProxy {

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListAdapter.Holder

    fun onBindViewHolder(holder: BaseListAdapter.Holder, position: Int)

    fun getItemCount(): Int
}