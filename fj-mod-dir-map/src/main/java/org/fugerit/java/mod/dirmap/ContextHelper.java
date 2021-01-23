package org.fugerit.java.mod.dirmap;

import java.io.File;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextHelper {

	private final static Logger logger = LoggerFactory.getLogger( ContextHelper.class );
	
	public static Object resolveJNDI( String jndiName, Object defValue ) throws NamingException {
		logger.debug( "ContextHelper.resulveJNDI - jndiName : "+jndiName );
		Object value = null;
		InitialContext ic = new InitialContext();
		value = ic.lookup( jndiName );
		if ( value == null ) {
			logger.debug( "ContextHelper.resulveJNDI - null value, using default : "+defValue );
			value = defValue;
		}
		logger.debug( "ContextHelper.resulveJNDI - value : "+value );
		return value;
	}
	
	public static File resolvePath( ServletContext context, String path ) throws Exception {
		logger.debug( "ContextHelper.resolvePath - path : "+path );
		if ( path.indexOf( "jndi:" ) == 0 ) {
			String jndiName = path.substring( 5 );
			path = (String)resolveJNDI( jndiName, null );
		}
		File result = new File( path );
		if ( !result.exists() ) {
			result = new File( context.getRealPath( "/" ), path );
		}
		logger.debug( "ContextHelper.resolvePath - result : "+result );
		return result;
	}
	
}
