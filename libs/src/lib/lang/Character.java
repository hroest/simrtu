/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.lang;

public final class Character
{
  public static final char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

  public static boolean isJavaIdentifierStart(char paramChar)
  {
    return ((paramChar == '$') || (paramChar == '_') || ((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= 'A') && (paramChar <= 'Z')));
  }

  public static boolean isJavaIdentifierPart(char paramChar)
  {
    return ((paramChar == '$') || (paramChar == '_') || ((paramChar >= '0') && (paramChar <= '9')) || ((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= 'A') && (paramChar <= 'Z')));
  }
}
