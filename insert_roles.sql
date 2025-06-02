USE PhoneSt;
GO

-- Thêm roles nếu chưa tồn tại
IF NOT EXISTS (SELECT * FROM Roles WHERE RoleName = 'Admin')
BEGIN
    INSERT INTO Roles (RoleName) VALUES ('Admin');
END

IF NOT EXISTS (SELECT * FROM Roles WHERE RoleName = 'Customer')
BEGIN
    INSERT INTO Roles (RoleName) VALUES ('Customer');
END

GO 