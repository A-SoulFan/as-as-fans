package com.example.asasfans.data;

/**
 * @author: zyxdb
 * @date: 2022/3/13
 * @description vtb数据类
 */
public class VTBData {
    String memberName;
    String membersign;
    String memberFaceUrl;
    int memberFansNum;
    int memberFansNumRise;
    int memberGuardNum;//舰长数量
    Boolean firstLoad;

//    public VTBData(String memberName, int memberFansNum, String membersign, String memberFaceUrl, int memberFansNumRise, int memberGuardNum,Boolean firstLoad) {
//        this.memberName = memberName;
//        this.membersign = membersign;
//        this.memberFansNum = memberFansNum;
//        this.memberFaceUrl = memberFaceUrl;
//        this.memberFansNumRise = memberFansNumRise;
//        this.memberGuardNum = memberGuardNum;
//        this.firstLoad = firstLoad;
//    }
    public VTBData(Boolean firstLoad) {
        this.firstLoad = firstLoad;
    }

    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMembersign() {
        return membersign;
    }
    public void setMembersign(String membersign) {
        this.membersign = membersign;
    }

    public String getMemberFaceUrl() {
        return memberFaceUrl;
    }
    public void setMemberFaceUrl(String memberFaceUrl) {
        this.memberFaceUrl = memberFaceUrl;
    }

    public int getMemberFansNum() {
        return memberFansNum;
    }
    public void setMemberFansNum(int memberFansNum) {
        this.memberFansNum = memberFansNum;
    }

    public int getMemberFansNumRise() {
        return memberFansNumRise;
    }
    public void setMemberFansNumRise(int memberFansNumRise) {
        this.memberFansNumRise = memberFansNumRise;
    }
    public int getMemberGuardNum() {
        return memberGuardNum;
    }
    public void setMemberGuardNum(int memberGuardNum) {
        this.memberGuardNum = memberGuardNum;
    }

    public Boolean getFirstLoad() {
        return firstLoad;
    }
    public void setFirstLoad(Boolean firstLoad) {
        this.firstLoad = firstLoad;
    }
}
