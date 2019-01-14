package libsearcher.mepride.android.librarysearcher.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import libsearcher.mepride.android.librarysearcher.R;
import libsearcher.mepride.android.librarysearcher.common.MyLazyFragment;
import libsearcher.mepride.android.librarysearcher.model.Situation;
import libsearcher.mepride.android.librarysearcher.view.adapter.StateAdapter;

public class StateFragment extends MyLazyFragment {

    @BindView(R.id.rv_state)
    RecyclerView recyclerView;
    private List<Situation> situationList = new ArrayList<>();
    private StateAdapter stateAdapter;

    public static StateFragment newInstance(List<Situation> situations){
        StateFragment stateFragment = new StateFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("situations",(Serializable)situations);
        stateFragment.setArguments(bundle);
        return stateFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_state;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        situationList = (List<Situation>) getArguments().getSerializable("situations");
        System.out.println(situationList.get(1).getState());
        stateAdapter = new StateAdapter(situationList);
        recyclerView.setAdapter(stateAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        new Thread(){
            @Override
            public void run() {
                try {
                    //你的处理逻辑,这里简单睡眠一秒
                    sleep(500);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stateAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
