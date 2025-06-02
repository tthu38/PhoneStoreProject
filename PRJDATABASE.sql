create database PhoneSt
use PhoneSt

CREATE TABLE [Roles] (
  [RoleID] INT IDENTITY(1,1) PRIMARY KEY,
  [RoleName] NVARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE [Users] (
  [UserID] INT IDENTITY(1,1) PRIMARY KEY,
  [Email] NVARCHAR(100) UNIQUE NOT NULL,
  [Password] NVARCHAR(255),
  [FullName] NVARCHAR(100) NOT NULL,
  [PhoneNumber] NVARCHAR(20),
  [RoleID] INT NOT NULL,
  [RememberToken] NVARCHAR(255),
  [Picture] NVARCHAR(255),
  [IsOauthUser] BIT DEFAULT (0),
  [OauthProvider] NVARCHAR(50),
  [IsActive] BIT DEFAULT (1),
  [CreatedAt] DATETIME DEFAULT (GETDATE()),
  FOREIGN KEY ([RoleID]) REFERENCES [Roles]([RoleID])
);

CREATE TABLE [Categories] (
  [CategoryID] INT IDENTITY(1,1) PRIMARY KEY,
  [CategoryName] NVARCHAR(100) NOT NULL,
  [ParentCategoryID] INT,
  [IsActive] BIT DEFAULT (1),
  FOREIGN KEY ([ParentCategoryID]) REFERENCES [Categories]([CategoryID])
);

CREATE TABLE [Brands] (
  [BrandID] INT IDENTITY(1,1) PRIMARY KEY,
  [BrandName] NVARCHAR(100) UNIQUE NOT NULL,
  [IsActive] BIT DEFAULT (1)
);

CREATE TABLE [ProductBase] (
  [ProductBaseID] INT IDENTITY(1,1) PRIMARY KEY,
  [ProductName] NVARCHAR(200) NOT NULL,
  [CategoryID] INT NOT NULL,
  [BrandID] INT NOT NULL,
  [Description] NVARCHAR(MAX),
  [Specifications] NVARCHAR(MAX),
  [IsActive] BIT DEFAULT (1),
  [CreatedAt] DATETIME DEFAULT (GETDATE()),
  FOREIGN KEY ([CategoryID]) REFERENCES [Categories]([CategoryID]),
  FOREIGN KEY ([BrandID]) REFERENCES [Brands]([BrandID])
);

CREATE TABLE [ProductVariants] (
  [VariantID] INT IDENTITY(1,1) PRIMARY KEY,
  [ProductBaseID] INT NOT NULL,
  [Color] NVARCHAR(50) NOT NULL,
  [RAM] INT,
  [ROM] INT,
  [SKU] NVARCHAR(50) UNIQUE NOT NULL,
  [Price] DECIMAL(18,2) NOT NULL,
  [StockQuantity] INT DEFAULT (0),
  [ImageURLs] NVARCHAR(MAX),
  [IsActive] BIT DEFAULT (1),
  FOREIGN KEY ([ProductBaseID]) REFERENCES [ProductBase]([ProductBaseID])
);

CREATE TABLE [Warehouses] (
  [WarehouseID] INT IDENTITY(1,1) PRIMARY KEY,
  [WarehouseName] NVARCHAR(100) NOT NULL,
  [Address] NVARCHAR(500) NOT NULL,
  [PhoneNumber] NVARCHAR(20) NOT NULL,
  [IsActive] BIT DEFAULT (1)
);

CREATE TABLE [InventoryLogs] (
  [LogID] INT IDENTITY(1,1) PRIMARY KEY,
  [WarehouseID] INT NOT NULL,
  [VariantID] INT NOT NULL,
  [ChangeType] NVARCHAR(255) NOT NULL CHECK ([ChangeType] IN ('Import', 'Export', 'Adjustment')),
  [Quantity] INT NOT NULL,
  [PreviousQuantity] INT NOT NULL,
  [NewQuantity] INT NOT NULL,
  [ReferenceID] NVARCHAR(100),
  [CreatedAt] DATETIME DEFAULT (GETDATE()),
  FOREIGN KEY ([WarehouseID]) REFERENCES [Warehouses]([WarehouseID]),
  FOREIGN KEY ([VariantID]) REFERENCES [ProductVariants]([VariantID])
);

CREATE TABLE [ShoppingCart] (
  [CartID] INT IDENTITY(1,1) PRIMARY KEY,
  [UserID] INT NOT NULL,
  [CreatedAt] DATETIME DEFAULT (GETDATE()),
  [UpdatedAt] DATETIME,
  FOREIGN KEY ([UserID]) REFERENCES [Users]([UserID])
);

CREATE TABLE [ShoppingCartItems] (
  [CartItemID] INT IDENTITY(1,1) PRIMARY KEY,
  [CartID] INT NOT NULL,
  [VariantID] INT NOT NULL,
  [Quantity] INT NOT NULL DEFAULT (1),
  [UnitPrice] DECIMAL(18,2) NOT NULL,
  FOREIGN KEY ([CartID]) REFERENCES [ShoppingCart]([CartID]),
  FOREIGN KEY ([VariantID]) REFERENCES [ProductVariants]([VariantID])
);

CREATE TABLE [PaymentMethods] (
  [PaymentMethodID] INT IDENTITY(1,1) PRIMARY KEY,
  [MethodName] NVARCHAR(50) NOT NULL,
  [IsActive] BIT DEFAULT (1)
);

CREATE TABLE [OrderStatus] (
  [StatusID] INT IDENTITY(1,1) PRIMARY KEY,
  [StatusName] NVARCHAR(20) UNIQUE NOT NULL,
  [Description] NVARCHAR(255),
  [IsActive] BIT DEFAULT (1)
);

CREATE TABLE [PaymentStatus] (
  [StatusID] INT IDENTITY(1,1) PRIMARY KEY,
  [StatusName] NVARCHAR(20) UNIQUE NOT NULL,
  [Description] NVARCHAR(255),
  [IsActive] BIT DEFAULT (1)
);

CREATE TABLE [UserAddresses] (
  [AddressID] INT IDENTITY(1,1) PRIMARY KEY,
  [UserID] INT NOT NULL,
  [FullName] NVARCHAR(100) NOT NULL,
  [PhoneNumber] NVARCHAR(20) NOT NULL,
  [Address] NVARCHAR(500) NOT NULL,
  [IsDefault] BIT DEFAULT (0),
  [IsActive] BIT DEFAULT (1),
  [CreatedAt] DATETIME DEFAULT (GETDATE()),
  [UpdatedAt] DATETIME,
  FOREIGN KEY ([UserID]) REFERENCES [Users]([UserID])
);

CREATE TABLE [Orders] (
  [OrderID] INT IDENTITY(1,1) PRIMARY KEY,
  [UserID] INT NOT NULL,
  [OrderNumber] NVARCHAR(50) UNIQUE NOT NULL,
  [OrderDate] DATETIME DEFAULT (GETDATE()),
  [TotalAmount] DECIMAL(18,2) NOT NULL,
  [ShippingFee] DECIMAL(18,2) DEFAULT (0),
  [DiscountAmount] DECIMAL(18,2) DEFAULT (0),
  [FinalAmount] DECIMAL(18,2) NOT NULL,
  [OrderStatusID] INT NOT NULL,
  [PaymentStatusID] INT NOT NULL,
  [UserAddressID] INT NOT NULL,
  [PaymentMethodID] INT NOT NULL,
  [PaymentTransactionID] NVARCHAR(100),
  FOREIGN KEY ([UserID]) REFERENCES [Users]([UserID]),
  FOREIGN KEY ([OrderStatusID]) REFERENCES [OrderStatus]([StatusID]),
  FOREIGN KEY ([PaymentStatusID]) REFERENCES [PaymentStatus]([StatusID]),
  FOREIGN KEY ([UserAddressID]) REFERENCES [UserAddresses]([AddressID]),
  FOREIGN KEY ([PaymentMethodID]) REFERENCES [PaymentMethods]([PaymentMethodID])
);

CREATE TABLE [OrderDetails] (
  [OrderDetailID] INT IDENTITY(1,1) PRIMARY KEY,
  [OrderID] INT NOT NULL,
  [VariantID] INT NOT NULL,
  [WarehouseID] INT NOT NULL,
  [Quantity] INT NOT NULL,
  [UnitPrice] DECIMAL(18,2) NOT NULL,
  [TotalPrice] DECIMAL(18,2) NOT NULL,
  FOREIGN KEY ([OrderID]) REFERENCES [Orders]([OrderID]),
  FOREIGN KEY ([VariantID]) REFERENCES [ProductVariants]([VariantID]),
  FOREIGN KEY ([WarehouseID]) REFERENCES [Warehouses]([WarehouseID])
);

CREATE TABLE [AITraining] (
  [TrainingID] INT IDENTITY(1,1) PRIMARY KEY,
  [ProductBaseID] INT NOT NULL,
  [VariantID] INT NOT NULL,
  [ProductName] NVARCHAR(200) NOT NULL,
  [BrandName] NVARCHAR(100) NOT NULL,
  [CategoryName] NVARCHAR(100) NOT NULL,
  [Color] NVARCHAR(50),
  [RAM] INT,
  [ROM] INT,
  [Price] DECIMAL(18,2),
  [Specifications] NVARCHAR(MAX),
  [Description] NVARCHAR(MAX),
  [IsActive] BIT DEFAULT (1),
  [CreatedAt] DATETIME DEFAULT (GETDATE()),
  FOREIGN KEY ([ProductBaseID]) REFERENCES [ProductBase]([ProductBaseID]),
  FOREIGN KEY ([VariantID]) REFERENCES [ProductVariants]([VariantID])
);
ALTER TABLE Users
ADD GoogleId NVARCHAR(50),
    VerifiedEmail BIT DEFAULT (0),
    GivenName NVARCHAR(100),
    FamilyName NVARCHAR(100),
    GoogleLink NVARCHAR(255);

CREATE TRIGGER trg_Insert_AITraining
ON ProductVariants
AFTER INSERT
AS
BEGIN
  SET NOCOUNT ON;

  INSERT INTO AITraining (
    ProductBaseID,
    VariantID,
    ProductName,
    BrandName,
    CategoryName,
    Color,
    RAM,
    ROM,
    Price,
    Specifications,
    Description,
    IsActive,
    CreatedAt
  )
  SELECT 
    pb.ProductBaseID,
    i.VariantID,
    pb.ProductName,
    b.BrandName,
    c.CategoryName,
    i.Color,
    i.RAM,
    i.ROM,
    i.Price,
    pb.Specifications,
    pb.Description,
    i.IsActive,
    GETDATE()
  FROM INSERTED i
  JOIN ProductBase pb ON i.ProductBaseID = pb.ProductBaseID
  JOIN Brands b ON pb.BrandID = b.BrandID
  JOIN Categories c ON pb.CategoryID = c.CategoryID;
END;

SET IDENTITY_INSERT Roles ON;
INSERT INTO Roles (RoleID, RoleName) VALUES 
(1, 'Admin'),
(2, 'Customer');
SET IDENTITY_INSERT Roles OFF;
GO

INSERT INTO Users (Email, Password, FullName, PhoneNumber, IsActive, RoleID, IsOauthUser, VerifiedEmail) VALUES 
('admin@phonestore.com', '$2a$10$VNb6EGbXMF.ZHv7GlRFbG.EB0QA8GEzBxYlGgYHZqkJoZVYyRCLOi', N'Admin User', '0123456789', 1, 1, 0, 1),
('customer@gmail.com', '$2a$10$VNb6EGbXMF.ZHv7GlRFbG.EB0QA8GEzBxYlGgYHZqkJoZVYyRCLOi', N'Customer User', '0987654321', 1, 2, 0, 1);

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