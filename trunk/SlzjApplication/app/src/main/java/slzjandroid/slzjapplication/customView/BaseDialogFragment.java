package slzjandroid.slzjapplication.customView;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import slzjandroid.slzjapplication.R;

/**
 * Created by xuyifei on 16/5/11.
 */
public class BaseDialogFragment extends DialogFragment {

    public static class BaseFragment extends Fragment {

        protected String title = "Default";
        public static final String TITLE = "title";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                title = getArguments().getString(TITLE);
            }

        }


        public FragmentTransaction getTransaction() {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.fragment_up2down, R.anim.fragment_down2up);

            return transaction;
        }

        /*public <T extends View> T justFindIt(View v,int id){

            if(!(((T)v.findViewById(id)) instanceof AdapterView)){

                ((T)v.findViewById(id)).setOnClickListener(this);
            }

            return  (T)v.findViewById(id);

        }*/
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded())
            super.show(manager, tag);
    }
}
