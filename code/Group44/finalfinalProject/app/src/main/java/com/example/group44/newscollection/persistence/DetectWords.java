package com.example.group44.newscollection.persistence;
import com.example.group44.newscollection.String_val;

import java.util.Comparator;

public class DetectWords {
    static String keywords = "!,@,#,$,%,^,&,*,(,),_,+,{,},|,:,\",>,?,/,.,<,;,\',[,],\\,=,-,《,》,？,：,“,”,|,「,」,+,——,。,，,新浪,公众号,关注,搜索,详情,公众,每天,你,我,它,她,他,本报,完成,不,好,坏,大,小,增加,减少,结果,重要,显,相当,上述,情况,长,短,正,负,有,因为,结果,原因,还,高,低,在,期间,时,就行,无,更,了,强,弱,淡,浓,稀,喜欢,简单,羡慕,困难,嫉妒,亮,暗,丑,美,没,么,怎,如何,着,笑,哭,啊,呢,呀,吧,嘿,";
    static String keywords2 = "0,1,2,3,4,5,6,7,8,9,那么,比如,以及,以至,自己,多,少,说,可能,一定,一,二,三,四,五,六,七,八,九,十,出,越来,好,坏,年,绝对,作为,确实,当然,仅仅,如果,q,w,e,r,t,y,u,i,o,p,a,s,d,f,g,h,j,k,l,z,x,c,v,b,n,m,Q,W,E,R,T,Y,U,I,O,P,A,S,D,F,G,H,J,K,L,Z,X,C,V,B,N,M";

    public static boolean inValid(String input){
        String[] key = null;
        key = keywords.split(",");
        for(String e : key){
            if(input.indexOf(e) != -1) return false;
        }
        key = keywords2.split(",");
        for(String e : key){
            if(input.indexOf(e) != -1) return false;
        }
        return true;
    }
}
