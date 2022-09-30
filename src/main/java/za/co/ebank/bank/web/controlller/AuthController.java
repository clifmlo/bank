package za.co.ebank.bank.web.controlller;

import java.net.UnknownHostException;
import java.util.Base64;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.ebank.bank.exception.UserExistsException;
import za.co.ebank.bank.model.ApiResponse;
import za.co.ebank.bank.model.dto.SignUpDto;
import za.co.ebank.bank.model.persistence.UserAccount;
import za.co.ebank.bank.service.UserAccountService;
import za.co.ebank.bank.exception.PasswordMissmatchException;
import za.co.ebank.bank.model.dto.PasswordChangeDto;

/**
 *
 * @author cliff
 */
@RestController
@RequestMapping("/api/v1/auth") 
@Slf4j
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    private final UserAccountService userAccountService;
    
    public AuthController(final UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
    
    @PostMapping("admin/signup")
    public ResponseEntity createUserAccount(HttpServletRequest request, @Valid @RequestBody final SignUpDto signUpDto) {                 
        try{
            UserAccount createdUserAccount = userAccountService.createUserAccount(request.getHeader("referer") + "login", signUpDto);        
            return new ResponseEntity(new ApiResponse(createdUserAccount, "success", false), HttpStatus.OK);
        } catch (UserExistsException ex){
            log.error(ex.getMessage());
            return new ResponseEntity(new ApiResponse(null, ex.getMessage(), true),  HttpStatus.BAD_REQUEST);
        } catch (MessagingException | UnknownHostException ex) {
            log.error(ex.getMessage());
            return new ResponseEntity(new ApiResponse(null, ex.getMessage(), true),  HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("user/signin")
    public ResponseEntity<String> authenticateUser(HttpServletRequest request){
        try {            
            final String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.toLowerCase().startsWith("basic")) {            
                String base64Credentials = authorization.substring(5).trim();            
                byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
                String credentials = new String(credDecoded);
                String email = credentials.split(":")[0];
                String password = credentials.split(":")[1];

                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return new ResponseEntity(userAccountService.findByEmil(authentication.getName()), HttpStatus.OK);
            }
        
        } catch (AuthenticationException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }  
    
    @PostMapping("user/password/change")
    public ResponseEntity changeUserPassword(@RequestBody final PasswordChangeDto passwordChangeDto){       
        try{
            byte[] decodedPass = Base64.getDecoder().decode(passwordChangeDto.getPassword());
            byte[] decodedConfirmPass = Base64.getDecoder().decode(passwordChangeDto.getConfirmPassword());
            final String password = new String(decodedPass);
            final String confirmPassword = new String(decodedConfirmPass);            
            userAccountService.updatePassword(password, confirmPassword, passwordChangeDto.getId()); 

            return new ResponseEntity(new ApiResponse(null, "success", false), HttpStatus.OK);
        }catch(PasswordMissmatchException ex) {
            log.error(ex.getMessage());
            return new ResponseEntity(new ApiResponse(null, ex.getMessage(), true),  HttpStatus.BAD_REQUEST);     
        }
    }
}
