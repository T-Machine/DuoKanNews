package com.example.group44.newscollection;

// 用于统计频数
public class String_val {
    private String chara;
    private Integer val;
    public String_val(String chara, Integer count){
        this.chara = chara;
        this.val = count;
    }

    public void setChara(String chara) {
        this.chara = chara;
    }

    public Integer getVal() {
        return val;
    }

    public String getChara() {
        return chara;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    public void add(){
        this.val++;
    }
}
