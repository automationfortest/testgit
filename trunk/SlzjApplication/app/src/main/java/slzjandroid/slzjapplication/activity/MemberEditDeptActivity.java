package slzjandroid.slzjapplication.activity;

import android.content.Intent;
import android.widget.GridView;

import java.util.ArrayList;

import slzjandroid.slzjapplication.R;
import slzjandroid.slzjapplication.adapter.ProjectChooseAdapter;
import slzjandroid.slzjapplication.context.AppContext;
import slzjandroid.slzjapplication.customView.NavigationView;
import slzjandroid.slzjapplication.dto.DepartmentInfo;

/**
 * Created by ASUS on 2016/4/17.
 */
public class MemberEditDeptActivity extends BasicActivity implements NavigationView.ClickCallback {
    private ArrayList<DepartmentInfo> projects = new ArrayList<>();
    private ArrayList<DepartmentInfo> dept = new ArrayList<>();
    private GridView gv_project;
    private ProjectChooseAdapter projectChooseAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_memberadd;
    }

    @Override
    protected void findViews() {
        gv_project = (GridView) findViewById(R.id.gv_member_add_choose_project_team);
    }

    @Override
    protected void init() {
        AppContext.getInstance().addActivity(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_main);
        navigationView.setTitle("所属项目");
        navigationView.setRightViewIsShow(true);
        navigationView.getRightView().setText("确认");
        navigationView.setClickCallback(this);
        projects = (ArrayList<DepartmentInfo>) getIntent().getSerializableExtra("projects");
        dept = (ArrayList<DepartmentInfo>) getIntent().getSerializableExtra("dept");
        projectChooseAdapter = new ProjectChooseAdapter(MemberEditDeptActivity.this, projects);
        gv_project.setAdapter(projectChooseAdapter);

    }

    @Override
    protected void bindViews() {
    }

    @Override
    public void onBackClick() {
        MemberEditDeptActivity.this.finish();
    }

    @Override
    public void onRightClick() {
        Intent intent = new Intent();
        intent.putExtra("changeproject", projectChooseAdapter.getData());
        intent.putExtra("dept", dept);
        setResult(RESULT_OK, intent);
        MemberEditDeptActivity.this.finish();

    }
}