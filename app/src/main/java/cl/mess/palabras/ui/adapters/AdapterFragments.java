package cl.mess.palabras.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import cl.mess.palabras.ui.fragments.FragmentQuotes;
import cl.mess.palabras.ui.fragments.spanishwords.FragmentWordsSpanish;
import cl.mess.palabras.ui.fragments.FragmentWordsEnglish;

public class AdapterFragments extends FragmentPagerAdapter {
    int numTabs;

    public AdapterFragments(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new FragmentWordsSpanish();
            case 1:
                return new FragmentQuotes();
            case 2:
                return new FragmentWordsEnglish();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}

