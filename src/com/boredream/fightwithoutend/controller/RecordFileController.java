
package com.boredream.fightwithoutend.controller;

import com.boredream.fightwithoutend.domain.Record;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ¼ÇÂ¼¶Á/´æ¿ØÖÆÆ÷
 * 
 * @author boredream
 *
 */
public class RecordFileController {

    private static List<Record> records;

    public static List<Record> getAllRecords() {
        if (records == null) {
            records = new ArrayList<Record>();
            records.add(load(1));
            records.add(load(2));
            records.add(load(3));
        }
        return records;
    }

    public static void save(Record record, int index) {
        // save to cache
        records.remove(index - 1);
        records.add(index - 1, record);
        // save to file
        File dir = new File(Record.saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        record.setName("sv" + index);
        File saveFile = new File(Record.saveDir + record.getName());
        if (!saveFile.exists()) {
            try {
                saveFile.createNewFile();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile));
                oos.writeObject(record);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Record load(int index) {
        Record record = null;
        File saveFile = new File(Record.saveDir + "sv" + index);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(
                    new FileInputStream(saveFile));
            record = (Record) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return record;
    }
}
