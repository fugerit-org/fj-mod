package org.fugerit.java.mod.dirmap;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fugerit.java.core.xml.dom.DOMIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class DirectoryMapServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6728876009586393088L;

	private final static Logger logger = LoggerFactory.getLogger( DirectoryMapServlet.class );
	
	/* (non-Javadoc)
	 * @see org.morozko.java.core.ent.servlet.filter.HttpFilter#init(javax.servlet.FilterConfig)
	 */
	public void init( ServletConfig config ) throws ServletException {
		try {
			File configFile = ContextHelper.resolvePath( config.getServletContext(), config.getInitParameter( "dir-map-config" ) );
			Document doc = DOMIO.loadDOMDoc( configFile );
			this.dirMapConfig = DirMapConfig.configure( doc );
		} catch (Exception e) {
			throw ( new ServletException( e ) );
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			MapOutputPage.outputPage( request, response, this.dirMapConfig );	
		} catch (Exception e) {
			logger.error( "Error : "+e, e );
			response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
		}
	}

	/* (non-Javadoc)
	 * @see org.morozko.java.core.ent.servlet.filter.HttpFilter#destroy()
	 */
	public void destroy() {
		this.dirMapConfig = null;
	}
	
	private DirMapConfig dirMapConfig;

}


