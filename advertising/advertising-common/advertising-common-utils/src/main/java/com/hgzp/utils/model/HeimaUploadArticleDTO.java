package com.hgzp.utils.model;

/**
 * 项目名：hgcb-parent
 * 类全名：com.hgcb.utils.model.HeimaUploadArticleDTO
 * 创建人：wangwk
 * 类描述：黑马校对
 * 创建日期：2020/9/5 9:19
 */
public class HeimaUploadArticleDTO {


    /**
     * article : 习进平；	习近平书记；	台湾共和国；
     大法弟子；	八九学潮；	摩梭族；
     黄鹊在后；	鞭辟入理；	不拘行迹；
     又能能够；	党的十八精神；	中华人民共国；
     更快的找到；	咯咯地叫声；	干活得时候；
     成份；	百页窗；	百試不爽；
     下工夫；	上方宝剑；	柯兴综合征；
     湖南卫辉；	英国耶鲁；	首都温哥华；
     赵孟频；	米开朗基罗；	柳洲；
     2月30日；	淳熙8年；	19822年23月32日；
     The Dow Jones Industrial Averag closed up 1.1%, at 9,791.
     3万多农民深受”高价电”之苦，。
     李克强、习近平出席会议。
     * token : 0
     * pid : 666c131926684c22b77200b25a01c5d3
     * checkType : {"CheckSentence":1,"CheckLeaderSort":0,"CheckMatchDots":0}
     * checkExtendInfo : {"ChkDefault":1,"ChkReturnResultType":0,"HtmlTag":1,"ChkEng":1,"UserErr":1,"Weight":2,"TaiWan":1,"FanTi":1,"Leader":1,"ProfType":1,"Needsug":1}
     */

    private String article;
    private String token;
    private String pid;
    private CheckTypeBean checkType = new CheckTypeBean();
    private CheckExtendInfoBean checkExtendInfo = new CheckExtendInfoBean();

    private String storyid;
    private String tableName;

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public CheckTypeBean getCheckType() {
        return checkType;
    }

    public void setCheckType(CheckTypeBean checkType) {
        this.checkType = checkType;
    }

    public String getStoryid() {
        return storyid;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setStoryid(String storyid) {
        this.storyid = storyid;
    }

    public CheckExtendInfoBean getCheckExtendInfo() {
        return checkExtendInfo;
    }

    public void setCheckExtendInfo(CheckExtendInfoBean checkExtendInfo) {
        this.checkExtendInfo = checkExtendInfo;
    }


    @Override
    public String toString() {
        return "HeimaUploadArticleDTO{" +
                "article='" + article + '\'' +
                ", token='" + token + '\'' +
                ", pid='" + pid + '\'' +
                ", checkType=" + checkType +
                ", checkExtendInfo=" + checkExtendInfo +
                ", storyid='" + storyid + '\'' +
                ", tableName='" + tableName + '\'' +
                '}';
    }

    public static class CheckTypeBean {
        /**
         * CheckSentence : 1
         * CheckLeaderSort : 0
         * CheckMatchDots : 0
         */

        private int CheckSentence = 1;
        private int CheckLeaderSort = 0;
        private int CheckMatchDots = 0;

        public int getCheckSentence() {
            return CheckSentence;
        }

        public void setCheckSentence(int CheckSentence) {
            this.CheckSentence = CheckSentence;
        }

        public int getCheckLeaderSort() {
            return CheckLeaderSort;
        }

        public void setCheckLeaderSort(int CheckLeaderSort) {
            this.CheckLeaderSort = CheckLeaderSort;
        }

        public int getCheckMatchDots() {
            return CheckMatchDots;
        }

        public void setCheckMatchDots(int CheckMatchDots) {
            this.CheckMatchDots = CheckMatchDots;
        }
    }

    public static class CheckExtendInfoBean {
        /**
         * ChkDefault : 1
         * ChkReturnResultType : 0
         * HtmlTag : 1
         * ChkEng : 1
         * UserErr : 1
         * Weight : 2
         * TaiWan : 1
         * FanTi : 1
         * Leader : 1
         * ProfType : 1
         * Needsug : 1
         */

        private int ChkDefault = 1 ;
        private int ChkReturnResultType = 0;
        private int HtmlTag = 1;
        private int ChkEng = 1;
        private int UserErr = 1 ;
        private int Weight = 2 ;
        private int TaiWan = 1;
        private int FanTi = 1;
        private int Leader = 1;
        private int ProfType = 1;
        private int Needsug = 1;

        public int getChkDefault() {
            return ChkDefault;
        }

        public void setChkDefault(int ChkDefault) {
            this.ChkDefault = ChkDefault;
        }

        public int getChkReturnResultType() {
            return ChkReturnResultType;
        }

        public void setChkReturnResultType(int ChkReturnResultType) {
            this.ChkReturnResultType = ChkReturnResultType;
        }

        public int getHtmlTag() {
            return HtmlTag;
        }

        public void setHtmlTag(int HtmlTag) {
            this.HtmlTag = HtmlTag;
        }

        public int getChkEng() {
            return ChkEng;
        }

        public void setChkEng(int ChkEng) {
            this.ChkEng = ChkEng;
        }

        public int getUserErr() {
            return UserErr;
        }

        public void setUserErr(int UserErr) {
            this.UserErr = UserErr;
        }

        public int getWeight() {
            return Weight;
        }

        public void setWeight(int Weight) {
            this.Weight = Weight;
        }

        public int getTaiWan() {
            return TaiWan;
        }

        public void setTaiWan(int TaiWan) {
            this.TaiWan = TaiWan;
        }

        public int getFanTi() {
            return FanTi;
        }

        public void setFanTi(int FanTi) {
            this.FanTi = FanTi;
        }

        public int getLeader() {
            return Leader;
        }

        public void setLeader(int Leader) {
            this.Leader = Leader;
        }

        public int getProfType() {
            return ProfType;
        }

        public void setProfType(int ProfType) {
            this.ProfType = ProfType;
        }

        public int getNeedsug() {
            return Needsug;
        }

        public void setNeedsug(int Needsug) {
            this.Needsug = Needsug;
        }

    }
}
