package edgecom.tech.athenasdk.ui.fragments


import android.view.LayoutInflater
import android.view.ViewGroup

import edgecom.tech.athena.model.Cohort
import edgecom.tech.athena.repo.AdminRepo
import edgecom.tech.athenasdk.R
import edgecom.tech.athenasdk.ui.adapter.BaseListAdapter
import edgecom.tech.athenasdk.ui.adapter.BaseListFragment
import edgecom.tech.athenasdk.ui.adapter.IAdapterProxy

import kotlinx.android.synthetic.main.common_list_item.view.*

import org.jetbrains.anko.toast

/**
 * 添加cohort
 */
class CohortFragments: BaseListFragment() {

    val cohorts: MutableList<Cohort> = mutableListOf()

    override fun initData() {
        AdminRepo.getCohorts(activity?.intent?.getStringExtra("appid") ?: "", 0, 10) {
            if (it?.data?.hasError == false) {
                it.data?.list?.cohorts?.let {
                    cohorts.clear()
                    cohorts.addAll(it)
                    notifyDatasetChanged()
                }
            } else {
                activity?.toast(R.string.common_error)
            }
        }
    }

    override fun add() {

    }

    override fun getAdapter(): IAdapterProxy? {
        return object: IAdapterProxy {

            override fun onBindViewHolder(holder: BaseListAdapter.Holder, position: Int) {
                holder.containerView?.title?.text        = "${cohorts[position].name}"
                holder.containerView?.second_title?.text = "ID: ${cohorts[position].cohortId}"
                holder.containerView?.third_title?.text  = "APPID: ${cohorts[position].appid}"
            }

            override fun getItemCount() = cohorts.size

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListAdapter.Holder {
                val inflater = LayoutInflater.from(parent.context)
                return BaseListAdapter.Holder(inflater.inflate(R.layout.common_list_item, parent, false))
            }
        }
    }
}