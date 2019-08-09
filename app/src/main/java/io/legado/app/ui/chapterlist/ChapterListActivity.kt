package io.legado.app.ui.chapterlist

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import io.legado.app.R
import io.legado.app.base.VMBaseActivity
import io.legado.app.lib.theme.ATH
import io.legado.app.lib.theme.primaryTextColor
import io.legado.app.utils.getViewModel
import io.legado.app.utils.gone
import io.legado.app.utils.visible
import kotlinx.android.synthetic.main.activity_chapter_list.*
import kotlinx.android.synthetic.main.view_tab_layout.*
import kotlinx.android.synthetic.main.view_title_bar.*

class ChapterListActivity : VMBaseActivity<ChapterListViewModel>(R.layout.activity_chapter_list) {
    override val viewModel: ChapterListViewModel
        get() = getViewModel(ChapterListViewModel::class.java)

    private var searchView: SearchView? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        viewModel.bookUrl = intent.getStringExtra("bookUrl")
        view_pager.adapter = TabFragmentPageAdapter(supportFragmentManager)
        tab_layout.setupWithViewPager(view_pager)
    }

    override fun onCompatCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_view, menu)
        val search = menu.findItem(R.id.action_search)
        searchView = search.actionView as SearchView
        ATH.setTint(searchView!!, primaryTextColor)
        searchView?.maxWidth = resources.displayMetrics.widthPixels
        searchView?.onActionViewCollapsed()
        searchView?.setOnCloseListener {
            tab_layout.visible()
            false
        }
        searchView?.setOnSearchClickListener { tab_layout.gone() }
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return super.onCompatCreateOptionsMenu(menu)
    }

    private inner class TabFragmentPageAdapter internal constructor(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                1 -> BookmarkFragment()
                else -> ChapterListFragment()
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                1 -> getString(R.string.bookmark)
                else -> getString(R.string.chapter_list)
            }
        }

    }

    override fun onBackPressed() {
        if (tab_layout.isGone) {
            searchView?.onActionViewCollapsed()
            tab_layout.visible()
        } else {
            super.onBackPressed()
        }
    }
}