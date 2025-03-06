package in.co.goodpay.api.service.impl;

import in.co.goodpay.api.dto.*;
import in.co.goodpay.api.entity.User;
import in.co.goodpay.api.exception.*;
import in.co.goodpay.api.repository.UserRepository;
import in.co.goodpay.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO, String createdBy) {
        validateRequest(userRequestDTO);

        if (userRepository.findByMobileNumber(userRequestDTO.getMobileNumber()).isPresent()) {
            throw new DuplicateUserException("User already exists with mobile: " + userRequestDTO.getMobileNumber());
        }

        User user = modelMapper.map(userRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword())); // Encrypt password
        user.setStatus("ACTIVE");
        user.setCreatedBy(createdBy);

        User savedUser = userRepository.save(user);
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
