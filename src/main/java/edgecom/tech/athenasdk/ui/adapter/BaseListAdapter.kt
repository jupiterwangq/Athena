package edgecom.tech.athenasdk.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer


class BaseListAdapter(val adapter: IAdapterProxy?): RecyclerView.Adapter<BaseListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return adapter?.onCreateViewHolder(parent, viewType) ?: Holder(View(parent.context))
    }

    override fun getItemCount() = adapter?.getItemCount() ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        adapter?.onBindViewHolder(holder, position)
    }

    open class Holder(override val containerView: View?): RecyclerView.ViewHolder(containerView), LayoutContainer
}