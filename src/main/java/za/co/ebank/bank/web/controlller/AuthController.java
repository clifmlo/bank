package za.co.ebank.bank.web.controlller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.ebank.bank.exception.UserExistsException;
import za.co.ebank.bank.model.ApiResponse;

import za.co.ebank.bank.model.dto.LoginDto;
import za.co.ebank.bank.model.dto.SignUpDto;
import za.co.ebank.bank.model.persistence.UserAccount;
import za.co.ebank.bank.service.UserAccountService;

/**
 *
 * @author cliff
 */
@RestController
@RequestMapping("/api/auth") 
@Slf4j
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    private final UserAccountService userAccountService;
    
    public AuthController(final UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
    
    @PostMapping("signup")
    public ResponseEntity createUserAccount(@Valid @RequestBody final SignUpDto signUpDto) {        
        try{
            UserAccount createdUserAccount = userAccountService.createUserAccount(signUpDto);        
            return new ResponseEntity(new ApiResponse(createdUserAccount, "success", false), HttpStatus.OK);
        } catch (UserExistsException ex){
            return new ResponseEntity(new ApiResponse(null, ex.getMessage(), true),  HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("signin")
    public ResponseEntity<String> authenticateUser(@RequestBody final LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
       return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
    }
}
