package in.co.goodpay.api.service;

import java.util.List;

import in.co.goodpay.api.dto.UserRequestDTO;
import in.co.goodpay.api.dto.UserResponseDTO;
import in.co.goodpay.api.dto.WalletResponseDTO;

public interface AdminService {
    UserResponseDTO addUser(UserRequestDTO userRequestDTO, String createdBy);
    List<UserResponseDTO> getAllUsers(int page, int size);
    void blockUser(int userId, String modifiedBy);
    long getUserCount();
    UserResponseDTO updateUser(int userId, UserRequestDTO userRequestDTO, String modifiedBy);
  //  List<UserResponseDTO> searchUsers(String keyword);
    void changeUserStatus(int userId, String status, String modifiedBy);
    WalletResponseDTO getUserWallet(int userId);
    void resetUserPassword(int userId, String newPassword, String modifiedBy);
   // void assignRoles(int userId, List<String> roles, String modifiedBy);
}

