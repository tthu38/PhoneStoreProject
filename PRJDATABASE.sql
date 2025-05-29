create database PhoneStore
use PhoneStore

CREATE TABLE [Roles] (
  [RoleID] INT IDENTITY(1,1) PRIMARY KEY,
  [RoleName] NVARCHAR(20) UNIQUE NOT NULL
);
INSERT INTO [Roles] ([RoleName]) VALUES 
(N'Admin'),
(N'Customer');
select * from Roles
--
CREATE TABLE [Users] (
  [UserID] INT IDENTITY(1,1) PRIMARY KEY,
  [Email] NVARCHAR(100) UNIQUE NOT NULL,
  [HashPassword] VARCHAR(150) NOT NULL,
  [FullName] NVARCHAR(100) NOT NULL,
  [PhoneNumber] VARCHAR(15) NOT NULL,
  [Role] NVARCHAR(15) DEFAULT 'Customer',
  [RememberToken] VARCHAR(255),
  [Picture] VARCHAR(255),
  [IsOauthUser] BIT DEFAULT (0),
  [OauthProvider] NVARCHAR(50),
  [Status] NVARCHAR(15) DEFAULT 'Active',
  [CreatedAt] DATETIME DEFAULT (GETDATE()),
  
);
--
CREATE TABLE [Categories] (
  [CategoryID] INT IDENTITY(1,1) PRIMARY KEY,
  [CategoryName] NVARCHAR(100) NOT NULL,
  [Description] NVARCHAR(MAX),

);

CREATE TABLE [Brands] (
  [BrandID] INT IDENTITY(1,1) PRIMARY KEY,
  [BrandName] NVARCHAR(100) UNIQUE NOT NULL,
  [IsActive] BIT DEFAULT (1)
);
--
CREATE TABLE [ProductBase] (
  [ProductBaseID] INT IDENTITY(1,1) PRIMARY KEY,
  [ProductName] NVARCHAR(200) NOT NULL,
  [CategoryID] INT NOT NULL,
  [BrandID] INT NOT NULL,
  [Description] NVARCHAR(MAX),
  [Specifications] NVARCHAR(MAX),
  [IsDeleted] BIT DEFAULT (0),
  [CreatedAt] DATETIME DEFAULT (GETDATE()),
  FOREIGN KEY ([CategoryID]) REFERENCES [Categories]([CategoryID]),
  FOREIGN KEY ([BrandID]) REFERENCES [Brands]([BrandID])
);
--
CREATE TABLE [ProductVariants] (
  [VariantID] INT IDENTITY(1,1) PRIMARY KEY,
  [ProductBaseID] INT NOT NULL,
  [Color] NVARCHAR(50) NOT NULL,
  [RAM] INT,
  [ROM] INT,
  [SKU] NVARCHAR(50) UNIQUE NOT NULL,
  [Price] DECIMAL(18,2) NOT NULL,
  [DiscountPrice] DECIMAL(18,2) NULL,
  [DiscountExpiry] DATETIME NULL,
  [StockQuantity] INT DEFAULT (0),
  [ImageURLs] NVARCHAR(MAX),
  [IsActive] BIT DEFAULT (1),
  FOREIGN KEY ([ProductBaseID]) REFERENCES [ProductBase]([ProductBaseID])
);

--
CREATE TABLE [Warehouses] (
  [WarehouseID] INT IDENTITY(1,1) PRIMARY KEY,
  [WarehouseName] NVARCHAR(100) NOT NULL,
  [Address] NVARCHAR(500) NOT NULL,
  [PhoneNumber] NVARCHAR(20) NOT NULL,
  [IsActive] BIT DEFAULT (1)
);
--
CREATE TABLE [InventoryLogs] (
  [LogID] INT IDENTITY(1,1) PRIMARY KEY,
  [WarehouseID] INT NOT NULL,
  [VariantID] INT NOT NULL,
  [ChangeType] NVARCHAR(20) NOT NULL CHECK ([ChangeType] IN ('Import', 'Export', 'Adjustment')),
  [Quantity] INT NULL,
  [PreviousQuantity] INT NOT NULL,
  [NewQuantity] INT NOT NULL,
  [CreatedAt] DATETIME DEFAULT (GETDATE()),
  FOREIGN KEY ([WarehouseID]) REFERENCES [Warehouses]([WarehouseID]),
  FOREIGN KEY ([VariantID]) REFERENCES [ProductVariants]([VariantID])
);
--
CREATE TABLE [ShoppingCart] (
  [CartID] INT IDENTITY(1,1) PRIMARY KEY,
  [UserID] INT NOT NULL,
  FOREIGN KEY ([UserID]) REFERENCES [Users]([UserID])
);
--
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
--?
CREATE TABLE [UserAddresses] (
  [AddressID] INT IDENTITY(1,1) PRIMARY KEY,
  [UserID] INT NOT NULL,
  [FullName] NVARCHAR(255) NOT NULL,
  [PhoneNumber] VARCHAR(20) NOT NULL,
  [Address] NVARCHAR(MAX) NOT NULL,
  [IsDefault] BIT DEFAULT (0),
  [IsActive] BIT DEFAULT (1),
  [CreatedAt] DATETIME DEFAULT (GETDATE()),
  [UpdatedAt] DATETIME,
  FOREIGN KEY ([UserID]) REFERENCES [Users]([UserID])
);

CREATE TABLE [Orders] (
  [OrderID] INT IDENTITY(1,1) PRIMARY KEY,
  [UserID] INT NOT NULL,
  [OrderDate] DATETIME DEFAULT (GETDATE()),
  [TotalAmount] DECIMAL(18,2) NOT NULL,
  [DiscountAmount] DECIMAL(18,2) DEFAULT (0),
  [FinalAmount] DECIMAL(18,2) NOT NULL,
  [OrderStatus] INT NOT NULL,
  [PaymentStatusID] INT NOT NULL,
  [UserAddressID] INT NOT NULL,
  [PaymentMethodID] INT NOT NULL,
  [PaymentTransactionID] NVARCHAR(100),
  FOREIGN KEY ([UserID]) REFERENCES [Users]([UserID]),
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

