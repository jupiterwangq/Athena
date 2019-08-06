package edgecom.tech.athenasdk.ui.fragments

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import edgecom.tech.athena.Athena
import edgecom.tech.athena.model.Event
import edgecom.tech.athena.model.ReportData
import edgecom.tech.athena.model.addExtra
import edgecom.tech.athena.repo.AdminRepo
import edgecom.tech.athenasdk.R
import edgecom.tech.athenasdk.ui.activities.NewEventActivity
import edgecom.tech.athenasdk.ui.adapter.BaseListAdapter
import edgecom.tech.athenasdk.ui.adapter.BaseListFragment
import edgecom.tech.athenasdk.ui.adapter.IAdapterProxy
import edgecom.tech.uilib.widget.ActionSheet

import kotlinx.android.synthetic.main.common_list_item.view.*

import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.textResource
import org.jetbrains.anko.toast

/**
 * 应用程序的事件列表
 */
class EventsFragment: BaseListFragment() {

    private var events: MutableList<Event> = mutableListOf()

    private val RequestNewEvent = 2000

    override fun initData() {
        initAthena()
        getData()
    }

    override fun add() {
        activity?.let {
            NewEventActivity.start(it, it.intent.getStringExtra("appid"), RequestNewEvent)
        }
    }

    override fun onRefresh() {
        refreshData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RequestNewEvent && resultCode == Activity.RESULT_OK) {
            val ev = Event()
            ev.eid = data?.getStringExtra("eid")   ?: ""
            ev.name = data?.getStringExtra("name") ?: ""
            ev.appid = activity?.intent?.getStringExtra("appid") ?: ""
            events.add(0, ev)
            notifyDatasetChanged()
        }
    }

    override fun getAdapter(): IAdapterProxy? {
        return object: IAdapterProxy {

            override fun onBindViewHolder(holder: BaseListAdapter.Holder, position: Int) {
                holder.containerView?.title?.text        = events[position].name
                holder.containerView?.second_title?.text = "ID: ${events[position].eid}"
                holder.containerView?.third_title?.text  = "APPID: ${events[position].appid}"
                holder.containerView?.icon?.textResource = R.string.report
                holder.containerView?.icon?.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_report, 0, 0)
                holder.containerView?.backgroundColorResource = if (position % 2 == 0) android.R.color.white else R.color.common_gray
                holder.containerView?.icon?.setOnClickListener {
                    report(events[position].appid.toLong(), events[position].eid.toLong())
                }
                holder.containerView?.setOnLongClickListener {
                    ActionSheet(context, listOf("删除事件", "上报事件")) {
                        when (it) {
                            0 -> removeEvent(position)
                            1 -> report(events[position].appid.toLong(), events[position].eid.toLong())
                        }
                    }.show()
                    true
                }
            }

            override fun getItemCount() = events.size

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListAdapter.Holder {
                val inflater = LayoutInflater.from(parent.context)
                return BaseListAdapter.Holder(inflater.inflate(R.layout.common_list_item, parent, false))
            }
        }
    }

    private fun removeEvent(pos: Int) {
        val ev = events[pos]
        AdminRepo.deleteEvent(ev.eid) {
            if (it?.data?.hasError == false) {
                events.removeAt(pos)
                notifyDatasetChanged()
            }
        }
    }

    private fun initAthena() {
        val appid  = activity?.intent?.getStringExtra("appid") ?: ""
        val secret = activity?.intent?.getStringExtra("secret") ?: ""
        context?.let {
            Athena.init(it, appid, secret) {
                if (!it) {
                    activity?.toast(R.string.invalid_app)
                }
            }
        }
    }

    private fun getData() {
        AdminRepo.getEvents(activity?.intent?.getStringExtra("appid") ?: "", 0, 10) {
            if (it?.data?.hasError == false) {
                it.data?.events?.events?.let {
                    events.clear()
                    events.addAll(it)
                    notifyDatasetChanged()
                }
            } else {
                activity?.toast(R.string.common_error)
            }
        }
    }

    private fun refreshData() {
        AdminRepo.getEvents(activity?.intent?.getStringExtra("appid") ?: "", 0, 10) {
            if (it?.data?.hasError == false) {
                finishRefresh(true)
                it.data?.events?.events?.let {
                    events.clear()
                    events.addAll(it)
                    notifyDatasetChanged()
                }
            } else {
                finishRefresh(false)
                activity?.toast(R.string.common_error)
            }
        }
    }

    private fun report(appid: Long, eid: Long) {
        val reportData      = ReportData(eid)
        reportData.appid    = appid
        reportData.platform = "ANDROID"
        reportData.version  = "1.0.1"
        reportData.addExtra("test111", "v111").addExtra("test222", "v2222")

        Athena.report(reportData)
    }
}