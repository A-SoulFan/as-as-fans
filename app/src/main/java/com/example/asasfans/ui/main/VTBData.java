package com.example.asasfans.ui.main;

public class VTBData {
    String memberName;
    String memberFansNum;

    public VTBData(String memberName, String memberFansNum) {
        this.memberName = memberName;
        this.memberFansNum = memberFansNum;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberFansNum() {
        return memberFansNum;
    }

    public void setMemberFansNum(String memberFansNum) {
        this.memberFansNum = memberFansNum;
    }
}
