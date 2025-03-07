package in.co.goodpay.api.service.impl;

import in.co.goodpay.api.dto.*;
import in.co.goodpay.api.entity.User;
import in.co.goodpay.api.entity.Wallet;
import in.co.goodpay.api.exception.*;
import in.co.goodpay.api.repository.UserRepository;
import in.co.goodpay.api.repository.WalletRepository;
import in.co.goodpay.api.service.EmailService;
import in.co.goodpay.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final WalletRepository walletRepository;
    private final EmailService  emailService;

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO, String createdBy) {
        validateRequest(userRequestDTO);

        if (userRepository.findByMobileNumber(userRequestDTO.getMobileNumber()).isPresent()) {
            throw new DuplicateUserException("User already exists with mobile: " + userRequestDTO.getMobileNumber());
        }

        // Map DTO to User entity
        User user = modelMapper.map(userRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword())); // Encrypt password
        user.setStatus("ACTIVE");
        user.setCreatedBy(createdBy);

        // Save User
        User savedUser = userRepository.save(user);

        // Create Wallet for the User
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(BigDecimal.ZERO); // Initialize with 0 balance
        wallet.setStatus("ACTIVE");
        wallet.setCreatedBy(createdBy);

        // Save Wallet
        walletRepository.save(wallet);

        // Send email notification
        String subject = "🎉 Welcome to GoodPay - Your Account is Ready!";
        String message = "Dear " + savedUser.getName() + ",\n\n"
                       + "Welcome to **GoodPay**! We are excited to have you on board. Your account has been successfully created, and you can now start using our services for secure transactions, recharges, bill payments, and more.\n\n"
                       + "🔑 **Login Credentials:**\n"
                       + "📱 Mobile Number: **" + savedUser.getMobileNumber() + "**\n"
                       + "🔒 Password: **" + userRequestDTO.getPassword() + "**\n\n"
                       + "📲 To get started, log in to your account using the credentials above.\n"
                       + "🔹 Ensure to change your password after the first login for security reasons.\n\n"
                       + "If you have any questions or need assistance, feel free to reach out to our support team.\n\n"
                       + "**Happy Transacting!** 🚀\n\n"
                       + "Best Regards,\n"
                       + "**GoodPay Team**";

        emailService.sendEmail(savedUser.getEmail(), subject, message);

        return modelMapper.map(savedUser, UserResponseDTO.class);
    }


    @Override
    public UserResponseDTO getUserByMobile(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber)
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .orElseThrow(() -> new UserNotFoundException("User not found with mobile: " + mobileNumber));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findByStatus("ACTIVE").stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(int id, UpdateUserDTO updateUserDTO, String modifiedBy) {
        validateRequest(updateUserDTO);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        modelMapper.map(updateUserDTO, user);
        user.setModifiedBy(modifiedBy);

        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserResponseDTO.class);
    }

    @Override
    public void softDeleteUser(int id, String modifiedBy) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        user.setStatus("INACTIVE");
        user.setModifiedBy(modifiedBy);
        userRepository.save(user);
    }

    private void validateRequest(Object request) {
        if (request == null) {
            throw new InvalidRequestException("Request body cannot be null");
        }
    }
}
