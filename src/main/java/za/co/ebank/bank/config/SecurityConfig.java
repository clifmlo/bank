
package za.co.ebank.bank.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 *
 * @author cliff
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
   
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.cors().and().csrf().disable();
        security.httpBasic().disable();
    } 
}
