package in.co.goodpay.api.service.impl;

import in.co.goodpay.api.dto.UserRequestDTO;
import in.co.goodpay.api.dto.UserResponseDTO;
import in.co.goodpay.api.dto.WalletResponseDTO;
import in.co.goodpay.api.entity.User;
import in.co.goodpay.api.entity.Wallet;
import in.co.goodpay.api.exception.UserNotFoundException;
import in.co.goodpay.api.repository.UserRepository;
import in.co.goodpay.api.repository.WalletRepository;
import in.co.goodpay.api.service.AdminService;
import in.co.goodpay.api.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WalletRepository walletRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public UserResponseDTO addUser(UserRequestDTO userRequestDTO, String createdBy) {
        User user = modelMapper.map(userRequestDTO, User.class);
        user.setStatus("ACTIVE");
        user.setCreatedBy(createdBy);
        User savedUser = userRepository.save(user);
        
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(java.math.BigDecimal.ZERO);
        wallet.setStatus("ACTIVE");
        wallet.setCreatedBy(createdBy);
        walletRepository.save(wallet);
        
        // Send email with login details
        String subject = "Welcome to GoodPay!";
        String message = String.format("Dear %s,\n\nYour GoodPay account has been created successfully.\n\nLogin Details:\nMobile Number: %s\nPassword: %s\n\nBest Regards,\nGoodPay Team",
                savedUser.getName(), savedUser.getMobileNumber(), userRequestDTO.getPassword());
        emailService.sendEmail(savedUser.getEmail(), subject, message);
        
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> getAllUsers(int page, int size) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));
        return users.stream().map(user -> modelMapper.map(user, UserResponseDTO.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void blockUser(int userId, String modifiedBy) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setStatus("BLOCKED");
        user.setModifiedBy(modifiedBy);
        userRepository.save(user);
    }

    @Override
    public long getUserCount() {
        return userRepository.count();
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(int userId, UserRequestDTO userRequestDTO, String modifiedBy) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        modelMapper.map(userRequestDTO, user);
        user.setModifiedBy(modifiedBy);
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserResponseDTO.class);
    }

//    @Override
//    public List<UserResponseDTO> searchUsers(String keyword) {
//        List<User> users = userRepository.searchByKeyword(keyword);
//        return users.stream().map(user -> modelMapper.map(user, UserResponseDTO.class)).collect(Collectors.toList());
//    }

    @Override
    @Transactional
    public void changeUserStatus(int userId, String status, String modifiedBy) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setStatus(status);
        user.setModifiedBy(modifiedBy);
        userRepository.save(user);
    }

    @Override
    public WalletResponseDTO getUserWallet(int userId) {
        Wallet wallet = walletRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Wallet not found"));
        return modelMapper.map(wallet, WalletResponseDTO.class);
    }

    @Override
    @Transactional
    public void resetUserPassword(int userId, String newPassword, String modifiedBy) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPassword(newPassword);
        user.setModifiedBy(modifiedBy);
        userRepository.save(user);
    }

//    @Override
//    @Transactional
//    public void assignRoles(int userId, List<String> roles, String modifiedBy) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
//        user.setRole(roles);
//        user.setModifiedBy(modifiedBy);
//        userRepository.save(user);
//    }
}
