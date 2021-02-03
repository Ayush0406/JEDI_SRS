
package com.flipkart.validator;

import org.glassfish.jersey.server.ResourceConfig;


import com.flipkart.RESTController.*;


/**
 * Registers REST api classes for connection with server
 * to implement back-end functionality
 * @author JEDI04
 *
 */

public class ApplicationConfig extends ResourceConfig {

	public ApplicationConfig() {
		register(StudentRESTAPI.class);
		register(AdminRESTAPI.class);
		register(ProfessorRESTAPI.class);
	}
}

