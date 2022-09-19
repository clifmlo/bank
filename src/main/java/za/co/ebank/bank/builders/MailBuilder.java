
package za.co.ebank.bank.builders;


import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import za.co.ebank.bank.mailer.Email;

/**
 *
 * @author cliff
 */
public class MailBuilder {
    private String subject;
	private String to;
	private String from;
	private String template;
	private final VelocityContext velocityContext;
	private final VelocityEngine velocityEngine;
	
	public MailBuilder() {
		this.to = "";
		this.from = "";
		this.subject = "";
		this.template = "";
		this.velocityContext = new VelocityContext();
		final Properties properties = new Properties();
		properties.setProperty("input.encoding", "UTF-8");
		properties.setProperty("output.encoding", "UTF-8");
		properties.setProperty("resource.loader", "file, class, jar");
		properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		this.velocityEngine = new VelocityEngine(properties);
	}
	
	public MailBuilder subject(String subject) {
		this.subject = subject;
		return this;
	}
	
	public MailBuilder to(String to) {
		this.to = to;
		return this;
	}
	
	public MailBuilder from(String from) {
		this.from = from;
		return this;
	}
	
	public MailBuilder template(String template) {
		this.template = template;
		return this;
	}

	public MailBuilder addContext(String key, String value) {
		velocityContext.put(key, value);
		return this;
	}

	public MailBuilder addContext(String key, Object value) {
		velocityContext.put(key, value);
		return this;
	}

	public Email createMail() throws IllegalArgumentException {
		final Template templateEngine = velocityEngine.getTemplate("templates/" + this.template);
		final StringWriter stringWriter = new StringWriter();
		templateEngine.merge(this.velocityContext, stringWriter);
		if(this.to.isEmpty() || this.from.isEmpty()) {
			throw new IllegalArgumentException("Missing mail headers");
		}
		final Email result = new Email();
		result.setTo(this.to);
		result.setFrom(this.from);
		result.setContent(stringWriter.toString());
		result.setSubject(this.subject);

		return result;
	}
}
