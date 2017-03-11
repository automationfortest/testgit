package slzjandroid.slzjapplication.utils;

import java.io.Serializable;

import slzjandroid.slzjapplication.dto.MemberAddBean;

public class PinyinModel implements Serializable ,Comparable<PinyinModel>{
    private int id;
    private String whole; //原字符串
    private String first; //首字母
    private String pingyin; //全拼
    private String firsts; //所有首字母
    private Boolean isChecked=false;//是否选中
    private MemberAddBean member;//对象

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWhole() {
        return whole;
    }

    public void setWhole(String whole) {
        this.whole = whole;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getPingyin() {
        return pingyin;
    }

    public void setPingyin(String pingyin) {
        this.pingyin = pingyin;
    }

    public String getFirsts() {
        return firsts;
    }

    public void setFirsts(String firsts) {
        this.firsts = firsts;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean checked) {
        isChecked = checked;
    }

    public MemberAddBean getMember() {
        return member;
    }

    public void setMember(MemberAddBean member) {
        this.member = member;
    }

    @Override
    public int compareTo(PinyinModel another) {
        return 0;
    }
}
