/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package iec60870;

import lib.xml.kXMLElement;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;

import org.openide.filesystems.LocalFileSystem;

/**
 *
 * @author mikra
 */
public class Config {

    private static final String directory = "\\config";
    private static final String file = directory + "\\config_iec60870.xml";

    public static kXMLElement read() throws IOException {
        LocalFileSystem fs = new LocalFileSystem();
        File afile = new File(fs.getRootDirectory().getAbsolutePath() + file);
        InputStreamReader areader;
        FileInputStream afstream;
        kXMLElement xml = new kXMLElement();
        afstream = new FileInputStream(afile);
        areader = new InputStreamReader(afstream);
        xml.parseFromReader(areader);
        return xml;
    }

    public static void save(kXMLElement xml) throws IOException {
        LocalFileSystem fs = new LocalFileSystem();
        File afile = new File(fs.getRootDirectory().getAbsolutePath() + file);

        if (!exists_directory()) {
            create_directory();
        }

        if (exists_file()) {
            delete_file();
            create_file();
        }

        FileOutputStream fstream = null;
        PrintStream out = null;

        fstream = new FileOutputStream(afile);
        out = new PrintStream(fstream);
        xml.output(out);

        try {
            fstream.close();
        } catch (Exception e) {
        }

        try {
            out.close();
        } catch (Exception e) {
        }

    }

    private static void create_directory() {
        LocalFileSystem fs = new LocalFileSystem();
        File afile = new File(fs.getRootDirectory().getAbsolutePath() + directory);
        afile.mkdir();
    }

    private static boolean exists_directory() {
        LocalFileSystem fs = new LocalFileSystem();
        File afile = new File(fs.getRootDirectory().getAbsolutePath() + directory);
        return afile.exists();
    }

    private static boolean exists_file() {
        LocalFileSystem fs = new LocalFileSystem();
        File afile = new File(fs.getRootDirectory().getAbsolutePath() + file);
        return afile.exists();
    }

    private static void delete_file() {
        LocalFileSystem fs = new LocalFileSystem();
        File afile = new File(fs.getRootDirectory().getAbsolutePath() + file);
        afile.delete();
    }

    private static void create_file() throws IOException {
        LocalFileSystem fs = new LocalFileSystem();
        File afile = new File(fs.getRootDirectory().getAbsolutePath() + file);
        afile.createNewFile();
    }
}
