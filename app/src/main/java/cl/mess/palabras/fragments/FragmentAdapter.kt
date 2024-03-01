package cl.mess.palabras.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import cl.mess.palabras.fragments.englishword.ui.FragmentWordsEnglish
import cl.mess.palabras.fragments.quote.FragmentQuotes
import cl.mess.palabras.fragments.spanishword.ui.WordsSpanishFragment

class FragmentAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {
    private val numTabs: Int = behavior

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> WordsSpanishFragment()
            1 -> FragmentQuotes()
            2 -> FragmentWordsEnglish()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getCount(): Int {
        return numTabs
    }
}
