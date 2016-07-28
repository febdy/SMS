package com.dayeong.seatmanagementsystem;

/**
 * Created by Dayeong on 2016-07-20.
 */
public class StoreInfo {

    private String store_name;
    private int table_num;
    private int[] tablesStatus;

    public StoreInfo() {
    }

    public StoreInfo(String store_name, int table_num, int[] tablesStatus) {
        this.store_name = store_name;
        this.table_num = table_num;
        this.tablesStatus = tablesStatus;
    }

    public void setStoreName(String store_name) {
        this.store_name = store_name;
    }

    public String getStoreName() {
        return store_name;
    }

    public void setTable_num(int table_num) {
        this.table_num = table_num;
    }

    public int getTableNum() {
        return table_num;
    }

    public void setTablesStatus(int[] tablesStatus) {
        this.tablesStatus = tablesStatus;
    }

    public int[] getTablesStatus() {
        return tablesStatus;
    }

}
