package com.tentlers.mngapp.data;

import com.tentlers.mngapp.data.tables.house.TableHouse;
import com.tentlers.mngapp.data.tables.rooms.TableRooms;
import com.tentlers.mngapp.data.tables.bills.Bills;
import com.tentlers.mngapp.data.tables.tenants.TenantsPersonal;

import androidx.sqlite.db.SimpleSQLiteQuery;

public class FilterObj {
    public FilterObj(int filterTable) {
        this.filterTable = filterTable;
    }

    /*table constants*/
    public static final int TABLE_TENANT = 100;
    public static final int TABLE_BILL = 101;

    /*quarry constants*/
    /*these are used in building the quarry string*/
    public static final String SPACE_C = ", ";

    /*filter fields constant*/
    public static final int TYPE_ALL = 10;
    public static final int TYPE_HOUSE = 11;
    public static final int TYPE_ROOM = 12;/*will be used for  bills filter.*/
    public static final int TYPE_ROOM_ALLOTED = 13;
    public static final int TYPE_ROOM_UNALLOTED = 14;
    public static final int TYPE_PAYMENT_LEFT = 15;
    public static final int TYPE_PAYMENT_COMPLETE = 16;
    public static final int TYPE_TENANT = 17;/*will be used for bills filter*/

    /*filter orientation constants*/
    public static final int TYPE_EARLIEST_FIRST = 18;
    public static final int TYPE_EARLIEST_LAST = 19;
    public static final int TYPE_AMT_LOWEST_FIRST = 20;
    public static final int TYPE_AMT_LOWEST_LAST = 21;
    public static final int TYPE_LH_TOTAL_BILLS = 22;/*no of bills from low to high*/
    public static final int TYPE_HL_TOTAL_BILLS = 23;/*vice-versa*/

    /*filter querry orientation*/

    private final int filterTable;
    private int filterField;
    private int filterOrder;
    private long filterArg;

    public FilterObj setHouseId(long houseId) {
        setFilterField(TYPE_HOUSE);
        filterArg = houseId;
        return this;
    }

    public int getFilterField() {
        return filterField;
    }

    public FilterObj setFilterField(int filterField) {
        this.filterField = filterField;
        this.filterOrder = TYPE_EARLIEST_FIRST;/*always resets the field sorting order*/
        return this;
    }

    public FilterObj getPrimarySearchForTenant() {
        this.filterField = TYPE_ROOM_ALLOTED;
        this.filterOrder = TYPE_EARLIEST_FIRST;
        return this;
    }

    public void setOnlyFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }
    public FilterObj setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
        return this;
    }

    /*returns the string having the name of the fields as per the table set for the filter object . fields name are those which are recqurired in the UI*/
    private String getInitialStatement() {
        switch (filterTable) {
            case TABLE_TENANT:
                return "SELECT " + TenantsPersonal.TENANT_ID + SPACE_C + TenantsPersonal.TENANT_NAME + SPACE_C
                        + TableRooms.ROOM_ID + SPACE_C + TenantsPersonal.IS_ROOM_ALLOTTED + SPACE_C
                        + TenantsPersonal.UNPAID_AMOUNT + " FROM " + TenantsPersonal.TABLE_NAME;
            case TABLE_BILL:
                return "SELECT ";/*TODO reset the way in which bill card is prepared*/
            default:
                return "";
        }
    }

    /*returns the date field as per the table*/
    private String getDateField() {
        switch (filterTable) {
            case TABLE_TENANT:
                return TenantsPersonal.CREATE_DATE;
            case TABLE_BILL:
                return Bills.CREATE_DATE;
            default:
                return "";

        }
    }

    /*returns the date field as per the table*/
    private String getAmtField() {
        switch (filterTable) {
            case TABLE_TENANT:
                return TenantsPersonal.UNPAID_AMOUNT;
            case TABLE_BILL:
                return Bills.TOTAL_AMT;
            default:
                return "";
        }
    }

    /*returns the comparing expression for the amt based on the table selected*/
    private String getUnpaidExpression(boolean ispaid) {
        switch (filterTable) {
            case TABLE_TENANT:
                return ispaid ? " = 0" : " > 0";
            case TABLE_BILL:
                return ispaid ? " = 1" : " =0 ";
            default:
                return "";
        }
    }

    /*prepares the string as per the selection of the table and the order fields*/
    private String getOrderStmt() {
        String stmt = "ORDER BY ";

        switch (filterOrder) {
            case TYPE_EARLIEST_FIRST:
                return stmt + getDateField() + " DESC";
            case TYPE_EARLIEST_LAST:
                return stmt + getDateField() + " ASC";
            case TYPE_AMT_LOWEST_FIRST:
                return stmt + getAmtField() + " ASC";
            case TYPE_AMT_LOWEST_LAST:
                return stmt + getAmtField() + " DESC";
            case TYPE_LH_TOTAL_BILLS:
                return stmt + TenantsPersonal.TOTAL_BILLS + " ASC";
            case TYPE_HL_TOTAL_BILLS:
                return stmt + TenantsPersonal.TOTAL_BILLS + " DESC";
            default:
                return stmt + getDateField();
        }
    }

    /*returns the filtered part of the query*/
    public String getQuerry() {
        String stmt = getInitialStatement();
        switch (this.filterField) {
            case TYPE_ALL:
                break;
            case TYPE_HOUSE:
                stmt += " WHERE " + TableHouse.HOUSE_ID + " = " + filterArg;
                break;
            case TYPE_ROOM:
                break;
            case TYPE_ROOM_ALLOTED:
                stmt += " WHERE " + TenantsPersonal.IS_ROOM_ALLOTTED + " = 1 ";
                break;
            case TYPE_ROOM_UNALLOTED:
                stmt += " WHERE " + TenantsPersonal.IS_ROOM_ALLOTTED + " = 0 ";
                break;
            case TYPE_PAYMENT_LEFT:
                stmt += " WHERE " + getAmtField() + getUnpaidExpression(false);
                break;
            case TYPE_PAYMENT_COMPLETE:
                stmt += " WHERE " + getAmtField() + getUnpaidExpression(true);
                break;

        }
        return stmt + " " + getOrderStmt();
    }

    public SimpleSQLiteQuery getQuerryObject() {
        return new SimpleSQLiteQuery(getQuerry());
    }
}
