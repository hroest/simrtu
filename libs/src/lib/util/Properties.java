/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.util;

import lib.util.*;
import lib.lang.Character;

import java.io.*;
import java.util.Enumeration;
import lib.util.Hashtable;

// Referenced classes of package com.ajile.util:
//            Hashtable, TimeStamper

public class Properties extends Hashtable
{
    private class PropDataReader extends BufferedReader
    {

        private String getNextPropLine()
            throws IOException
        {
            String s = null;
            Object obj = null;
            StringBuffer stringbuffer = new StringBuffer();
            while(s == null)
            {
                String s1 = readLine();
                if(s1 == null)
                    if(stringbuffer.length() == 0)
                        return null;
                    else
                        return stringbuffer.toString();
                if(s1.length() > 0)
                {
                    char c = s1.charAt(0);
                    if(c != '#' && c != '!')
                    {
                        s1 = removeLeadingWhiteSpace(s1, 0);
                        if(s1 != null)
                            if(lineContinued(s1))
                            {
                                stringbuffer.append(s1.substring(0, s1.length() - 1));
                                if(Properties.DEBUG_ENABLED)
                                    System.out.println("continuing data input line : [[" + s1 + "]]");
                            } else
                            {
                                stringbuffer.append(s1);
                                s = stringbuffer.toString();
                            }
                    } else
                    if(Properties.DEBUG_ENABLED)
                        System.out.println("skipping comment line : [[" + s1 + "]]");
                }
            }
            return s;
        }

        private String removeLeadingWhiteSpace(String s, int i)
        {
            for(int j = s.length(); i < j;)
                switch(s.charAt(i))
                {
                case 9: // '\t'
                case 10: // '\n'
                case 12: // '\f'
                case 13: // '\r'
                case 32: // ' '
                    i++;
                    break;

                default:
                    return s.substring(i, j);
                }

            return null;
        }

        private boolean lineContinued(String s)
        {
            int i = s.length() - 1;
            if(s.charAt(i) != '\\')
                return false;
            int j;
            for(j = i - 1; j >= 0 && s.charAt(j) == '\\'; j--);
            if((i - j & 1) == 1)
            {
                s = s.substring(0, s.length());
                return true;
            } else
            {
                return false;
            }
        }


        private PropDataReader(InputStream inputstream)
        {
            super(new InputStreamReader(inputstream));
        }

    }


    public Properties()
    {
        defaults = null;
    }

    public Properties(Properties properties)
    {
        defaults = properties;
    }

    public String getProperty(String s)
    {
        String s1 = (String)get(s);
        if(s1 == null && defaults != null)
            return defaults.getProperty(s);
        else
            return s1;
    }

    public String getProperty(String s, String s1)
    {
        String s2 = getProperty(s);
        if(s2 == null)
            return s1;
        else
            return s2;
    }

    public String setProperty(String s, String s1)
    {
        return (String)put(s, s1);
    }

    public Object put(Object obj, Object obj1)
    {
        if(!(obj instanceof String) || !(obj1 instanceof String))
            throw nonStringParm;
        else
            return super.put(obj, obj1);
    }

    public void list(PrintStream printstream)
    {
        printstream.println("-- listing properties --");
        String s1;
        for(Enumeration enumeration = keys(); enumeration.hasMoreElements(); printstream.println(s1))
        {
            String s = (String)enumeration.nextElement();
            s1 = getProperty(s);
            printstream.print(s);
            printstream.print('=');
        }

    }

    public Enumeration propertyNames()
    {
        if(defaults == null)
            return keys();
        int i = 1;
        for(Properties properties = defaults; properties != null; properties = properties.defaults)
            i++;

        Properties aproperties[] = new Properties[i];
        aproperties[i - 1] = this;
        Properties properties1 = defaults;
        for(int j = i - 2; j >= 0; j--)
        {
            aproperties[j] = properties1;
            properties1 = properties1.defaults;
        }

        lib.util.Hashtable hashtable = new lib.util.Hashtable();
        for(int k = 0; k < i; k++)
        {
            String s;
            for(Enumeration enumeration = aproperties[k].keys(); enumeration.hasMoreElements(); hashtable.put(s, aproperties[k].get(s)))
                s = (String)enumeration.nextElement();

        }

        return hashtable.keys();
    }

    public void store(OutputStream outputstream, String s)
        throws IOException
    {
        OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream, "ISO8859_1");
        if(s != null)
        {
            outputstreamwriter.write(35);
            outputstreamwriter.write(s);
            outputstreamwriter.write(10);
        }
        outputstreamwriter.write(35);
        outputstreamwriter.write(TimeStamper.getCurDateStringToUse());
        outputstreamwriter.write(10);
        for(Enumeration enumeration = keys(); enumeration.hasMoreElements(); outputstreamwriter.write(10))
        {
            String s1 = (String)enumeration.nextElement();
            String s2 = (String)get(s1);
            s1 = encodeCtrlChars(s1, true);
            s2 = encodeCtrlChars(s2, false);
            outputstreamwriter.write(s1);
            outputstreamwriter.write(61);
            outputstreamwriter.write(s2);
        }

        outputstreamwriter.flush();
        outputstreamwriter = null;
    }

    protected String encodeCtrlChars(String s, boolean flag)
    {
        boolean flag1 = false;
        int i = s.length();
        StringBuffer stringbuffer = new StringBuffer(i * 2);
        char ac[] = new char[6];
        ac[0] = '\\';
        ac[1] = 'u';
        for(int j = 0; j < i; j++)
        {
            char c = s.charAt(j);
            switch(c)
            {
            case 92: // '\\'
                stringbuffer.append("\\\\");
                flag1 = true;
                break;

            case 9: // '\t'
                stringbuffer.append("\\t");
                break;

            case 10: // '\n'
                stringbuffer.append("\\n");
                break;

            case 13: // '\r'
                stringbuffer.append("\\r");
                break;

            case 12: // '\f'
                stringbuffer.append("\\f");
                break;

            case 61: // '='
                stringbuffer.append("\\=");
                flag1 = true;
                break;

            case 58: // ':'
                stringbuffer.append("\\:");
                flag1 = true;
                break;

            case 32: // ' '
                if(flag)
                {
                    stringbuffer.append("\\ ");
                    break;
                }
                if(flag1)
                    stringbuffer.append(" ");
                else
                    stringbuffer.append("\\ ");
                break;

            case 35: // '#'
                stringbuffer.append("\\#");
                flag1 = true;
                break;

            case 33: // '!'
                stringbuffer.append("\\!");
                flag1 = true;
                break;

            default:
                flag1 = true;
                if(c < ' ' || c > '~')
                {
                    char ac1[] = Character.hexChars;
                    ac[2] = ac1[c >> 12 & 0xf];
                    ac[3] = ac1[c >> 8 & 0xf];
                    ac[4] = ac1[c >> 4 & 0xf];
                    ac[5] = ac1[c & 0xf];
                    stringbuffer.append(ac);
                } else
                {
                    stringbuffer.append(c);
                }
                break;
            }
        }

        return stringbuffer.toString();
    }

    public void load(InputStream inputstream)
        throws IOException
    {
        PropDataReader propdatareader = new PropDataReader(inputstream);
        String s;
        while((s = propdatareader.getNextPropLine()) != null)
        {
            if(DEBUG_ENABLED)
                System.out.println("current property data input line : [[" + s + "]]");
            int k = s.length();
            int i = getKeyEndIdx(s, k);
            int j = getValStartIdx(s, i, k);
            if(DEBUG_ENABLED)
                System.out.println("raw key : [[" + s.substring(0, i) + "]]");
            String s1 = convertEscapeChars(s.substring(0, i));
            if(DEBUG_ENABLED)
                System.out.println("decoded key : [[" + s1 + "]]");
            String s2;
            if(j < k)
            {
                if(DEBUG_ENABLED)
                    System.out.println("raw value : [[" + s.substring(j, k) + "]]");
                s2 = convertEscapeChars(s.substring(j, k));
                if(DEBUG_ENABLED)
                    System.out.println("decoded value : [[" + s2 + "]]");
            } else
            {
                if(DEBUG_ENABLED)
                    System.out.println("value : [[<empty string>]]");
                s2 = "";
            }
            put(s1, s2);
        }
    }

    private int getKeyEndIdx(String s, int i)
    {
        int j;
        for(j = 0; j < i;)
            switch(s.charAt(j))
            {
            case 92: // '\\'
                j += 2;
                if(DEBUG_ENABLED && j >= i)
                {
                    System.out.println("Unexpected \\ at eol while reading property line :");
                    System.out.print("[[");
                    System.out.print(s);
                    System.out.println("]]");
                    return i;
                }
                break;

            case 9: // '\t'
            case 10: // '\n'
            case 12: // '\f'
            case 13: // '\r'
            case 32: // ' '
            case 58: // ':'
            case 61: // '='
                return j;

            default:
                j++;
                break;
            }

        return j;
    }

    private int getValStartIdx(String s, int i, int j)
    {
        i = getNextNonSpaceIdx(s, i, j);
        if(i < j)
        {
            char c = s.charAt(i);
            if(c == '=' || c == ':')
                i++;
        }
        i = getNextNonSpaceIdx(s, i, j);
        return i;
    }

    private int getNextNonSpaceIdx(String s, int i, int j)
    {
        while(i < j)
            switch(s.charAt(i))
            {
            case 9: // '\t'
            case 10: // '\n'
            case 12: // '\f'
            case 13: // '\r'
            case 32: // ' '
                i++;
                break;

            default:
                return i;
            }
        return i;
    }

    private int getHexEncodedChar(String s, int i)
    {
        int j = 0;
        for(int k = 0; k < 4; k++)
        {
            char c = s.charAt(i++);
            if(c >= '0' && c <= '9')
            {
                j = (j << 4) + (c - 48);
            } else
            {
                c &= '\uFFDF';
                if(c >= 'A' && c <= 'F')
                    j = (j << 4) + ((c - 65) + 10);
                else
                    return -1;
            }
        }

        return j;
    }

    private String convertEscapeChars(String s)
    {
        int i = s.length();
        StringBuffer stringbuffer = new StringBuffer(i);
        for(int j = 0; j < i;)
        {
            char c = s.charAt(j++);
            if(c == '\\')
            {
                c = s.charAt(j++);
                if(c == 'u')
                {
                    int k = getHexEncodedChar(s, j);
                    if(k == -1)
                    {
                        stringbuffer.append("\\\\u");
                    } else
                    {
                        stringbuffer.append((char)k);
                        j += 4;
                    }
                } else
                {
                    if(c == 't')
                        c = '\t';
                    else
                    if(c == 'r')
                        c = '\r';
                    else
                    if(c == 'n')
                        c = '\n';
                    else
                    if(c == 'f')
                        c = '\f';
                    stringbuffer.append(c);
                }
            } else
            {
                stringbuffer.append(c);
            }
        }

        return stringbuffer.toString();
    }

    private static boolean DEBUG_ENABLED = false;
    private static IllegalArgumentException nonStringParm = new IllegalArgumentException("Key and value parameters must be String objects");
    protected Properties defaults;


}
