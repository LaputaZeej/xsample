package com.laputa.zeej.other;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XuWuJian(Xuwj@hadlinks.com) on 2021/9/13
 * <p>
 * Describe:子菜谱烹饪单例实例 列表
 */
public class CookBookSingListleHolder {

    private List<CookBookSingleData> cookBookSingleDataList ;
    private CookBookSingListleHolder(){
        cookBookSingleDataList = new ArrayList<>();
    }

    public void setCookBookSingleDataList(List<CookBookSingleData> cookBookSingleDataList) {
        this.cookBookSingleDataList.clear();
        this.cookBookSingleDataList.addAll(cookBookSingleDataList);
    }

    public List<CookBookSingleData> getCookBookSingleDataList() {
        return cookBookSingleDataList;
    }

    public List<CookBookSingleData> getCookBookSingleDataListDeepCopy() {
        return new ArrayList<>(cookBookSingleDataList);
    }

    public static CookBookSingListleHolder getInstance() {
        return Single.INSTANCE;
    }

    private static final class Single {
        public static final CookBookSingListleHolder INSTANCE = new CookBookSingListleHolder();
    }

    /**
     * 清空单例
     */
    public void clearCookBookSingleDataList() {
        if (cookBookSingleDataList != null) {
            cookBookSingleDataList.clear();
        }
    }
}
