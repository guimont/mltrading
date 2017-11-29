package com.mltrading.dao.mongoFile;

import com.mongodb.DBCursor;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class MongoUtil {

    public static void saveDirectory(GridFS gfsModel,  File dir ) throws IOException {
        Collection<File> files = FileUtils.listFiles(dir, null, true);
        for (File f : files) {
            GridFSInputFile gfsFile = gfsModel.createFile(f);
            if (f.getPath().contains("_temporary"))
                gfsFile.setFilename(f.getPath().split("_temporary")[0] + f.getName());
            else
                gfsFile.setFilename(f.getPath());
            gfsFile.save();
        }
    }


    public static void removeDB(GridFS gfsModel) {
        DBCursor cursor = gfsModel.getFileList();
        while (cursor.hasNext()) {
            GridFSDBFile f = gfsModel.findOne(cursor.next());
            gfsModel.remove(f.getFilename());
        }
    }


    public static void distribute(GridFS gfsModel,File dir, File dirmeta) throws IOException {
        DBCursor cursor = gfsModel.getFileList();
        while (cursor.hasNext()) {
            if (!dir.exists())
                FileUtils.forceMkdir(dir);
            if (!dirmeta.exists())
                FileUtils.forceMkdir(dirmeta);
            GridFSDBFile f = gfsModel.findOne(cursor.next());
            f.writeTo(f.getFilename());
        }
    }


}
