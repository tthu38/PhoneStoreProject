USE PhoneSt;
GO

-- Kiểm tra dữ liệu trong bảng Roles
SELECT * FROM Roles;

-- Kiểm tra dữ liệu trong bảng Users
SELECT 
    UserID,
    Email,
    FullName,
    PhoneNumber,
    IsActive,
    RoleID,
    IsOauthUser,
    OauthProvider,
    GoogleId,
    Picture,
    VerifiedEmail,
    GivenName,
    FamilyName,
    GoogleLink,
    CreatedAt
FROM Users; 