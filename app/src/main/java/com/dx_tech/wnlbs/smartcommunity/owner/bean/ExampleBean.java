package com.dx_tech.wnlbs.smartcommunity.owner.bean;

/**
 * Created by 戴圣伟 on 0016 2017/10/16.
 * 接口数据例子
 * 实际开发与后台数据格式同步修改
 * 可使用GsonFormat插件快速生成
 */

public class ExampleBean {

    private String RET_CODE;
    private String RET_DATA;
    private String RET_MSG;

    public String getRET_CODE() {
        return RET_CODE;
    }

    public void setRET_CODE(String RET_CODE) {
        this.RET_CODE = RET_CODE;
    }

    public String getRET_DATA() {
        return RET_DATA;
    }

    public void setRET_DATA(String RET_DATA) {
        this.RET_DATA = RET_DATA;
    }

    public String getRET_MSG() {
        return RET_MSG;
    }

    public void setRET_MSG(String RET_MSG) {
        this.RET_MSG = RET_MSG;
    }

    @Override
    public String toString() {
        return "AddAlertSetResultBean{" +
                "RET_CODE='" + RET_CODE + '\'' +
                ", RET_DATA='" + RET_DATA + '\'' +
                ", RET_MSG='" + RET_MSG + '\'' +
                '}';
    }
}
