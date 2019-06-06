---云端数据库建立TB_DataSynchLog
---原理:通过查询ERP端-基础资料同步日志TB_DataSynchLog 时间戳与各个基础资料表的时间戳进行比较
If Exists(Select 1 From Sysobjects WHere Name='TB_DataSynchLog' And xType='U')
Drop table TB_DataSynchLog
GO
Create Table TB_DataSynchLog
(
FClassID Int Not Null,UNIQUE (FClassID),
FDataType Varchar(200),
FTableName Varchar(100),
FDescription Varchar(255),
FModifyTime binary(12),
Primary Key (FClassID)
)
GO
If Exists(Select 1 From Sysobjects WHere Name='GetNewBasicItemList' And xType='P')
Drop Procedure GetNewBasicItemList
GO
Create Procedure GetNewBasicItemList
AS
Declare @ClassID Int
Declare @ModifyTime binary(8)

---1、同步客户
Select @ModifyTime=IsNUll(FModifyTime,0x0000000) From TB_DataSynchLog Where FClassID=1
Set @ModifyTime=IsNUll(@ModifyTime,0x0000000)
Select 1 AS 类别,A.FNumber AS 客户代码,A.FName AS 客户名称,B.FName AS 销售代表,A.FItemID,ISNULL(A.FEmployee,0) AS FEmpID,A.FModifyTime 
From t_Organization A
Left  Join t_Emp B On B.FItemID=A.FEmployee
Where A.FDeleted=0 And A.FModifyTime>@ModifyTime
Order By A.FNumber 

Delete From TB_DataSynchLog Where FClassID=1
Insert Into TB_DataSynchLog
(FClassID,FDataType,FTableName,FDescription,FModifyTime)
Select 1,'客户','T_Organization','',Max(FModifyTime)
From t_Organization 
Where FDeleted=0

---2、同步部门
Select @ModifyTime=IsNUll(FModifyTime,0x0000000) From TB_DataSynchLog Where FClassID=2
Set @ModifyTime=IsNUll(@ModifyTime,0x0000000)
--部门查询脚本
Select 2 AS 类别,A.FNumber AS 部门代码,A.FName AS 部门名称,A.FItemID,A.FModifyTime 
From T_Department A
Where A.FDeleted=0 And A.FModifyTime>@ModifyTime
Order By A.FNumber

Delete From TB_DataSynchLog Where FClassID=2
Insert Into TB_DataSynchLog
(FClassID,FDataType,FTableName,FDescription,FModifyTime)
Select 2,'部门','T_Department','',Max(FModifyTime)
From T_Emp  
Where FDeleted=0

---3、同步职员
Select @ModifyTime=IsNUll(FModifyTime,0x0000000) From TB_DataSynchLog Where FClassID=3
Set @ModifyTime=IsNUll(@ModifyTime,0x0000000)
--职员查询脚本
Select 3 AS 类别,A.FNumber AS 员工编号,A.FName AS 姓名,B.FName AS 部门名称,A.FItemID,IsNull(A.FDepartmentID,0) AS FDeptID,A.FModifyTime 
From T_Emp A
Left  Join t_Department B On B.FItemID=A.FDepartmentID
Where A.FDeleted=0 And A.FModifyTime>@ModifyTime
Order By A.FNumber

Delete From TB_DataSynchLog Where FClassID=3
Insert Into TB_DataSynchLog
(FClassID,FDataType,FTableName,FDescription,FModifyTime)
Select 3,'职员','T_Emp','',Max(FModifyTime)
From T_Emp  
Where FDeleted=0

---4、同步物料
Select @ModifyTime=IsNUll(FModifyTime,0x0000000) From TB_DataSynchLog Where FClassID=4
Set @ModifyTime=IsNUll(@ModifyTime,0x0000000)
--物料查询脚本
Select 4 AS 类别,A.FNumber AS 物料编码,A.FName AS 物料名称,A.FModel AS 规格型号,B.FName AS 单位,C.FName AS 属性,A.FItemID,A.FModifyTime 
From T_ICItem A
Inner Join t_MeasureUnit B On B.FMeasureUnitID=A.FUnitID
Inner Join t_SubMessage C On C.FInterID=A.FErpClsID
Where A.FDeleted=0 And A.FModifyTime>@ModifyTime
Order By A.FNumber

Delete From TB_DataSynchLog Where FClassID=4
Insert Into TB_DataSynchLog
(FClassID,FDataType,FTableName,FDescription,FModifyTime)
Select 4,'产品','T_ICItem','',Max(FModifyTime)
From T_ICItem 
Where FDeleted=0

---5、同步仓库
Select @ModifyTime=FModifyTime From TB_DataSynchLog Where FClassID=5
Set @ModifyTime=IsNUll(@ModifyTime,0x0000000)
--仓库查询脚本
Select 5 AS 类别,A.FNumber AS 仓库编码,A.FName AS 仓库名称,A.FMRPAvail AS 可用性,A.FItemID,A.FModifyTime 
From T_Stock A
Where A.FDeleted=0 And A.FModifyTime>@ModifyTime
Order By A.FNumber

Delete From TB_DataSynchLog Where FClassID=5
Insert Into TB_DataSynchLog
(FClassID,FDataType,FTableName,FDescription,FModifyTime)
Select 5,'仓库','T_Stock','',Max(FModifyTime)
From T_Stock 
Where FDeleted=0

---6、同步供应商
Select @ModifyTime=IsNUll(FModifyTime,0x0000000) From TB_DataSynchLog Where FClassID=8
Set @ModifyTime=IsNUll(@ModifyTime,0x0000000)
--供应商查询脚本
Select 8 AS 类别,A.FNumber AS 供应商代码,A.FName AS 供应商名称,A.FItemID,A.FModifyTime 
From t_Supplier A
Where A.FDeleted=0 And A.FModifyTime>@ModifyTime
Order By A.FNumber

Delete From TB_DataSynchLog Where FClassID=8
Insert Into TB_DataSynchLog
(FClassID,FDataType,FTableName,FDescription,FModifyTime)
Select 8,'供应商','t_Supplier','',Max(FModifyTime)
From t_Supplier 
Where FDeleted=0


---7、同步币别
Select @ModifyTime=IsNUll(FModifyTime,0x0000000) From TB_DataSynchLog Where FClassID=4000001
Set @ModifyTime=IsNUll(@ModifyTime,0x0000000)
--供应商查询脚本
Select 4000001 AS 类别,A.FNumber AS 供应商代码,A.FName AS 供应商名称,A.FCurrencyID,A.FModifyTime 
From t_Currency A
Where A.FCurrencyID<>0 And A.FModifyTime>@ModifyTime
Order By A.FNumber

Delete From TB_DataSynchLog Where FClassID=4000001
Insert Into TB_DataSynchLog
(FClassID,FDataType,FTableName,FDescription,FModifyTime)
Select 4000001,'币别','t_Currency','',Max(FModifyTime)
From t_Supplier 
Where FCurrencyID<>0


--8、物料分类
Select FItemID,FNumber,FName,FParentID,FModifyTime
From t_Item 
Where FItemClassID=4 And FDetail=0 And FDeleted=0

Select FItemID as id,FNumber as number,FName as name,FParentID as parentId,FModifyTime as modifyTime
From t_Item 
Where FItemClassID=4 And FDetail=0 And FDeleted=0



---9、仓库库存查询
Select C.FName AS 仓库,D.FName AS 仓位, B.FNumber AS 物料编码,B.FName AS 物料编码,ISNULL(B.FModel,'') AS 规格型号,E.FName AS 辅助属性,ISNULL(A.FBatchNo,'') AS 批号,
F.FName AS 单位,A.FQty/F.FCoefficient AS 数量
From ICInventory A
Inner Join t_ICItem B On B.FItemID=A.FItemID
Inner Join t_Stock C On C.FItemID=A.FStockID
Left  Join t_StockPlace D On D.FSPID=A.FStockPlaceID
Left  Join t_AuxItem E On E.FItemID=A.FAuxPropID
Inner Join t_MeasureUnit F On F.FMeasureUnitID=B.FStoreUnitID
Where C.FName Like '%仓库参数%'
And B.FNumber+B.FName+ISNULL(B.FModel,'') Like  '%仓库参数%'


GO