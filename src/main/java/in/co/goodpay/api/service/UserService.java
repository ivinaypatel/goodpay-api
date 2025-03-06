package in.co.goodpay.api.service;

import in.co.goodpay.api.dto.*;
import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO, String createdBy);
    UserResponseDTO getUserByMobile(String mobileNumber);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(int id, UpdateUserDTO updateUserDTO, String modifiedBy);
    void softDeleteUser(int id, String modifiedBy);
}
