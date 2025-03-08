package in.co.goodpay.api.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import in.co.goodpay.api.dto.LoginDTO;
import in.co.goodpay.api.entity.User;
import in.co.goodpay.api.repository.UserRepository;
import in.co.goodpay.api.service.LoginService;
import in.co.goodpay.api.util.JwtUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl  implements LoginService{
	
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Override
	public String logIn(LoginDTO loginDTO) {
		
		// Validate input
				if (loginDTO.getMobileNumber() == null || loginDTO.getMobileNumber().isEmpty()) {
					throw new IllegalArgumentException("Mobile Number cannot be null or empty");
				}
				if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
					throw new IllegalArgumentException("Password cannot be null or empty");
				}

				try {
					// Attempt to authenticate the user
					authenticationManager
							.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getMobileNumber(), loginDTO.getPassword()));

					// If authentication is successful, return a success message (you can replace
					// this with a JWT token)
				      User userFromDb = userRepository.findByMobileNumber(loginDTO.getMobileNumber()).get();
				      
				   return   jwtUtil.generateToken(userFromDb.getMobileNumber());

				} catch (BadCredentialsException e) {
					// Handle bad credentials (incorrect username or password)
					return "Invalid email or password";

				} catch (AuthenticationException e) {
					// Handle other authentication exceptions
					return "Authentication failed: " + e.getMessage();
				}
			

	}

}
