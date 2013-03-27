
package com.boredream.fightwithoutend.domain;

import android.os.Environment;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ´æµµ¼ÇÂ¼
 * 
 * @author boredream
 *
 */
public class Record implements Serializable {

    public static final String saveDir = Environment.getExternalStorageDirectory()
            + "/fightwithoutend/save/";

    public static final String recordDateFormat = "yyyy-MM-dd HH:mm";

    private int id;
    private String name;
    private Hero heroInfo;
    private Date saveDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hero getHeroInfo() {
        return heroInfo;
    }

    public void setHeroInfo(Hero heroInfo) {
        this.heroInfo = heroInfo;
    }

    public Date getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Date saveDate) {
        this.saveDate = saveDate;
    }

    public String getFormatedDate() {
        SimpleDateFormat format = new SimpleDateFormat(recordDateFormat, Locale.CHINA);
        return format.format(saveDate);
    }

    public Record(int id, Hero heroInfo, Date saveDate) {
        super();
        this.id = id;
        this.heroInfo = heroInfo;
        this.saveDate = saveDate;
    }
}
