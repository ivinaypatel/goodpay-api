package in.co.goodpay.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.goodpay.api.common.ApiResponse;
import in.co.goodpay.api.dto.UserRequestDTO;
import in.co.goodpay.api.dto.UserResponseDTO;
import in.co.goodpay.api.dto.WalletResponseDTO;
import in.co.goodpay.api.service.AdminService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@PostMapping("/users")
	public ResponseEntity<ApiResponse<UserResponseDTO>> addUser(@RequestBody UserRequestDTO userRequestDTO,
			@RequestParam String createdBy) {
		UserResponseDTO user = adminService.addUser(userRequestDTO, createdBy);
		return ResponseEntity.ok(new ApiResponse<>(true, "User created successfully", HttpStatus.CREATED, user));
	}

	@GetMapping("/users")
	public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		List<UserResponseDTO> users = adminService.getAllUsers(page, size);
		return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched successfully", HttpStatus.OK, users));
	}

	@PutMapping("/users/{userId}/block")
	public ResponseEntity<ApiResponse<Void>> blockUser(@PathVariable int userId, @RequestParam String modifiedBy) {
		adminService.blockUser(userId, modifiedBy);
		return ResponseEntity.ok(new ApiResponse<>(true, "User blocked successfully", HttpStatus.OK, null));
	}

	@GetMapping("/users/count")
	public ResponseEntity<ApiResponse<Long>> getUserCount() {
		long count = adminService.getUserCount();
		return ResponseEntity.ok(new ApiResponse<>(true, "User count retrieved successfully", HttpStatus.OK, count));
	}

	@PutMapping("/users/{userId}")
	public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(@PathVariable int userId,
			@RequestBody UserRequestDTO userRequestDTO, @RequestParam String modifiedBy) {
		UserResponseDTO updatedUser = adminService.updateUser(userId, userRequestDTO, modifiedBy);
		return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", HttpStatus.OK, updatedUser));
	}

//	@GetMapping("/users/search")
//	public ResponseEntity<ApiResponse<List<UserResponseDTO>>> searchUsers(@RequestParam String keyword) {
//		List<UserResponseDTO> users = adminService.searchUsers(keyword);
//		return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", HttpStatus.OK, users));
//	}

	@PutMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<Void>> changeUserStatus(@PathVariable int userId, 
                                                              @RequestParam String status, 
                                                              @RequestParam String modifiedBy) {
        adminService.changeUserStatus(userId, status, modifiedBy);
        return ResponseEntity.ok(new ApiResponse<>(true, "User status changed successfully", HttpStatus.OK, null));
    }

	@GetMapping("/users/{userId}/wallet")
    public ResponseEntity<ApiResponse<WalletResponseDTO>> getUserWallet(@PathVariable int userId) {
        WalletResponseDTO wallet = adminService.getUserWallet(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "User wallet details retrieved successfully", HttpStatus.OK, wallet));
    }

	@PutMapping("/users/{userId}/password")
    public ResponseEntity<ApiResponse<Void>> resetUserPassword(@PathVariable int userId, 
                                                               @RequestParam String newPassword, 
                                                               @RequestParam String modifiedBy) {
        adminService.resetUserPassword(userId, newPassword, modifiedBy);
        return ResponseEntity.ok(new ApiResponse<>(true, "User password reset successfully", HttpStatus.OK, null));
    }
//
//	@PutMapping("/users/{userId}/roles")
//    public ResponseEntity<ApiResponse<Void>> assignRoles(@PathVariable int userId, 
//                                                         @RequestBody List<String> roles, 
//                                                         @RequestParam String modifiedBy) {
//        adminService.assignRoles(userId, roles, modifiedBy);
//        return ResponseEntity.ok(new ApiResponse<>(true, "User roles assigned successfully", HttpStatus.OK, null));
//    }
}
