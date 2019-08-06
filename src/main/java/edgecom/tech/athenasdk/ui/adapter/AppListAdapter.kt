package edgecom.tech.athenasdk.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edgecom.tech.athena.model.App
import edgecom.tech.athena.repo.AdminRepo
import edgecom.tech.athenasdk.R
import edgecom.tech.athenasdk.ui.activities.ApplicationActivity
import edgecom.tech.uilib.widget.ActionSheet

import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.common_list_item.view.*

import org.jetbrains.anko.backgroundColorResource

/**
 * 应用程序列表页
 */
class AppListAdapter: RecyclerView.Adapter<AppListAdapter.Holder>() {

    private var apps: MutableList<App> = mutableListOf()

    fun setApps(list: List<App>?) {
        list?.let {
            apps.clear()
            apps.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun appendApp(app: App) {
        apps.add(0, app)
        notifyItemInserted(0)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val app = apps[position]
        holder.containerView?.title?.text        = app.name
        holder.containerView?.second_title?.text = "APPID: ${app.appid}"
        holder.containerView?.third_title?.text  = "SECRET: ${app.secret}"
        holder.containerView?.backgroundColorResource = if (position % 2 == 0) android.R.color.white else R.color.common_gray
        holder.containerView?.setOnClickListener {
            ApplicationActivity.start(holder.containerView.context, app.appid, app.secret)
        }
        holder.containerView?.setOnLongClickListener {
            ActionSheet(it.context, listOf("删除应用")) {
                if (it == 0) {
                    removeApp(position)
                }
            }.show()
            true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(inflater.inflate(R.layout.common_list_item, parent, false))
    }

    override fun getItemCount() = apps.size

    private fun removeApp(pos: Int) {
        val app = apps[pos]
        AdminRepo.deleteApplication(app.appid) {
            if (it?.data?.hasError == false) {
                apps.removeAt(pos)
                notifyDataSetChanged()
            }
        }
    }

    class Holder(override val containerView: View?): RecyclerView.ViewHolder(containerView), LayoutContainer
}