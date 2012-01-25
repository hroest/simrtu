/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RT_DB;

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

    // relativer Pfad zum Verzeichnis mit den Configs
    private static final String t = File.separator;
    public static final String ROOT_DIR = "."+t+"config";
    private static final String file = "config_db.xml";
    
    public static kXMLElement read() throws IOException
    {
            LocalFileSystem fs = new LocalFileSystem();
            File afile = new File(ROOT_DIR + t + file);
            InputStreamReader areader;
            FileInputStream afstream;
            kXMLElement xml = new kXMLElement();
            afstream = new FileInputStream(afile);
            areader = new InputStreamReader(afstream);
            xml.parseFromReader(areader);
            return xml;
    }

    public static void save(kXMLElement xml) throws IOException
    {
        LocalFileSystem fs = new LocalFileSystem();
        File afile = new File(ROOT_DIR + t + file);

        if (!exists_directory())
        {
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

        try
        {
            fstream.close();
        }
        catch(Exception e)
        {

        }

        try
        {
            out.close();
        }
        catch(Exception e)
        {

        }

    }

    private static void create_directory()
    {
         LocalFileSystem fs = new LocalFileSystem();
         File afile = new File(ROOT_DIR);
         afile.mkdir();
    }

    private static boolean exists_directory()
    {
         LocalFileSystem fs = new LocalFileSystem();
         File afile = new File(ROOT_DIR);
         return afile.exists();
    }
       
    private static boolean exists_file()
    {
        LocalFileSystem fs = new LocalFileSystem();
        File afile = new File(ROOT_DIR + t + file);
        return afile.exists();
    }

    private static void delete_file()
    {
        LocalFileSystem fs = new LocalFileSystem();
        File afile = new File(ROOT_DIR + t + file);
        afile.delete();
    }

    private static void create_file() throws IOException
    {
        LocalFileSystem fs = new LocalFileSystem();
        File afile = new File(ROOT_DIR + t + file);
        afile.createNewFile();
    }

}
