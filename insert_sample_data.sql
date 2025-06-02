USE PhoneSt;
GO

-- Xóa dữ liệu cũ (nếu cần)
DELETE FROM Users;
DELETE FROM Roles;
GO

-- Thêm roles
SET IDENTITY_INSERT Roles ON;
INSERT INTO Roles (RoleID, RoleName) VALUES 
(1, 'Admin'),
(2, 'Customer');
SET IDENTITY_INSERT Roles OFF;
GO

-- Thêm users mẫu
-- Mật khẩu mẫu: 123456 (đã được hash bằng BCrypt)
INSERT INTO Users (Email, Password, FullName, PhoneNumber, IsActive, RoleID, IsOauthUser, VerifiedEmail) VALUES 
('admin@phonestore.com', '$2a$10$VNb6EGbXMF.ZHv7GlRFbG.EB0QA8GEzBxYlGgYHZqkJoZVYyRCLOi', N'Admin User', '0123456789', 1, 1, 0, 1),
('customer@gmail.com', '$2a$10$VNb6EGbXMF.ZHv7GlRFbG.EB0QA8GEzBxYlGgYHZqkJoZVYyRCLOi', N'Customer User', '0987654321', 1, 2, 0, 1);

-- Thêm một user đăng nhập bằng Google mẫu
INSERT INTO Users (Email, FullName, IsActive, RoleID, IsOauthUser, OauthProvider, GoogleId, Picture, VerifiedEmail) VALUES 
('google.user@gmail.com', N'Google User', 1, 2, 1, 'GOOGLE', '123456789', 'https://lh3.googleusercontent.com/photo.jpg', 1);
GO 