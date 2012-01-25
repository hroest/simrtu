/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.interfaces.application;

import lib.interfaces.xml.XMLInterface;

/**
 *
 * @author mikra
 */
//-----------------------------------------------------------------------------
// Class
//-----------------------------------------------------------------------------

/**
 * Applications that want to be configured via the HTTP server must implement
 * this interface.
 *
 * <p>
 * This mechanism avoids to write specific servlets for every application.
 * </p>
 *
 * <p>
 * This interface extends XMLInterface in order to add XML functionnality to
 * this mechanism.
 *
 * <ul>
 * <li>
 * XML should be used to describe ALL parameters, including data mappings
 * </li>
 * <li>
 * Other parameters are just tuning parameters that may be modified in the
 * runtime.
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * Modifications:
 *
 * <dl>
 * <dt>
 * 10.IV.2003
 * </dt>
 * <dd>
 * Added the method {@link #getFileId()}
 * </dd>
 * </dl>
 * </p>
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 * @version 1.1 - 10.IV.2003
 *
 * @see com.itlity.io.XMLInterface
 */
public interface Configurable extends XMLInterface {

}
