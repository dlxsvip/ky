package com.ky.logic.utils.page.restriction;

/**
 * Created by ZY on 2017/2/13.
 */
public class RelationalTable {

    private String associationPath;
    private String aliasName;

    public RelationalTable(String associationPath, String aliasName) {
        this.associationPath = associationPath;
        this.aliasName = aliasName;
    }

    public RelationalTable(String associationPath) {
        this.associationPath = associationPath;
    }

    public String getAssociationPath() {
        return associationPath;
    }

    public void setAssociationPath(String associationPath) {
        this.associationPath = associationPath;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
